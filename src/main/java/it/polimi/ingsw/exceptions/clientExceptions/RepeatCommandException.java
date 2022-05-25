package it.polimi.ingsw.exceptions.clientExceptions;

public class RepeatCommandException extends ScannerException {
    @Override
    public String getMessage() {
        return "Game phase has changed";
    }
}
