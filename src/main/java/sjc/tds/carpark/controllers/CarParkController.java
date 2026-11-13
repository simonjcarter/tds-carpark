package sjc.tds.carpark.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sjc.tds.carpark.exceptions.AlreadyParkedException;
import sjc.tds.carpark.exceptions.CarParkFullException;
import sjc.tds.carpark.exceptions.NotParkedException;
import sjc.tds.carpark.exceptions.ParkingNotCompletedException;
import sjc.tds.carpark.models.domain.VehicleType;
import sjc.tds.carpark.models.dto.*;
import sjc.tds.carpark.services.CarParkService;

@RestController
@RequestMapping("/parking")
public class CarParkController {

    private final CarParkService carParkService;

    public CarParkController(CarParkService carParkService) {
        this.carParkService = carParkService;
    }

    @GetMapping
    public SpacesResponse getSpaces() {
        return carParkService.getSpaces().toSpacesResponse();
    }

    @PostMapping
    public ParkingStartResponse parkVehicle(@Valid @RequestBody VehicleRequest vehicleRequest)
            throws CarParkFullException, AlreadyParkedException {
        var parkedVehicle = carParkService.parkVehicle(vehicleRequest.vehicleReg(),
                VehicleType.fromId(vehicleRequest.vehicleType()));
        return new ParkingStartResponse(parkedVehicle.vehicleReg(), parkedVehicle.timeIn());
    }

    @PostMapping("/bill")
    public CompletedParkingResponse completeParking(@Valid @RequestBody RegistrationRequest registrationRequest)
            throws ParkingNotCompletedException, NotParkedException {
        var completedParking = carParkService.completeParking(registrationRequest.vehicleReg());
        return new CompletedParkingResponse(
                completedParking.registration(),
                completedParking.vehicleCharge(),
                completedParking.startTime(),
                completedParking.endTime());
    }
}
