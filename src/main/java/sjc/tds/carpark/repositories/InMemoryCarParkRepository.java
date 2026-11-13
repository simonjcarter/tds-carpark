package sjc.tds.carpark.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.VehicleType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Repository
public class InMemoryCarParkRepository implements CarParkRepository {

    public final Integer totalSpaces;
    private final Map<String, ParkedVehicle> parkedCars = new HashMap<>();

    public InMemoryCarParkRepository(@Value("${carpark.repository.total-spaces}") Integer totalSpaces) {
        this.totalSpaces = totalSpaces;
        if (totalSpaces <= 0) {
            throw new IllegalArgumentException("totalSpaces must be greater than zero");
        }
    }

    private String normaliseRegistration(String registration) {
        return registration.trim().toUpperCase(Locale.ROOT);
    }

    @Override
    public int getOccupiedSpaces() {
        return parkedCars.size();
    }

    @Override
    public int getTotalSpaces() {
        return totalSpaces;
    }

    @Override
    public ParkedVehicle parkVehicle(String registration, VehicleType vehicleType)
            throws AlreadyParkedException, CarParkFullException {

        var normalisedRegistration = normaliseRegistration(registration);
        if (parkedCars.containsKey(normalisedRegistration)) {
            throw new AlreadyParkedException("Cannot park vehicle as it is already parked: " + registration);
        }

        if (parkedCars.size() >= totalSpaces) {
            throw new CarParkFullException("Car park is full");
        }

        var parkedVehicle = new ParkedVehicle(normalisedRegistration, Instant.now(), null, vehicleType);
        parkedCars.put(normalisedRegistration, parkedVehicle);
        return parkedVehicle;

    }

    @Override
    public ParkedVehicle finishParking(String registration) throws NotParkedException {
        var normalisedRegistration = normaliseRegistration(registration);
        if (!parkedCars.containsKey(normalisedRegistration)) {
            throw new NotParkedException("Cannot finish parking as vehicle is not parked: " + registration);
        }

        var parkedVehicle = parkedCars.remove(normalisedRegistration);
        return new ParkedVehicle(parkedVehicle.vehicleReg(),
                parkedVehicle.timeIn(),
                Instant.now(),
                parkedVehicle.vehicleType());
    }
}
