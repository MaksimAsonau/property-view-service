package by.asonau.propertyviewservice.service;

import java.util.Map;

public interface HistogramService {

    Map<String, Long> getHistogram(String param);
}
