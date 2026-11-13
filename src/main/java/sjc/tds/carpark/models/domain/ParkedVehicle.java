package sjc.tds.carpark.models.domain;

import java.time.Instant;

public record ParkedVehicle(String vehicleReg, Instant timeIn, Instant timeOut, VehicleType vehicleType) {
    public boolean isParked() {
        return timeOut == null;
    }
}
