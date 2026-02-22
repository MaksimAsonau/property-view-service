package by.asonau.propertyviewservice.service.impl;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.exception.NotFoundException;
import by.asonau.propertyviewservice.mapper.HotelMapper;
import by.asonau.propertyviewservice.model.entity.Amenity;
import by.asonau.propertyviewservice.model.entity.Hotel;
import by.asonau.propertyviewservice.repository.AmenityRepository;
import by.asonau.propertyviewservice.repository.HotelRepository;
import by.asonau.propertyviewservice.service.HotelService;
import by.asonau.propertyviewservice.repository.specification.HotelSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private static final String MESSAGE_HOTEL_NOT_FOUND = "Hotel with id=%d not found";

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelShortResponse> getAll() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    public HotelDetailsResponse getById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MESSAGE_HOTEL_NOT_FOUND.formatted(id)));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    @Transactional
    public HotelShortResponse create(HotelCreateRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toShortDto(saved);
    }

    @Override
    @Transactional
    public HotelDetailsResponse addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(MESSAGE_HOTEL_NOT_FOUND.formatted(hotelId)));

        if (amenities == null || amenities.isEmpty()) {
            return hotelMapper.toDetailsDto(hotel);
        }

        for (String raw : amenities) {
            if (raw == null) {
                continue;
            }
            String name = normalizeAmenityName(raw);
            if (name.isBlank()) {
                continue;
            }

            Amenity amenity = getOrCreateAmenity(name);
            hotel.getAmenities().add(amenity);
        }

        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toDetailsDto(saved);
    }

    @Override
    public List<HotelShortResponse> search(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities) {

        Specification<Hotel> spec = HotelSpecifications.nameLike(name)
                .and(HotelSpecifications.brandEquals(brand))
                .and(HotelSpecifications.cityEquals(city))
                .and(HotelSpecifications.countryEquals(country))
                .and(HotelSpecifications.hasAmenitiesAll(amenities));

        return hotelRepository.findAll(spec)
                .stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    private Amenity getOrCreateAmenity(String name) {
        return amenityRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            try {
                return amenityRepository.save(Amenity.builder().name(name).build());
            } catch (DataIntegrityViolationException e) {
                return amenityRepository.findByNameIgnoreCase(name)
                        .orElseThrow(() -> e);
            }
        });
    }

    private String normalizeAmenityName(String raw) {
        return raw.trim().replaceAll("\\s+", " ");
    }
}
