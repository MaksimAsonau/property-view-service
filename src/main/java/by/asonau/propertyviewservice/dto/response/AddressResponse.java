package by.asonau.propertyviewservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
