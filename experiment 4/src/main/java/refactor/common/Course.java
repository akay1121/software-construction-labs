package refactor.common;

import refactor.common.exception.InvalidEntityException;

/**
 * Immutable course entity with an enrollment limit.
 *
 * @param cid course id, never blank
 * @param name course title, never blank
 * @param credit credit weight, strictly positive
 * @param maxCapacity enrollment limit, at least 1
 * @author Student
 */
public record Course(String cid, String name, int credit, int maxCapacity) {

    /**
     * Validates the entity invariants.
     *
     * @throws InvalidEntityException when an argument is invalid
     */
    public Course {
        if (cid == null || cid.isBlank()) {
            throw new InvalidEntityException("course id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidEntityException("course name must not be blank");
        }
        if (credit <= 0) {
            throw new InvalidEntityException("credit must be positive: " + credit);
        }
        if (maxCapacity < 1) {
            throw new InvalidEntityException("max capacity must be >= 1: " + maxCapacity);
        }
    }

    /**
     * Tells whether one more student fits into the course.
     *
     * <p>This capacity rule belongs to the course, not to the enrollment
     * service (feature envy moved into the entity).
     *
     * @param enrolledCount current number of enrolled students
     * @return true when {@code enrolledCount < maxCapacity}
     */
    public boolean hasFreeSeat(int enrolledCount) {
        if (enrolledCount < 0) {
            throw new IllegalArgumentException("enrolled count must not be negative");
        }
        return enrolledCount < maxCapacity;
    }
}
