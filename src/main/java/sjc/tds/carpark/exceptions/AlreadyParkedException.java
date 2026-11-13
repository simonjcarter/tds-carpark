package sjc.tds.carpark.exceptions;

public class AlreadyParkedException extends Exception {
    public AlreadyParkedException() {
    }

    public AlreadyParkedException(String message) {
        super(message);
    }
}
