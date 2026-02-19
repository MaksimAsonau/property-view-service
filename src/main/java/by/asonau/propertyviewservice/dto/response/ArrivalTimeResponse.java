package by.asonau.propertyviewservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTimeResponse {

    private LocalTime checkIn;
    private LocalTime checkOut;
}
