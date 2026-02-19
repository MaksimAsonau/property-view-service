package by.asonau.propertyviewservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDetailsResponse {

    private Long id;
    private String name;
    private String description;
    private String brand;

    private AddressResponse address;
    private ContactsResponse contacts;
    private ArrivalTimeResponse arrivalTime;

    private List<String> amenities;
}
