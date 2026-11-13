package sjc.tds.carpark.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.exceptions.ParkingNotCompletedException;
import sjc.tds.carpark.models.domain.CompletedParking;
import sjc.tds.carpark.models.domain.ParkedVehicle;
import sjc.tds.carpark.models.domain.Spaces;
import sjc.tds.carpark.models.domain.VehicleType;
import sjc.tds.carpark.repositories.CarParkRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Service
public class CarParkServiceImpl implements CarParkService {
    private final CarParkRepository carParkRepository;
    private final BigDecimal additionalChargeRate;
    private final int additionalChargeMinutes;

    public CarParkServiceImpl(CarParkRepository carParkRepository,
                              @Value("${carpark.service.additionalChargeRate}") BigDecimal additionalChargeRate,
                              @Value("${carpark.service.additionalChargeMinutes}") int additionalChargeMinutes) {
        this.carParkRepository = carParkRepository;
        this.additionalChargeRate = additionalChargeRate;
        this.additionalChargeMinutes = additionalChargeMinutes;

        if (additionalChargeMinutes < 0) {
            throw new IllegalArgumentException("additionalChargeMinutes must be a positive integer");
        }

        if (additionalChargeRate == null || additionalChargeRate.signum() < 0) {
            throw new IllegalArgumentException("additionalChargeRate must be a positive number");
        }
    }

    @Override
    public Spaces getSpaces() {
        var occupiedSpaces = carParkRepository.getOccupiedSpaces();
        var totalSpaces = carParkRepository.getTotalSpaces();

        return new Spaces(totalSpaces - occupiedSpaces, occupiedSpaces);
    }

    private double calculateBill(ParkedVehicle parkedVehicle) throws ParkingNotCompletedException {

        if (parkedVehicle.isParked()) {
            throw new ParkingNotCompletedException("Failed to calculate bill, vehicle is still parked " + parkedVehicle.vehicleReg());
        }

        var startTime = parkedVehicle.timeIn();
        var endTime = parkedVehicle.timeOut();
        var totalTime = Duration.between(startTime, endTime);

        // toMinutes rounds down, charge for partial minute
        var minutes = BigDecimal.valueOf((totalTime.toSeconds() + 59) / 60);


        var parkingCharge = minutes.multiply(
                parkedVehicle.vehicleType().getCharge()
        );

        var additionalMinutes =
                additionalChargeMinutes == 0 ? BigDecimal.ZERO :
                        minutes.divide(BigDecimal.valueOf(additionalChargeMinutes), 0, RoundingMode.FLOOR);
        var additionalCharge = additionalMinutes.multiply(additionalChargeRate);
        var totalCharge = parkingCharge.add(additionalCharge);

        return totalCharge
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public ParkedVehicle parkVehicle(String registration, VehicleType vehicleType) throws AlreadyParkedException, CarParkFullException {
        return carParkRepository.parkVehicle(registration, vehicleType);
    }

    @Override
    public CompletedParking completeParking(String registration) throws NotParkedException, ParkingNotCompletedException {
        var parkedVehicle = carParkRepository.finishParking(registration);
        var vehicleCharge = calculateBill(parkedVehicle);
        return new CompletedParking(parkedVehicle.vehicleReg(), vehicleCharge, parkedVehicle.timeIn(), parkedVehicle.timeOut());
    }
}
