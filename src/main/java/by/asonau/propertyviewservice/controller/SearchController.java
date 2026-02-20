package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final HotelService hotelService;

    @Operation(
            summary = "Search hotels",
            description = "All parameters are optional. If amenities are provided, hotel must contain all listed amenities."
    )
    @GetMapping
    public List<HotelShortResponse> search(
            @Parameter(description = "Hotel name contains (case-insensitive)", example = "DoubleTree")
            @RequestParam(required = false) String name,

            @Parameter(description = "Exact brand match (case-insensitive)", example = "Hilton")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Exact city match (case-insensitive)", example = "Minsk")
            @RequestParam(required = false) String city,

            @Parameter(description = "Exact country match (case-insensitive)", example = "Belarus")
            @RequestParam(required = false) String country,

            @Parameter(description = "Repeat param: amenities=Free%20WiFi&amenities=Free%20parking", example = "Free WiFi")
            @RequestParam(required = false) List<String> amenities
    ) {
        return hotelService.search(name, brand, city, country, amenities);
    }
}
