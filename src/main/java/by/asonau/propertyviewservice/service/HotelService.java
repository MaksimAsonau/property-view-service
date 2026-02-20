package by.asonau.propertyviewservice.service;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;

import java.util.List;

public interface HotelService {

    List<HotelShortResponse> getAll();

    HotelDetailsResponse getById(Long id);

    HotelShortResponse create(HotelCreateRequest request);

    HotelDetailsResponse addAmenities(Long hotelId, List<String> amenities);

    List<HotelShortResponse> search(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    );
}
