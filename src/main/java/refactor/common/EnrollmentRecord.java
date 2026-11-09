package refactor.common;

import java.time.LocalDateTime;

import refactor.common.exception.InvalidEntityException;

/**
 * Immutable enrollment fact: a student joined a course at a timestamp.
 *
 * @param student the student, never null
 * @param course the course, never null
 * @param enrolledAt the enrollment timestamp, never null
 * @author Student
 */
public record EnrollmentRecord(Student student, Course course, LocalDateTime enrolledAt) {

    /**
     * Validates the record invariants.
     *
     * @throws InvalidEntityException when a reference is null
     */
    public EnrollmentRecord {
        if (student == null) {
            throw new InvalidEntityException("student must not be null");
        }
        if (course == null) {
            throw new InvalidEntityException("course must not be null");
        }
        if (enrolledAt == null) {
            throw new InvalidEntityException("enrollment timestamp must not be null");
        }
    }
}
