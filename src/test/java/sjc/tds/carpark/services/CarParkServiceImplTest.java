package sjc.tds.carpark.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.exceptions.ParkingNotCompletedException;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.VehicleType;
import sjc.tds.carpark.repositories.CarParkRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarParkServiceImplTest {
    @Mock
    private CarParkRepository carParkRepository;
    private CarParkService carParkService;

    @BeforeEach
    void setup() {
        carParkService = new CarParkServiceImpl(carParkRepository, BigDecimal.valueOf(1), 5);
    }

    @Test
    void givenCarParkFull_whenParkVehicle_thenThrowException() throws Exception {
        var registration = "ABC123";

        when(carParkRepository.parkVehicle(
                registration, VehicleType.SMALL
        )).thenThrow(new CarParkFullException());

        assertThrows(
                CarParkFullException.class,
                () -> carParkService.parkVehicle(
                        registration, VehicleType.SMALL
                )
        );
    }

    @Test
    void givenVehicleAlreadyParked_whenParkVehicle_thenThrowException() throws Exception {
        var registration = "ABC123";

        when(carParkRepository.parkVehicle(
                registration, VehicleType.SMALL
        )).thenThrow(new AlreadyParkedException());

        assertThrows(
                AlreadyParkedException.class,
                () -> carParkService.parkVehicle(
                        registration, VehicleType.SMALL
                )
        );
    }

    @Test
    void givenVehicleNotParked_whenParkVehicle_thenReturnParkedVehicle() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var vehicleType = VehicleType.SMALL;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                null,
                vehicleType

        );
        when(carParkRepository.parkVehicle(registration, vehicleType)).thenReturn(vehicle);

        var result = carParkService.parkVehicle(registration, vehicleType);
        assertEquals(registration, result.vehicleReg());
        assertEquals(startTime, result.timeIn());
        assertNull(result.timeOut());
        assertEquals(vehicleType, result.vehicleType());

    }

    @Test
    void givenOccupiedAndTotalSpaces_whenGetSpaces_thenReturnAvailableAndOccupied() {
        when(carParkRepository.getOccupiedSpaces()).thenReturn(7);
        when(carParkRepository.getTotalSpaces()).thenReturn(20);

        var result = carParkService.getSpaces();

        assertEquals(13, result.availableSpaces());
        assertEquals(7, result.occupiedSpaces());
    }

    @Test
    void givenParkingNotCompleted_givenCompleteParking_thenThrowException() throws Exception {
        var registration = "ABC123";
        var vehicle = new ParkedVehicle(
                registration,
                Instant.now(),
                null,
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        assertThrows(
                ParkingNotCompletedException.class,
                () -> carParkService.completeParking(registration)
        );

        verify(carParkRepository).finishParking(registration);
    }

    @Test
    void givenVehicleNotParked_givenCompleteParking_thenThrowException() throws Exception {
        var registration = "ABC123";
        var vehicle = new ParkedVehicle(
                registration,
                Instant.now(),
                null,
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenThrow(new NotParkedException());

        assertThrows(
                NotParkedException.class,
                () -> carParkService.completeParking(registration)
        );

        verify(carParkRepository).finishParking(registration);
    }

    @Test
    void givenVehicleParkedForPartialMinute_whenCompleteParking_thenRoundUpMinutes() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 1;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(0.1, completedParking.vehicleCharge());
    }

    @Test
    void givenVehicleParkedForOverFiveMinutes_whenCompleteParking_thenAddAdditionalCharge() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60 * 5;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(1.5, completedParking.vehicleCharge());
    }

    @Test
    void givenVehicleParkedForUnderFiveMinutes_whenCompleteParking_thenNoAdditionalCharge() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60 * 4;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(0.4, completedParking.vehicleCharge());
    }

    @Test
    void givenVehicleParkedForFifteenMinutes_whenCompleteParking_thenMultipleAdditionalChargesAdded() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60 * 15;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(4.5, completedParking.vehicleCharge());
    }

    @Test
    void givenVehicleParked_whenCompleteParking_thenSameRegistrationReturned() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60;
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                VehicleType.SMALL
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(registration, completedParking.registration());
    }

    @Test
    void givenSmallVehicleParked_whenCompleteParking_thenUseSmallVehicleRate() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60;
        var vehicleType = VehicleType.SMALL;
        var expectedRate = vehicleType.getCharge()
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                vehicleType
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(expectedRate, completedParking.vehicleCharge());
    }

    @Test
    void givenMediumVehicleParked_whenCompleteParking_thenUseMediumVehicleRate() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60;
        var vehicleType = VehicleType.MEDIUM;
        var expectedRate = vehicleType.getCharge()
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                vehicleType
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(expectedRate, completedParking.vehicleCharge());
    }

    @Test
    void givenLargeVehicleParked_whenCompleteParking_thenUseLargeVehicleRate() throws Exception {
        var registration = "ABC123";
        var startTime = Instant.now();
        var parkTimeSeconds = 60;
        var vehicleType = VehicleType.LARGE;
        var expectedRate = vehicleType.getCharge()
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        var vehicle = new ParkedVehicle(
                registration,
                startTime,
                startTime.plusSeconds(parkTimeSeconds),
                vehicleType
        );
        when(carParkRepository.finishParking(registration)).thenReturn(vehicle);

        var completedParking = carParkService.completeParking(registration);
        assertEquals(expectedRate, completedParking.vehicleCharge());
    }
}