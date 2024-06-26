package smartphone.infraestructure.rest.error;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String msg) {super(msg);}
}
