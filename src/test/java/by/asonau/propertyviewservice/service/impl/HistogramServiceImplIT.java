package by.asonau.propertyviewservice.service.impl;

import by.asonau.propertyviewservice.exception.BadRequestException;
import by.asonau.propertyviewservice.service.HistogramService;
import by.asonau.propertyviewservice.service.HotelService;
import by.asonau.propertyviewservice.testutil.TestHotelFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HistogramServiceImplIT {

    @Autowired
    private HistogramService histogramService;

    @Autowired
    private HotelService hotelService;

    @Test
    void getHistogram_ShouldReturnCountsByCity() {
        hotelService.create(TestHotelFactory.createRequestShort("Hotel A", "Brand", "Minsk", "Belarus"));
        hotelService.create(TestHotelFactory.createRequestShort("Hotel B", "Brand", "Minsk", "Belarus"));
        hotelService.create(TestHotelFactory.createRequestShort("Hotel C", "Brand", "Moscow", "Russia"));

        Map<String, Long> result = histogramService.getHistogram("city");

        assertThat(result.get("Minsk")).isEqualTo(2L);
        assertThat(result.get("Moscow")).isEqualTo(1L);
    }

    @Test
    void getHistogram_ShouldReturnCountsByAmenities() {
        Long aId = hotelService.create(TestHotelFactory.createRequestShort("Hotel A", "Brand", "Minsk", "Belarus")).getId();
        Long bId = hotelService.create(TestHotelFactory.createRequestShort("Hotel B", "Brand", "Minsk", "Belarus")).getId();

        hotelService.addAmenities(aId, List.of("Free WiFi", "Spa"));
        hotelService.addAmenities(bId, List.of("Free WiFi"));

        Map<String, Long> result = histogramService.getHistogram("amenities");

        assertThat(result.get("Free WiFi")).isEqualTo(2L);
        assertThat(result.get("Spa")).isEqualTo(1L);
    }

    @Test
    void getHistogram_ShouldThrowBadRequest_WhenParamUnknown() {
        assertThatThrownBy(() -> histogramService.getHistogram("unknown"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid histogram param. Allowed: brand, city, country, amenities");
    }

    @Test
    void getHistogram_ShouldReturnCountsByBrand() {
        hotelService.create(TestHotelFactory.createRequestShort("Hotel A", "Hilton", "Minsk", "Belarus"));
        hotelService.create(TestHotelFactory.createRequestShort("Hotel B", "Hilton", "Moscow", "Russia"));
        hotelService.create(TestHotelFactory.createRequestShort("Hotel C", "Marriott", "Minsk", "Belarus"));

        Map<String, Long> result = histogramService.getHistogram("brand");

        assertThat(result.get("Hilton")).isEqualTo(2L);
        assertThat(result.get("Marriott")).isEqualTo(1L);
    }

    @Test
    void getHistogram_ShouldReturnEmptyMap_WhenNoHotels() {
        Map<String, Long> result = histogramService.getHistogram("city");
        assertThat(result).isEmpty();
    }
}