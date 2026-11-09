package refactor.clean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import refactor.common.Course;
import refactor.common.EnrollmentRecord;
import refactor.common.Student;
import refactor.common.exception.CourseFullException;
import refactor.common.exception.EnrollmentConflictException;
import refactor.common.exception.InvalidEntityException;
import refactor.common.exception.NotEnrolledException;

/**
 * Course enrollment service after the refactoring lab.
 *
 * <p>Compared with {@code refactor.baseline.EnrollSvc}, this class has no
 * duplicated guard clause, no magic number, no console output, meaningful
 * names, extracted helpers and complete contract documentation. The observable
 * behaviour (same exceptions for the same situations, same enrollment set)
 * is unchanged.
 *
 * <p><b>Class invariant</b>: no (student, course) pair appears twice and the
 * enrolled count of every course never exceeds {@code course.maxCapacity()}.
 *
 * @author Student
 */
public final class CourseEnrollmentService {

    private final List<EnrollmentRecord> enrollments = new ArrayList<>();

    /**
     * Enrolls a student into a course.
     *
     * @param student the student to enroll, must not be null
     * @param course the target course, must not be null
     * @throws InvalidEntityException if {@code student} or {@code course} is null
     * @throws EnrollmentConflictException if the student already enrolled this course
     * @throws CourseFullException if the course has no free seat left
     * @effects appends a new {@link EnrollmentRecord} for the pair
     * @modifies this
     */
    public void enroll(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        requireNotEnrolled(student, course);
        requireFreeSeat(course);
        enrollments.add(new EnrollmentRecord(student, course, LocalDateTime.now()));
        checkRep();
    }

    /**
     * Drops a course previously selected by the student.
     *
     * @param student the student, must not be null
     * @param course the course to drop, must not be null
     * @throws InvalidEntityException if {@code student} or {@code course} is null
     * @throws NotEnrolledException if the pair is not part of the enrollment set
     * @effects removes the matching {@link EnrollmentRecord}
     * @modifies this
     */
    public void drop(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        requireEnrolled(student, course);
        enrollments.removeIf(record -> matches(record, student, course));
        checkRep();
    }

    /**
     * Returns every course taken by one student.
     *
     * @param student the student, must not be null
     * @return an immutable list of the selected courses
     * @throws InvalidEntityException if {@code student} is null
     */
    public List<Course> getCoursesOfStudent(Student student) {
        requireEntity(student, "student");
        return enrollments.stream()
                .filter(record -> record.student().equals(student))
                .map(EnrollmentRecord::course)
                .toList();
    }

    /**
     * Counts the students currently enrolled in a course.
     *
     * @param course the course, must not be null
     * @return the number of enrollment records of that course
     * @throws InvalidEntityException if {@code course} is null
     */
    public int getEnrolledCount(Course course) {
        requireEntity(course, "course");
        return (int) enrollments.stream()
                .filter(record -> record.course().equals(course))
                .count();
    }

    /**
     * Tells whether the pair is already enrolled.
     *
     * @param student the student, must not be null
     * @param course the course, must not be null
     * @return true when a matching record exists
     * @throws InvalidEntityException if an argument is null
     */
    public boolean hasEnrolled(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        return enrollments.stream().anyMatch(record -> matches(record, student, course));
    }

    private static boolean matches(EnrollmentRecord record, Student student, Course course) {
        return record.student().equals(student) && record.course().equals(course);
    }

    private static void requireEntity(Object entity, String name) {
        if (entity == null) {
            throw new InvalidEntityException(name + " must not be null");
        }
    }

    private void requireNotEnrolled(Student student, Course course) {
        if (hasEnrolledIgnoringNull(student, course)) {
            throw new EnrollmentConflictException(
                    "student " + student.sid() + " already enrolled " + course.cid());
        }
    }

    private void requireEnrolled(Student student, Course course) {
        if (!hasEnrolledIgnoringNull(student, course)) {
            throw new NotEnrolledException(
                    "student " + student.sid() + " never enrolled " + course.cid());
        }
    }

    private boolean hasEnrolledIgnoringNull(Student student, Course course) {
        return enrollments.stream().anyMatch(record -> matches(record, student, course));
    }

    private void requireFreeSeat(Course course) {
        int current = enrollments.stream()
                .filter(record -> record.course().equals(course))
                .mapToInt(record -> 1)
                .sum();
        if (!course.hasFreeSeat(current)) {
            throw new CourseFullException("course " + course.cid() + " is full: "
                    + course.maxCapacity());
        }
    }

    /**
     * Checks the class invariant; effective only when assertions are enabled.
     *
     * @throws AssertionError when a duplicate pair or an over-capacity course is detected
     */
    void checkRep() {
        Set<String> pairs = new HashSet<>();
        for (EnrollmentRecord record : enrollments) {
            String key = record.student().sid() + "|" + record.course().cid();
            boolean isNewPair = pairs.add(key);
            assert isNewPair : "duplicate enrollment: " + key;
        }
        for (EnrollmentRecord record : enrollments) {
            Course course = record.course();
            long current = enrollments.stream()
                    .filter(other -> other.course().equals(course))
                    .count();
            assert current <= course.maxCapacity()
                    : "course " + course.cid() + " over capacity: " + current;
        }
    }
}
