package reliability.contract;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reliability.exception.InvalidEntityException;
import reliability.model.Course;
import reliability.model.EnrollmentRecord;
import reliability.model.Student;

/**
 * Course selection service written in <b>Design by Contract</b> style.
 *
 * <p>The caller owns the preconditions: the service documents them with
 * {@code @requires} and does not re-validate them. Class invariants are guarded
 * by {@code assert}, therefore they only fire when the JVM runs with
 * {@code -ea}. If a caller breaks a precondition the behaviour is undefined.
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
     * @requires {@code student != null && course != null}
     *           {@code && this.hasNotEnrolled(student, course)}
     *           {@code && this.getEnrolledCount(course) < course.getMaxCapacity()}
     * @modifies this
     * @effects adds a new {@link EnrollmentRecord} for the pair
     * @param student the student to enroll, must not be null
     * @param course the target course, must not be null and must have a free seat
     * @throws InvalidEntityException if the entity layer receives a null reference
     */
    public void enroll(Student student, Course course) {
        records.add(new EnrollmentRecord(student, course, LocalDateTime.now()));
        checkRep();
    }

    /**
     * Drops a course previously selected by the student.
     *
     * @requires {@code student != null && course != null}
     *           {@code && this.hasEnrolled(student, course)}
     * @modifies this
     * @effects removes the matching {@link EnrollmentRecord}
     * @param student the student, must not be null
     * @param course the course to drop, must not be null and must be enrolled
     * @throws InvalidEntityException if the entity layer receives a null reference
     */
    public void drop(Student student, Course course) {
        records.removeIf(record -> record.getStudent().equals(student)
                && record.getCourse().equals(course));
        checkRep();
    }

    /**
     * Returns every course taken by one student.
     *
     * @requires {@code student != null}
     * @param student the student to inspect, must not be null
     * @return an immutable list of the selected courses
     * @throws InvalidEntityException if {@code student} is null
     */
    public List<Course> getCoursesOfStudent(Student student) {
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
     * @requires {@code course != null}
     * @param course the course to count, must not be null
     * @return the number of enrollment records of that course
     * @throws InvalidEntityException if {@code course} is null
     */
    public int getEnrolledCount(Course course) {
        return countOf(course);
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

    /**
     * Tells whether the pair is still absent from the enrollment table.
     *
     * @param student the student, may be null
     * @param course the course, may be null
     * @return true when no matching record exists
     */
    public boolean hasNotEnrolled(Student student, Course course) {
        return !hasEnrolled(student, course);
    }

    private int countOf(Course course) {
        int count = 0;
        for (EnrollmentRecord record : records) {
            if (record.getCourse().equals(course)) {
                count++;
            }
        }
        return count;
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
