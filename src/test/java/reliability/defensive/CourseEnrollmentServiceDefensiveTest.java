package reliability.defensive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reliability.exception.CourseFullException;
import reliability.exception.EnrollmentConflictException;
import reliability.exception.InvalidEntityException;
import reliability.exception.NotEnrolledException;
import reliability.model.Course;
import reliability.model.EnrollmentRecord;
import reliability.model.Student;

/**
 * Test suite of the defensive implementation: every rule is enforced by an
 * explicit exception, with or without the {@code -ea} JVM flag.
 *
 * @author Student
 */
class CourseEnrollmentServiceDefensiveTest {

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
    void normalEnrollmentAndDrop() {
        service.enroll(alice, course);
        assertEquals(1, service.getEnrolledCount(course));
        assertTrue(service.hasEnrolled(alice, course));
        assertEquals(List.of(course), service.getCoursesOfStudent(alice));

        service.drop(alice, course);
        assertEquals(0, service.getEnrolledCount(course));
        assertFalse(service.hasEnrolled(alice, course));
    }

    @Test
    void duplicateEnrollmentIsRejected() {
        service.enroll(alice, course);
        EnrollmentConflictException error = assertThrows(EnrollmentConflictException.class,
                () -> service.enroll(alice, course));
        assertEquals("student S001 already enrolled C001", error.getMessage());
    }

    @Test
    void enrollingWhenCourseIsFullIsRejected() {
        Course small = new Course("C002", "Algorithm", 4, 1);
        service.enroll(alice, small);
        assertThrows(CourseFullException.class, () -> service.enroll(bob, small));
    }

    @Test
    void capacityOneAcceptsExactlyOneStudent() {
        Course single = new Course("C003", "Single Seat", 2, 1);
        service.enroll(alice, single);
        assertEquals(1, service.getEnrolledCount(single));
        assertThrows(CourseFullException.class, () -> service.enroll(bob, single));
    }

    @Test
    void dropWithoutEnrollmentIsRejected() {
        assertThrows(NotEnrolledException.class, () -> service.drop(alice, course));
    }

    @Test
    void nullStudentIsRejected() {
        assertThrows(InvalidEntityException.class, () -> service.enroll(null, course));
        assertThrows(InvalidEntityException.class, () -> service.getCoursesOfStudent(null));
    }

    @Test
    void nullCourseIsRejected() {
        assertThrows(InvalidEntityException.class, () -> service.enroll(alice, null));
        assertThrows(InvalidEntityException.class, () -> service.getEnrolledCount(null));
    }

    @Test
    void courseWithZeroCapacityViolatesTheEntityInvariant() {
        assertThrows(InvalidEntityException.class,
                () -> new Course("C004", "Empty Course", 3, 0));
    }

    @Test
    void manuallyBrokenInvariantIsCaughtByCheckRep() throws Exception {
        service.enroll(alice, course);
        recordsOf(service).add(new EnrollmentRecord(alice, course, LocalDateTime.now()));
        assertThrows(AssertionError.class, () -> service.checkRep());
    }

    @SuppressWarnings("unchecked")
    private static List<EnrollmentRecord> recordsOf(CourseEnrollmentService target) throws Exception {
        Field field = CourseEnrollmentService.class.getDeclaredField("records");
        field.setAccessible(true);
        return (List<EnrollmentRecord>) field.get(target);
    }
}
