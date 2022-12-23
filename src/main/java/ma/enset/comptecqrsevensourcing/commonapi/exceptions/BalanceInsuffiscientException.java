package ma.enset.comptecqrsevensourcing.commonapi.exceptions;

public class BalanceInsuffiscientException extends RuntimeException {
    public BalanceInsuffiscientException(String message) {
        super(message);
    }
}
