package by.asonau.propertyviewservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTimeRequest {

    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "Check-in time is required")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOut;
}