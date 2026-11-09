package reliability.exception;

/**
 * Unchecked business exception: the student has already enrolled this course.
 *
 * @author Student
 */
public class EnrollmentConflictException extends EnrollmentException {

    /**
     * Creates the exception with a diagnostic message.
     *
     * @param message description of the violated rule
     */
    public EnrollmentConflictException(String message) {
        super(message);
    }
}
