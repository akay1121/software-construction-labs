package refactor.common.exception;

/**
 * Unchecked exception: null entity, blank id or invalid capacity.
 *
 * @author Student
 */
public class InvalidEntityException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the invalid entity
     */
    public InvalidEntityException(String message) {
        super(message);
    }
}
