package sjc.tds.carpark.services;

import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.exceptions.ParkingNotCompletedException;
import sjc.tds.carpark.models.domain.CompletedParking;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.Spaces;
import sjc.tds.carpark.models.domain.VehicleType;

public interface CarParkService {
    Spaces getSpaces();
    ParkedVehicle parkVehicle(String registration, VehicleType vehicleType) throws AlreadyParkedException, CarParkFullException;
    CompletedParking completeParking(String registration) throws ParkingNotCompletedException, NotParkedException;
}
