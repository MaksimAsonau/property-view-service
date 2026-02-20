package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public List<HotelShortResponse> getAll() {
        return hotelService.getAll();
    }

    @GetMapping("/{id}")
    public HotelDetailsResponse getById(@PathVariable Long id) {
        return hotelService.getById(id);
    }

    @PostMapping
    public ResponseEntity<HotelShortResponse> create(@Valid @RequestBody HotelCreateRequest request) {
        HotelShortResponse created = hotelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/amenities")
    public HotelDetailsResponse addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }
}
