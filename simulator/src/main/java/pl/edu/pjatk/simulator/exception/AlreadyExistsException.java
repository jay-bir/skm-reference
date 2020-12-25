package pl.edu.pjatk.simulator.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String msg){
        super(msg);
    }
}
