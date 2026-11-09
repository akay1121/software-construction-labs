package reliability.exception;

/**
 * Unchecked business exception: the student tries to drop a course he never enrolled.
 *
 * @author Student
 */
public class NotEnrolledException extends EnrollmentException {

    /**
     * Creates the exception with a diagnostic message.
     *
     * @param message description of the violated rule
     */
    public NotEnrolledException(String message) {
        super(message);
    }
}
