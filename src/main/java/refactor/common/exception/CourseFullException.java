package refactor.common.exception;

/**
 * Unchecked exception: the course reached its capacity.
 *
 * @author Student
 */
public class CourseFullException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the violation
     */
    public CourseFullException(String message) {
        super(message);
    }
}
