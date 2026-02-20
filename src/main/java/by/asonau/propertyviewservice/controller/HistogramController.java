package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.service.HistogramService;
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

    @GetMapping("/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return histogramService.getHistogram(param);
    }
}
