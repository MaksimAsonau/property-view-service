package by.asonau.propertyviewservice.spec;

import by.asonau.propertyviewservice.model.entity.Amenity;
import by.asonau.propertyviewservice.model.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class HotelSpecifications {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_BRAND = "brand";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_AMENITIES = "amenities";

    private HotelSpecifications() {
    }

    public static Specification<Hotel> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(FIELD_NAME)), "%" + name.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> brandEquals(String brand) {
        return (root, query, criteriaBuilder) -> {
            if (brand == null || brand.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get(FIELD_BRAND)), brand.trim().toLowerCase());
        };
    }

    public static Specification<Hotel> cityEquals(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get(FIELD_ADDRESS).get(FIELD_CITY)),
                    city.trim().toLowerCase()
            );
        };
    }

    public static Specification<Hotel> countryEquals(String country) {
        return (root, query, criteriaBuilder) -> {
            if (country == null || country.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get(FIELD_ADDRESS).get(FIELD_COUNTRY)),
                    country.trim().toLowerCase()
            );
        };
    }

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            if (amenities == null || amenities.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<String> normalized = amenities.stream()
                    .filter(a -> a != null && !a.trim().isBlank())
                    .map(a -> a.trim().toLowerCase())
                    .distinct()
                    .toList();

            if (normalized.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            if (query != null) {
                query.distinct(true);
            }

            Join<Hotel, Amenity> join = root.join(FIELD_AMENITIES, JoinType.LEFT);
            return join.get(FIELD_NAME).in(normalized);
        };
    }
}
