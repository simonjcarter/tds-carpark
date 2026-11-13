package sjc.tds.carpark.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(AlreadyParkedException.class)
    public ProblemDetail handleIAlreadyParkedException(AlreadyParkedException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problem.setTitle("Already Parked");

        return problem;
    }

    @ExceptionHandler(CarParkFullException.class)
    public ProblemDetail handleICarParkFullException(CarParkFullException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problem.setTitle("Car Park Full");

        return problem;
    }

    @ExceptionHandler(NotParkedException.class)
    public ProblemDetail handleINotParkedException(NotParkedException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problem.setTitle("Not Parked");

        return problem;
    }

    @ExceptionHandler(ParkingNotCompletedException.class)
    public ProblemDetail handleIParkingNotCompletedException(ParkingNotCompletedException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problem.setTitle("Parking Not Completed");

        return problem;
    }
}
