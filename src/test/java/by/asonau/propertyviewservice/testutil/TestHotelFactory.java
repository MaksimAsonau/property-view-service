package by.asonau.propertyviewservice.testutil;

import by.asonau.propertyviewservice.dto.request.AddressRequest;
import by.asonau.propertyviewservice.dto.request.ArrivalTimeRequest;
import by.asonau.propertyviewservice.dto.request.ContactsRequest;
import by.asonau.propertyviewservice.dto.request.HotelCreateRequest;

import java.time.LocalTime;
import java.util.UUID;

public final class TestHotelFactory {

    private TestHotelFactory() {
    }

    public static HotelCreateRequest createRequestFull(
            String name,
            String brand,
            String street,
            int houseNumber,
            String city,
            String country,
            String postCode,
            String phone,
            String email
    ) {
        return HotelCreateRequest.builder()
                .name(name)
                .description("Some description")
                .brand(brand)
                .address(AddressRequest.builder()
                        .houseNumber(houseNumber)
                        .street(street)
                        .city(city)
                        .country(country)
                        .postCode(postCode)
                        .build())
                .contacts(ContactsRequest.builder()
                        .phone(phone)
                        .email(email)
                        .build())
                .arrivalTime(ArrivalTimeRequest.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .build();
    }

    public static HotelCreateRequest createRequestShort(
            String name,
            String brand,
            String city,
            String country
    ) {
        return createRequestFull(
                name,
                brand,
                "Main Street",
                1,
                city,
                country,
                "000000",
                "+000",
                uniqueEmail(name)
        );
    }

    public static String validHotelJson(String name, String brand, String city, String country) {
        return """
                {
                  "name": "%s",
                  "description": "Some description",
                  "brand": "%s",
                  "address": {
                    "houseNumber": 1,
                    "street": "Main Street",
                    "city": "%s",
                    "country": "%s",
                    "postCode": "220000"
                  },
                  "contacts": {
                    "phone": "+111",
                    "email": "%s"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """.formatted(name, brand, city, country, uniqueEmail(name));
    }

    public static String invalidHotelJson_NameBlank() {
        return """
                {
                  "name": "   ",
                  "brand": "Hilton",
                  "address": {
                    "houseNumber": 1,
                    "street": "Main Street",
                    "city": "Minsk",
                    "country": "Belarus",
                    "postCode": "220000"
                  },
                  "contacts": {
                    "phone": "+111",
                    "email": "%s"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """.formatted(uniqueEmail("invalid"));
    }

    public static String uniqueEmail(String seed) {
        String safe = seed == null ? "hotel" : seed.toLowerCase().replaceAll("\\s+", "");
        return safe + "-" + UUID.randomUUID() + "@mail.com";
    }
}