package sjc.tds.carpark.models;

import java.time.Instant;

public record CompletedParkingResponse(String vehicleReg, double vehicleCharge, Instant timeIn, Instant timeOut) {
}