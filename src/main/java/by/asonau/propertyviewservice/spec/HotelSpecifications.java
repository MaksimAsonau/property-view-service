package by.asonau.propertyviewservice.spec;

import by.asonau.propertyviewservice.model.entity.Amenity;
import by.asonau.propertyviewservice.model.entity.Hotel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class HotelSpecifications {

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_CITY = "city";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_AMENITIES = "amenities";

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

    public static Specification<Hotel> hasAmenitiesAll(List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            List<String> normalized = normalizeAmenities(amenities);
            if (normalized.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Hotel, Amenity> amenityJoin = root.join(FIELD_AMENITIES, JoinType.INNER);

            Expression<String> amenityNameLower = criteriaBuilder.lower(amenityJoin.get(FIELD_NAME));
            Predicate inPredicate = amenityNameLower.in(normalized);

            if (query != null) {
                query.distinct(true);
                query.groupBy(root.get(FIELD_ID));
                query.having(criteriaBuilder.equal(criteriaBuilder.countDistinct(amenityNameLower), normalized.size()));
            }

            return inPredicate;
        };
    }

    private static List<String> normalizeAmenities(List<String> amenities) {
        if (amenities == null) {
            return List.of();
        }
        return amenities.stream()
                .filter(a -> a != null && !a.trim().isBlank())
                .map(a -> a.trim().toLowerCase())
                .distinct()
                .toList();
    }
}
