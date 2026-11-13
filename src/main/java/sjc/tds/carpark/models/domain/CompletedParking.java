package sjc.tds.carpark.models.domain;

import sjc.tds.carpark.models.dto.CompletedParkingResponse;

import java.time.Instant;

public record CompletedParking(String registration, double vehicleCharge, Instant startTime, Instant endTime) {
    public CompletedParkingResponse toCompletedParkingResponse() {
        return new CompletedParkingResponse(registration, vehicleCharge, startTime, endTime);
    }
}