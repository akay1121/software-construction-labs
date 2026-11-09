package reliability.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reliability.exception.InvalidEntityException;
import reliability.model.Course;
import reliability.model.EnrollmentRecord;
import reliability.model.Student;

/**
 * Test suite of the contract (DbC) implementation.
 *
 * <p>These tests must be executed with {@code -ea}; the contract violations are
 * only observable while assertions are enabled.
 *
 * @author Student
 */
class CourseEnrollmentServiceContractTest {

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
    void duplicateEnrollmentBreaksTheInvariant() {
        service.enroll(alice, course);
        AssertionError error = assertThrows(AssertionError.class,
                () -> service.enroll(alice, course));
        assertTrue(error.getMessage().startsWith("duplicate enrollment"));
    }

    @Test
    void enrollingBeyondCapacityBreaksTheInvariant() {
        Course small = new Course("C002", "Algorithm", 4, 1);
        service.enroll(alice, small);
        AssertionError error = assertThrows(AssertionError.class,
                () -> service.enroll(bob, small));
        assertTrue(error.getMessage().startsWith("course C002 over capacity"));
    }

    @Test
    void dropWithoutEnrollmentIsSilentlyIgnored() {
        // Undefined behaviour: the caller broke @requires, the DbC version does not care.
        service.drop(alice, course);
        assertEquals(0, service.getEnrolledCount(course));
    }

    @Test
    void nullStudentIsRejectedByTheEntityLayer() {
        assertThrows(InvalidEntityException.class, () -> service.enroll(null, course));
    }

    @Test
    void nullCourseIsRejectedByTheEntityLayer() {
        assertThrows(InvalidEntityException.class, () -> service.enroll(alice, null));
    }

    @Test
    void courseWithZeroCapacityViolatesTheEntityInvariant() {
        assertThrows(InvalidEntityException.class,
                () -> new Course("C003", "Empty Course", 3, 0));
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
