package sjc.tds.carpark.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parking")
public class CarParkController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }


}
