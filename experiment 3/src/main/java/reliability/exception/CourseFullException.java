package reliability.exception;

/**
 * Unchecked business exception: the course reached its maximum capacity.
 *
 * @author Student
 */
public class CourseFullException extends EnrollmentException {

    /**
     * Creates the exception with a diagnostic message.
     *
     * @param message description of the violated rule
     */
    public CourseFullException(String message) {
        super(message);
    }
}
