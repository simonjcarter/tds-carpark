package sjc.tds.carpark.models.dto;

import java.time.Instant;

public record ParkingStartResponse(String vehicleReg, Instant timeIn) {
}
