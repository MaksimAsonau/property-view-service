package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Get all hotels (short view)")
    @GetMapping
    public List<HotelShortResponse> getAll() {
        return hotelService.getAll();
    }

    @Operation(summary = "Get hotel details by id")
    @GetMapping("/{id}")
    public HotelDetailsResponse getById(
            @Parameter(description = "Hotel id", example = "1")
            @PathVariable Long id) {
        return hotelService.getById(id);
    }

    @Operation(summary = "Create a new hotel")
    @PostMapping
    public ResponseEntity<HotelShortResponse> create(@Valid @RequestBody HotelCreateRequest request) {
        HotelShortResponse created = hotelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Add amenities to hotel",
            description = "Accepts list of strings. Amenities are created if not exist. Duplicates are ignored."
    )
    @PostMapping("/{id}/amenities")
    public HotelDetailsResponse addAmenities(
            @Parameter(description = "Hotel id", example = "1")
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }
}
