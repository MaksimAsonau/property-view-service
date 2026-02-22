package by.asonau.propertyviewservice.service.impl;

import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;
import by.asonau.propertyviewservice.dto.response.HotelDetailsResponse;
import by.asonau.propertyviewservice.dto.response.HotelShortResponse;
import by.asonau.propertyviewservice.repository.AmenityRepository;
import by.asonau.propertyviewservice.service.HotelService;
import by.asonau.propertyviewservice.testutil.TestHotelFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HotelServiceImplIT {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private AmenityRepository amenityRepository;

    @Test
    void create_ShouldPersistHotel_AndReturnShort() {
        HotelCreateRequest request = TestHotelFactory.createRequestFull(
                "DoubleTree by Hilton Minsk",
                "Hilton",
                "Pobediteley Avenue",
                9,
                "Minsk",
                "Belarus",
                "220004",
                "+375 17 309-80-00",
                "doubletreeminsk.info@hilton.com"
        );

        HotelShortResponse created = hotelService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(created.getDescription()).isEqualTo(request.getDescription());
        assertThat(created.getPhone()).isEqualTo("+375 17 309-80-00");

        assertThat(created.getAddress())
                .isEqualTo("9 Pobediteley Avenue, Minsk, 220004, Belarus");
    }

    @Test
    void addAmenities_ShouldNotCreateDuplicates_WhenCalledTwice() {
        HotelShortResponse created = hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel A",
                "BrandA",
                "Main Street",
                1,
                "Minsk",
                "Belarus",
                "220000",
                "+111",
                "a@mail.com"
        ));

        HotelDetailsResponse afterFirst = hotelService.addAmenities(
                created.getId(),
                Arrays.asList(" Free   WiFi ", null, "   ")
        );
        HotelDetailsResponse afterSecond = hotelService.addAmenities(
                created.getId(),
                List.of("Free WiFi")
        );

        assertThat(afterFirst.getAmenities()).containsExactly("Free WiFi");
        assertThat(afterSecond.getAmenities()).containsExactly("Free WiFi");
        assertThat(amenityRepository.count()).isEqualTo(1);
    }

    @Test
    void search_ShouldFilterByCity_CaseInsensitive() {
        hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel Minsk",
                "Brand",
                "Street",
                10,
                "Minsk",
                "Belarus",
                "220000",
                "+222",
                "minsk@mail.com"
        ));
        hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel Moscow",
                "Brand",
                "Street",
                11,
                "Moscow",
                "Russia",
                "101000",
                "+333",
                "moscow@mail.com"
        ));

        List<HotelShortResponse> result = hotelService.search(
                null, null, "minsk", null, null
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Hotel Minsk");
    }

    @Test
    void search_ShouldReturnOnlyHotelsWithAllAmenities() {
        HotelShortResponse a = hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel A",
                "Brand",
                "Street",
                1,
                "Minsk",
                "Belarus",
                "220000",
                "+444",
                "a2@mail.com"
        ));
        HotelShortResponse b = hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel B",
                "Brand",
                "Street",
                2,
                "Minsk",
                "Belarus",
                "220000",
                "+555",
                "b2@mail.com"
        ));

        hotelService.addAmenities(a.getId(), List.of("Free WiFi", "Spa"));
        hotelService.addAmenities(b.getId(), List.of("Free WiFi"));

        List<HotelShortResponse> result = hotelService.search(
                null, null, null, null, List.of("Free WiFi", "Spa")
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Hotel A");
    }

    @Test
    void addAmenities_ShouldIgnoreBlankAndNullValues_AndNotCreateAnything() {
        HotelShortResponse created = hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel Blank Amenities",
                "BrandA",
                "Main Street",
                1,
                "Minsk",
                "Belarus",
                "220000",
                "+111",
                "blank@mail.com"
        ));

        HotelDetailsResponse result = hotelService.addAmenities(
                created.getId(),
                Arrays.asList("   ", null, "\n\t")
        );

        assertThat(result.getAmenities()).isEmpty();
        assertThat(amenityRepository.count()).isEqualTo(0);
    }

    @Test
    void search_ShouldNotFilterByAmenities_WhenAmenitiesListIsEmpty() {
        hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel A",
                "BrandA",
                "Street",
                1,
                "Minsk",
                "Belarus",
                "220000",
                "+111",
                "a_empty@mail.com"
        ));
        hotelService.create(TestHotelFactory.createRequestFull(
                "Hotel B",
                "BrandB",
                "Street",
                2,
                "Moscow",
                "Russia",
                "101000",
                "+222",
                "b_empty@mail.com"
        ));

        List<HotelShortResponse> result = hotelService.search(
                null, null, null, null, List.of()
        );

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(HotelShortResponse::getName)
                .containsExactlyInAnyOrder("Hotel A", "Hotel B");
    }
}