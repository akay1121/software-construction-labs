package refactor.clean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import refactor.common.Course;
import refactor.common.EnrollmentRecord;
import refactor.common.Student;
import refactor.common.exception.CourseFullException;
import refactor.common.exception.EnrollmentConflictException;
import refactor.common.exception.InvalidEntityException;
import refactor.common.exception.NotEnrolledException;

/**
 * Test suite of the refactored service: the legacy scenarios of Lab 3 plus the
 * boundary cases suggested by the AI review.
 *
 * @author Student
 */
class CourseEnrollmentServiceTest {

    private Student alice;
    private Student bob;
    private Course course;
    private CourseEnrollmentService service;

    @BeforeEach
    void setUp() {
        alice = new Student("S001", "Alice");
        bob = new Student("S002", "Bob");
        course = new Course("C001", "Software Construction", 3, 2);
        service = new CourseEnrollmentService();
    }

    @Test
    void legacyScenarioEnrollAndDrop() {
        service.enroll(alice, course);
        assertEquals(1, service.getEnrolledCount(course));
        assertTrue(service.hasEnrolled(alice, course));
        assertEquals(List.of(course), service.getCoursesOfStudent(alice));

        service.drop(alice, course);
        assertEquals(0, service.getEnrolledCount(course));
        assertFalse(service.hasEnrolled(alice, course));
    }

    @Test
    void legacyScenarioDuplicateEnrollment() {
        service.enroll(alice, course);
        EnrollmentConflictException error = assertThrows(EnrollmentConflictException.class,
                () -> service.enroll(alice, course));
        assertEquals("student S001 already enrolled C001", error.getMessage());
    }

    @Test
    void legacyScenarioCourseFull() {
        Course small = new Course("C002", "Algorithm", 4, 2);
        service.enroll(alice, small);
        service.enroll(bob, small);
        Student eve = new Student("S005", "Eve");
        assertThrows(CourseFullException.class, () -> service.enroll(eve, small));
    }

    @Test
    void legacyScenarioDropWithoutEnrollment() {
        assertThrows(NotEnrolledException.class, () -> service.drop(alice, course));
    }

    @Test
    void legacyScenarioNullArguments() {
        assertThrows(InvalidEntityException.class, () -> service.enroll(null, course));
        assertThrows(InvalidEntityException.class, () -> service.enroll(alice, null));
        assertThrows(InvalidEntityException.class, () -> service.getCoursesOfStudent(null));
        assertThrows(InvalidEntityException.class, () -> service.getEnrolledCount(null));
    }

    @Test
    void legacyScenarioInvalidCourseCapacity() {
        assertThrows(InvalidEntityException.class,
                () -> new Course("C003", "Bad Course", 3, 0));
    }

    @Test
    void manuallyBrokenInvariantIsCaughtByCheckRep() throws Exception {
        service.enroll(alice, course);
        recordsOf(service).add(new EnrollmentRecord(alice, course, LocalDateTime.now()));
        assertThrows(AssertionError.class, () -> service.checkRep());
    }

    @Test
    void boundaryCapacityOneAcceptsExactlyOneStudent() {
        Course single = new Course("C004", "Single Seat", 2, 1);
        service.enroll(alice, single);
        assertEquals(1, service.getEnrolledCount(single));
        assertThrows(CourseFullException.class, () -> service.enroll(bob, single));
    }

    @Test
    void blankStudentIdIsRejectedAtConstruction() {
        assertThrows(InvalidEntityException.class, () -> new Student("  ", "Nobody"));
    }

    @Test
    void droppingOneCourseKeepsTheOtherEnrollment() {
        Course other = new Course("C005", "Operating Systems", 4, 10);
        service.enroll(alice, course);
        service.enroll(alice, other);
        service.drop(alice, course);
        assertEquals(List.of(other), service.getCoursesOfStudent(alice));
    }

    @SuppressWarnings("unchecked")
    private static List<EnrollmentRecord> recordsOf(CourseEnrollmentService target) throws Exception {
        Field field = CourseEnrollmentService.class.getDeclaredField("enrollments");
        field.setAccessible(true);
        return (List<EnrollmentRecord>) field.get(target);
    }
}
