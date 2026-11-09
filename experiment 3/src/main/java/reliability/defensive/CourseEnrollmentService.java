package reliability.defensive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reliability.exception.CourseFullException;
import reliability.exception.EnrollmentConflictException;
import reliability.exception.InvalidEntityException;
import reliability.exception.NotEnrolledException;
import reliability.model.Course;
import reliability.model.EnrollmentRecord;
import reliability.model.Student;

/**
 * Course selection service written in <b>defensive programming</b> style.
 *
 * <p>Every public method validates its arguments and the current state before
 * touching the data, and fails fast with an explicit business exception. The
 * service never relies on the caller honouring a contract, so the behaviour is
 * the same with or without {@code -ea}.
 *
 * <p><b>Class invariant</b>: for every course the current enrollment count is
 * not larger than {@code course.getMaxCapacity()}, and no
 * (student, course) pair appears twice.
 *
 * @author Student
 */
public final class CourseEnrollmentService {

    private final List<EnrollmentRecord> records = new ArrayList<>();

    /**
     * Enrolls a student into a course.
     *
     * @param student the student to enroll, must not be null
     * @param course the target course, must not be null and must have a free seat
     * @throws InvalidEntityException if the student or the course is null
     * @throws EnrollmentConflictException if the student already enrolled this course
     * @throws CourseFullException if the course reached its maximum capacity
     */
    public void enroll(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        if (hasEnrolled(student, course)) {
            throw new EnrollmentConflictException(
                    "student " + student.getSid() + " already enrolled " + course.getCid());
        }
        if (getEnrolledCount(course) >= course.getMaxCapacity()) {
            throw new CourseFullException(
                    "course " + course.getCid() + " is full: " + course.getMaxCapacity());
        }
        records.add(new EnrollmentRecord(student, course, LocalDateTime.now()));
        checkRep();
    }

    /**
     * Drops a course previously selected by the student.
     *
     * @param student the student, must not be null
     * @param course the course to drop, must not be null and must be enrolled
     * @throws InvalidEntityException if the student or the course is null
     * @throws NotEnrolledException if the student never enrolled this course
     */
    public void drop(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        if (!hasEnrolled(student, course)) {
            throw new NotEnrolledException(
                    "student " + student.getSid() + " never enrolled " + course.getCid());
        }
        records.removeIf(record -> record.getStudent().equals(student)
                && record.getCourse().equals(course));
        checkRep();
    }

    /**
     * Returns every course taken by one student.
     *
     * @param student the student to inspect, must not be null
     * @return an immutable list of the selected courses
     * @throws InvalidEntityException if {@code student} is null
     */
    public List<Course> getCoursesOfStudent(Student student) {
        requireEntity(student, "student");
        List<Course> courses = new ArrayList<>();
        for (EnrollmentRecord record : records) {
            if (record.getStudent().equals(student)) {
                courses.add(record.getCourse());
            }
        }
        return List.copyOf(courses);
    }

    /**
     * Counts the students currently enrolled in a course.
     *
     * @param course the course to count, must not be null
     * @return the number of enrollment records of that course
     * @throws InvalidEntityException if {@code course} is null
     */
    public int getEnrolledCount(Course course) {
        requireEntity(course, "course");
        int count = 0;
        for (EnrollmentRecord record : records) {
            if (record.getCourse().equals(course)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tells whether the pair is already enrolled.
     *
     * @param student the student, may be null
     * @param course the course, may be null
     * @return true when a matching record exists
     */
    public boolean hasEnrolled(Student student, Course course) {
        for (EnrollmentRecord record : records) {
            if (record.getStudent().equals(student) && record.getCourse().equals(course)) {
                return true;
            }
        }
        return false;
    }

    private void requireEntity(Object entity, String name) {
        if (entity == null) {
            throw new InvalidEntityException(name + " must not be null");
        }
        if (entity instanceof Student && ((Student) entity).getSid().isBlank()) {
            throw new InvalidEntityException("student id must not be blank");
        }
        if (entity instanceof Course && ((Course) entity).getCid().isBlank()) {
            throw new InvalidEntityException("course id must not be blank");
        }
    }

    /**
     * Checks the class invariant; only effective when assertions are enabled.
     *
     * @throws AssertionError when a duplicate pair or an over-capacity course is detected
     */
    void checkRep() {
        assert records != null : "record list must not be null";
        Set<String> pairs = new HashSet<>();
        for (EnrollmentRecord record : records) {
            assert record != null : "record must not be null";
            String key = record.getStudent().getSid() + "|" + record.getCourse().getCid();
            boolean isNewPair = pairs.add(key);
            assert isNewPair : "duplicate enrollment: " + key;
        }
        Map<String, Integer> counts = new HashMap<>();
        for (EnrollmentRecord record : records) {
            counts.merge(record.getCourse().getCid(), 1, Integer::sum);
        }
        for (EnrollmentRecord record : records) {
            Course course = record.getCourse();
            int current = counts.get(course.getCid());
            assert current <= course.getMaxCapacity()
                    : "course " + course.getCid() + " over capacity: " + current;
        }
    }
}
