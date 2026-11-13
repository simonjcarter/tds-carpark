package sjc.tds.carpark.exceptions;

public class ParkingNotCompletedException extends Exception {

    public ParkingNotCompletedException() {
    }

    public ParkingNotCompletedException(String message) {
        super(message);
    }
}
