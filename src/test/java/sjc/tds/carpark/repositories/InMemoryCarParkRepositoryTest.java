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

    @Test
    void givenNegativeTotalSpaces_whenCreateRepository_thenThrowException() {
        var totalSpaces = -1;

        assertThrows(IllegalArgumentException.class,
                () -> new InMemoryCarParkRepository(totalSpaces));
    }

    @Test
    void givenZeroTotalSpaces_whenCreateRepository_thenThrowException() {
        var totalSpaces = 0;

        assertThrows(IllegalArgumentException.class,
                () -> new InMemoryCarParkRepository(totalSpaces));
    }

    @Test
    void givenOneTotalSpace_whenCreateRepository_thenReturnTotalSpaces() {
        var totalSpaces = 1;

        var result = new InMemoryCarParkRepository(totalSpaces);
        assertEquals(totalSpaces, result.getTotalSpaces());
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
        assertEquals("ABC123", result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertTrue(result.isParked());
        // need a way to test the time...could use Clock
    }

    @Test
    void givenAlreadyParkedWithDifferentCase_whenParkVehicle_thenThrowException() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        assertThrows(AlreadyParkedException.class,
                () -> carParkRepository.parkVehicle("abc123", vehicleType));
        assertEquals(1, carParkRepository.getOccupiedSpaces());
    }

    @Test
    void givenAlreadyParkedWithSurroundingWhitespace_whenParkVehicle_thenThrowException() throws Exception {
        var registration = " ABC123 ";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        assertThrows(AlreadyParkedException.class,
                () -> carParkRepository.parkVehicle("ABC123", vehicleType));
        assertEquals(1, carParkRepository.getOccupiedSpaces());
    }

    @Test
    void givenMixedCaseRegistrationWithSurroundingWhitespace_whenParkVehicle_thenReturnNormalisedRegistration() throws Exception {
        var registration = " AbC123 ";
        var vehicleType = VehicleType.SMALL;

        var result = carParkRepository.parkVehicle(registration, vehicleType);
        assertEquals("ABC123", result.vehicleReg());
    }

    // finishParking

    @Test
    void givenNotParked_whenFinishParking_thenThrowException() {
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
        assertEquals("ABC123", result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertFalse(result.isParked());
    }

    @Test
    void givenParkedWithDifferentCase_whenFinishParking_thenReturnParkedVehicleAndFreeSpace() throws Exception {
        var registration = "abc123";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        var result = carParkRepository.finishParking("ABC123");
        assertEquals("ABC123", result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertFalse(result.isParked());
        assertEquals(0, carParkRepository.getOccupiedSpaces());
    }

    @Test
    void givenParkedWithSurroundingWhitespace_whenFinishParking_thenReturnParkedVehicleAndFreeSpace() throws Exception {
        var registration = "ABC123";
        var vehicleType = VehicleType.SMALL;
        carParkRepository.parkVehicle(registration, vehicleType);

        var result = carParkRepository.finishParking(" ABC123 ");
        assertEquals("ABC123", result.vehicleReg());
        assertEquals(vehicleType, result.vehicleType());
        assertFalse(result.isParked());
        assertEquals(0, carParkRepository.getOccupiedSpaces());
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