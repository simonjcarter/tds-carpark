package sjc.tds.carpark.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(AlreadyParkedException.class)
    public ResponseEntity<String> handleIAlreadyParkedException(AlreadyParkedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CarParkFullException.class)
    public ResponseEntity<String> handleICarParkFullException(CarParkFullException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotParkedException.class)
    public ResponseEntity<String> handleINotParkedException(NotParkedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ParkingNotCompletedException.class)
    public ResponseEntity<String> handleIParkingNotCompletedException(ParkingNotCompletedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
