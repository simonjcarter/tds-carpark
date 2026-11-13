package sjc.tds.carpark.exceptions;

public class NotParkedException extends Exception{
    public NotParkedException() {
    }

    public NotParkedException(String message) {
        super(message);
    }
}
