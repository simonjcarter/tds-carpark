package sjc.tds.carpark.models;

import jakarta.validation.constraints.NotBlank;


public record RegistrationRequest(
        @NotBlank String vehicleReg
) {
}
