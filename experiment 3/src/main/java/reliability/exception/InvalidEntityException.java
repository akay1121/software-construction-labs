package reliability.exception;

/**
 * Unchecked business exception: an entity is null, has a blank id or an invalid capacity.
 *
 * @author Student
 */
public class InvalidEntityException extends EnrollmentException {

    /**
     * Creates the exception with a diagnostic message.
     *
     * @param message description of the violated rule
     */
    public InvalidEntityException(String message) {
        super(message);
    }
}
