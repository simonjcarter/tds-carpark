package sjc.tds.carpark.models;

import java.time.Instant;

public record ParkingStartResponse(String vehicleReg, Instant timeIn) {
}
