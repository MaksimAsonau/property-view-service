package by.asonau.propertyviewservice.repository;

import by.asonau.propertyviewservice.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Query("""
           SELECT h.brand, count(h)
           FROM Hotel h
           WHERE h.brand IS NOT NULL AND h.brand <> ''
           GROUP BY h.brand
           """)
    List<Object[]> histogramByBrand();

    @Query("""
           SELECT h.address.city, count(h)
           FROM Hotel h
           WHERE h.address.city IS NOT NULL AND h.address.city <> ''
           GROUP BY h.address.city
           """)
    List<Object[]> histogramByCity();

    @Query("""
           SELECT h.address.country, count(h)
           FROM Hotel h
           WHERE h.address.country IS NOT NULL AND h.address.country <> ''
           GROUP BY h.address.country
           """)
    List<Object[]> histogramByCountry();

    @Query("""
           SELECT a.name, count(distinct h.id)
           FROM Hotel h
           JOIN h.amenities a
           GROUP BY a.name
           """)
    List<Object[]> histogramByAmenities();
}
