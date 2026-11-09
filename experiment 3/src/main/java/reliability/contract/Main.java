package reliability.contract;

import reliability.model.Course;
import reliability.model.Student;

/**
 * Demo of the contract version: a full course is only detected when the JVM
 * runs with {@code -ea}, otherwise the invariant is silently broken.
 *
 * @author Student
 */
public final class Main {

    private Main() {
    }

    /**
     * Runs the demo scenario.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        System.out.println("assertions enabled: "
                + CourseEnrollmentService.class.desiredAssertionStatus());

        Student alice = new Student("S001", "Alice");
        Student bob = new Student("S002", "Bob");
        Course course = new Course("C001", "Software Construction", 3, 1);
        CourseEnrollmentService service = new CourseEnrollmentService();

        service.enroll(alice, course);
        System.out.println("after Alice: " + service.getEnrolledCount(course) + " / "
                + course.getMaxCapacity());

        try {
            service.enroll(bob, course);
            System.out.println("after Bob: " + service.getEnrolledCount(course) + " / "
                    + course.getMaxCapacity() + " (invariant broken, no assertion!)");
        } catch (AssertionError error) {
            System.out.println("AssertionError: " + error.getMessage());
            System.out.println("after Bob: " + service.getEnrolledCount(course) + " / "
                    + course.getMaxCapacity());
        }

        System.out.println("courses of Alice: " + service.getCoursesOfStudent(alice));
    }
}
