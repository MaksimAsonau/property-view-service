package by.asonau.propertyviewservice.histogram;

import java.util.Arrays;

public enum HistogramParam {

    BRAND("brand"),
    CITY("city"),
    COUNTRY("country"),
    AMENITIES("amenities");

    private final String value;

    HistogramParam(String value) {
        this.value = value;
    }

    public static HistogramParam from(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException();
        }
        String v = raw.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(p -> p.value.equals(v))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
