package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.service.HistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histogram")
public class HistogramController {

    private final HistogramService histogramService;

    @Operation(
            summary = "Get histogram by parameter",
            description = "param can be: brand, city, country, amenities"
    )
    @GetMapping("/{param}")
    public Map<String, Long> getHistogram(
            @Parameter(description = "Histogram parameter: brand|city|country|amenities", example = "city")
            @PathVariable String param) {
        return histogramService.getHistogram(param);
    }
}
