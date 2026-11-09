package refactor.common.exception;

/**
 * Unchecked exception: the student never enrolled this course.
 *
 * @author Student
 */
public class NotEnrolledException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the violation
     */
    public NotEnrolledException(String message) {
        super(message);
    }
}
