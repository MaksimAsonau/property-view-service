package by.asonau.propertyviewservice.dto.request;

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

    @NotNull(message = "Check-in time is required")
    private LocalTime checkIn;

    private LocalTime checkOut;
}