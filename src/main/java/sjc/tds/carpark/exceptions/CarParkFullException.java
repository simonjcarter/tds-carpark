package sjc.tds.carpark.exceptions;

public class CarParkFullException extends Exception {
    public CarParkFullException() {
    }

    public CarParkFullException(String message) {
        super(message);
    }
}
