package sjc.tds.carpark.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sjc.tds.carpark.models.*;
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
        throw new RuntimeException("Not implemented yet");
    }

    @PostMapping
    public ParkingStartResponse parkVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        throw new RuntimeException("Not implemented yet");
    }

    @PostMapping("/bill")
    public CompletedParkingResponse completeParking(@Valid @RequestBody RegistrationRequest registrationRequest)  {
        throw new RuntimeException("Not implemented yet");
    }


}
