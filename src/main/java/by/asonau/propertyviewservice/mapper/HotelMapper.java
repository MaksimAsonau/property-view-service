package by.asonau.propertyviewservice.mapper;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.model.embedded.Address;
import by.asonau.propertyviewservice.model.entity.Amenity;
import by.asonau.propertyviewservice.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelCreateRequest dto);

    @Mapping(source = "address", target = "address", qualifiedByName = "addressToString")
    @Mapping(source = "contacts.phone", target = "phone")
    HotelShortResponse toShortDto(Hotel hotel);

    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "amenitiesToNames")
    HotelDetailsResponse toDetailsDto(Hotel hotel);

    @Named("addressToString")
    default String addressToString(Address address) {
        if (address == null) {
            return null;
        }
        return String.format("%s %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }

    @Named("amenitiesToNames")
    default List<String> amenitiesToNames(Set<Amenity> amenities) {
        if (amenities == null) {
            return List.of();
        }
        return amenities.stream()
                .map(Amenity::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
