package engine;

/**
 * If the place in hall is busy, this will throws
 */
public class BusyPlaceException extends NotChangedException {
    public BusyPlaceException(String message) {
        super(message);
    }
}
