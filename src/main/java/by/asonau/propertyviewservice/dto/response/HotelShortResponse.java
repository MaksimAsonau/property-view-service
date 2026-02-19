package by.asonau.propertyviewservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelShortResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
