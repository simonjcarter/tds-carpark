package sjc.tds.carpark.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.models.domain.VehicleType;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCarParkRepositoryTest {
    private InMemoryCarParkRepository carParkRepository;
    private int totalSpaces;

    @BeforeEach
    void setup() {
        totalSpaces = 2;
        carParkRepository = new InMemoryCarParkRepository(totalSpaces);
    }

    // parkVehicle

    @Test
    void givenAlreadyParked_whenParkVehicle_thenThrowException() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        assertThrows(AlreadyParkedException.class,
                () -> carParkRepository.parkVehicle(registration, vehicleType));
    }

    @Test
    void givenCarParkFull_whenParkVehicle_thenThrowException() throws Exception {
        var registration = "ABC123";
        var registrationPrefix = "ABC";
        var vehicleType = VehicleType.SMALL;

        for (int i = 0; i < totalSpaces; i++) {
            carParkRepository.parkVehicle(registrationPrefix + i, vehicleType);
        }

        assertThrows(CarParkFullException.class,
                () -> carParkRepository.parkVehicle(registration, vehicleType));
    }

    @Test
    void givenSpaceAvailable_whenParkVehicle_thenParkVehicle() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;

        var result = carParkRepository.parkVehicle(registration, vehicleType);
        assertEquals(registration, result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertTrue(result.isParked());
        // need a way to test the time...could use Clock
    }

    // finishParking

    @Test
    void givenNotParked_whenFinishParking_thenThrowException() throws Exception {
        var registration = "ABC123";

        assertThrows(NotParkedException.class,
                () -> carParkRepository.finishParking(registration));
    }

    @Test
    void givenParked_whenFinishParking_thenReturnParkedVehicle() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        var result = carParkRepository.finishParking(registration);
        assertEquals(registration, result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertFalse(result.isParked());
    }

    @Test
    void givenVehicleParked_whenGetOccupiedSpaces_thenReturnValidCount() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;

        carParkRepository.parkVehicle(registration, vehicleType);
        assertEquals(1, carParkRepository.getOccupiedSpaces());
    }

    @Test
    void givenVehicleParked_whenGetTotalSpaces_thenReturnUnmodifiedValue() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;

        carParkRepository.parkVehicle(registration, vehicleType);
        assertEquals(totalSpaces, carParkRepository.getTotalSpaces());
    }
}