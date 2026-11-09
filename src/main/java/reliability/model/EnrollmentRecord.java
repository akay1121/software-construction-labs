package reliability.model;

import java.time.LocalDateTime;

import reliability.exception.InvalidEntityException;

/**
 * Immutable record of one enrollment: a student joined a course at a given time.
 *
 * <p><b>Abstraction function</b>: AF(r) = the fact that {@code r.student}
 * enrolled in {@code r.course} at {@code r.enrolledAt}.
 *
 * <p><b>Representation invariant</b>: {@code r.student} and {@code r.course}
 * are non-null and {@code r.enrolledAt} is non-null.
 *
 * @author Student
 */
public final class EnrollmentRecord {

    private final Student student;
    private final Course course;
    private final LocalDateTime enrolledAt;

    /**
     * Creates an enrollment record.
     *
     * @param student the enrolled student, must not be null
     * @param course the selected course, must not be null
     * @param enrolledAt the enrollment timestamp, must not be null
     * @throws InvalidEntityException when any reference is null
     */
    public EnrollmentRecord(Student student, Course course, LocalDateTime enrolledAt) {
        if (student == null) {
            throw new InvalidEntityException("student must not be null");
        }
        if (course == null) {
            throw new InvalidEntityException("course must not be null");
        }
        if (enrolledAt == null) {
            throw new InvalidEntityException("enrollment timestamp must not be null");
        }
        this.student = student;
        this.course = course;
        this.enrolledAt = enrolledAt;
        checkRep();
    }

    /**
     * Returns the student.
     *
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the course.
     *
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Returns the enrollment timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    /**
     * Validates the representation invariant.
     *
     * @throws AssertionError when the invariant is broken and assertions are enabled
     */
    private void checkRep() {
        assert student != null : "student must not be null";
        assert course != null : "course must not be null";
        assert enrolledAt != null : "enrollment timestamp must not be null";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EnrollmentRecord)) {
            return false;
        }
        EnrollmentRecord that = (EnrollmentRecord) other;
        return student.equals(that.student) && course.equals(that.course);
    }

    @Override
    public int hashCode() {
        return student.hashCode() * 31 + course.hashCode();
    }

    @Override
    public String toString() {
        return student.getName() + " -> " + course.getName() + " @" + enrolledAt;
    }
}
