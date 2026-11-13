package sjc.tds.carpark.repositories;

import org.springframework.stereotype.Repository;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.VehicleType;

@Repository
public class InMemoryCarParkRepository implements CarParkRepository {
    @Override
    public int getOccupiedSpaces() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int getTotalSpaces() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ParkedVehicle parkVehicle(String registration, VehicleType vehicleType) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ParkedVehicle finishParking(String registration) {
        throw new RuntimeException("Not implemented");
    }
}
