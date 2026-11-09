package refactor.baseline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import refactor.common.Course;
import refactor.common.Student;
import refactor.common.exception.EnrollmentConflictException;
import refactor.common.exception.NotEnrolledException;

/**
 * Safety net for the smelly baseline: documents the behaviour that the
 * refactoring must preserve.
 *
 * @author Student
 */
class EnrollSvcTest {

    private Student alice;
    private Course course;
    private EnrollSvc service;

    @BeforeEach
    void setUp() {
        alice = new Student("S001", "Alice");
        course = new Course("C001", "Software Construction", 3, 30);
        service = new EnrollSvc();
    }

    @Test
    void addStoresOneRecord() {
        service.add(alice, course);
        assertEquals(1, service.recs.size());
    }

    @Test
    void duplicateAddIsRejected() {
        service.add(alice, course);
        assertThrows(EnrollmentConflictException.class, () -> service.add(alice, course));
    }

    @Test
    void removeDeletesTheRecord() {
        service.add(alice, course);
        service.remove(alice, course);
        assertEquals(0, service.recs.size());
    }

    @Test
    void removeWithoutEnrollmentIsRejected() {
        assertThrows(NotEnrolledException.class, () -> service.remove(alice, course));
    }

    @Test
    void nullStudentIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.add(null, course));
    }
}
