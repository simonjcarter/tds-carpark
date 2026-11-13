package sjc.tds.carpark.repositories;

import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.VehicleType;

public interface CarParkRepository {
    int getOccupiedSpaces();
    int getTotalSpaces();
    ParkedVehicle parkVehicle(String registration, VehicleType vehicleType) throws AlreadyParkedException, CarParkFullException;
    ParkedVehicle finishParking(String registration) throws NotParkedException;
}
