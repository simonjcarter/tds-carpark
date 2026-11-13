package sjc.tds.carpark.models.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record VehicleRequest(
        @NotBlank String vehicleReg,
        @NotNull @Min(1) @Max(3) Integer vehicleType
) {
}
