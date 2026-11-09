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

public final class CourseEnrollmentService {

    private final List<EnrollmentRecord> enrollments = new ArrayList<>();

    public void enroll(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        requireNotEnrolled(student, course);
        requireFreeSeat(course);
        enrollments.add(new EnrollmentRecord(student, course, LocalDateTime.now()));
        checkRep();
    }

    public void drop(Student student, Course course) {
        requireEntity(student, "student");
        requireEntity(course, "course");
        requireEnrolled(student, course);
        enrollments.removeIf(record -> matches(record, student, course));
        checkRep();
    }

    public List<Course> getCoursesOfStudent(Student student) {
        requireEntity(student, "student");
        return enrollments.stream()
                .filter(record -> record.student().equals(student))
                .map(EnrollmentRecord::course)
                .toList();
    }

    public int getEnrolledCount(Course course) {
        requireEntity(course, "course");
        return (int) enrollments.stream()
                .filter(record -> record.course().equals(course))
                .count();
    }

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
