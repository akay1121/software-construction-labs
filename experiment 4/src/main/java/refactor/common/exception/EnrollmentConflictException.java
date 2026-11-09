package refactor.common.exception;

/**
 * Unchecked exception: the student already enrolled this course.
 *
 * @author Student
 */
public class EnrollmentConflictException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the conflict
     */
    public EnrollmentConflictException(String message) {
        super(message);
    }
}
