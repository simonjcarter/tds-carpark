package sjc.tds.carpark.services;

import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.exceptions.ParkingNotCompletedException;
import sjc.tds.carpark.models.domain.CompletedParking;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.Spaces;
import sjc.tds.carpark.models.domain.VehicleType;

/**
 * Manages parking availability, vehicle entry and billing on exit.
 * Vehicle registrations are trimmed and converted to uppercase for lookup and responses.
 */
public interface CarParkService {

    /**
     * Gets the current number of available and occupied spaces.
     *
     * @return the available and occupied space counts
     */
    Spaces getSpaces();

    /**
     * Parks a vehicle if a space is available and records its entry time.
     *
     * @param registration the vehicle registration
     * @param vehicleType the vehicle type used to determine its parking rate
     * @return the parked vehicle with its normalised registration and entry time
     * @throws AlreadyParkedException if the vehicle is already parked
     * @throws CarParkFullException if no spaces are available
     */
    ParkedVehicle parkVehicle(String registration, VehicleType vehicleType) throws AlreadyParkedException, CarParkFullException;

    /**
     * Ends a vehicle's parking session, frees its space and calculates its final charge.
     * This operation removes the vehicle from the car park
     *
     * @param registration the registration of the parked vehicle
     * @return the normalised registration, final charge, entry time and exit time
     * @throws ParkingNotCompletedException if the parking record has no exit time
     * @throws NotParkedException if the vehicle is not parked
     */
    CompletedParking completeParking(String registration) throws ParkingNotCompletedException, NotParkedException;
}
