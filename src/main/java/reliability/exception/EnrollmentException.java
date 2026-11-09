package reliability.exception;

/**
 * Signals a business rule violation inside the course selection system.
 *
 * <p>All business exceptions of this lab are unchecked: they represent
 * programming errors or rule violations that the caller cannot reasonably
 * recover from, and they are preferred over magic return values.
 *
 * @author Student
 */
public abstract class EnrollmentException extends RuntimeException {

    /**
     * Creates the exception with a diagnostic message.
     *
     * @param message description of the violated rule
     */
    protected EnrollmentException(String message) {
        super(message);
    }
}
