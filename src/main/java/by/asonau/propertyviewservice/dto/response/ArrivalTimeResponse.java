package by.asonau.propertyviewservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOut;
}
