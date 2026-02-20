package by.asonau.propertyviewservice.service.impl;

import by.asonau.propertyviewservice.exception.BadRequestException;
import by.asonau.propertyviewservice.histogram.HistogramParam;
import by.asonau.propertyviewservice.repository.HotelRepository;
import by.asonau.propertyviewservice.service.HistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistogramServiceImpl implements HistogramService {

    private static final String MESSAGE_BAD_PARAM = "Invalid histogram param. Allowed: brand, city, country, amenities";

    private final HotelRepository hotelRepository;

    @Override
    public Map<String, Long> getHistogram(String param) {
        HistogramParam parameter;
        try {
            parameter = HistogramParam.from(param);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(MESSAGE_BAD_PARAM);
        }

        List<Object[]> rows = switch (parameter) {
            case BRAND -> hotelRepository.histogramByBrand();
            case CITY -> hotelRepository.histogramByCity();
            case COUNTRY -> hotelRepository.histogramByCountry();
            case AMENITIES -> hotelRepository.histogramByAmenities();
        };

        return toMap(rows);
    }

    private Map<String, Long> toMap(List<Object[]> rows) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String key = (String) row[0];
            Long count = (Long) row[1];
            result.put(key, count);
        }
        return result;
    }
}
