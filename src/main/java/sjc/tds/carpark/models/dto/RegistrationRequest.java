package sjc.tds.carpark.models.dto;

import jakarta.validation.constraints.NotBlank;


public record RegistrationRequest(
        @NotBlank String vehicleReg
) {
}
