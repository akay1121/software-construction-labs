package reliability.model;

import java.util.Objects;

import reliability.exception.InvalidEntityException;

/**
 * Immutable course entity with an enrollment limit.
 *
 * <p><b>Abstraction function</b>: AF(c) = a course identified by {@code c.cid},
 * titled {@code c.name}, worth {@code c.credit} credits, able to host at most
 * {@code c.maxCapacity} students.
 *
 * <p><b>Representation invariant</b>: {@code c.cid} and {@code c.name} are
 * non-null and non-blank, {@code c.credit > 0} and {@code c.maxCapacity >= 1}.
 *
 * @author Student
 */
public final class Course {

    private final String cid;
    private final String name;
    private final int credit;
    private final int maxCapacity;

    /**
     * Creates a course.
     *
     * @param cid course id, must not be blank
     * @param name course title, must not be blank
     * @param credit credit weight, must be positive
     * @param maxCapacity enrollment limit, must be at least 1
     * @throws InvalidEntityException when an argument violates the invariant
     */
    public Course(String cid, String name, int credit, int maxCapacity) {
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
        this.cid = cid;
        this.name = name;
        this.credit = credit;
        this.maxCapacity = maxCapacity;
        checkRep();
    }

    /**
     * Returns the course id.
     *
     * @return the id
     */
    public String getCid() {
        return cid;
    }

    /**
     * Returns the course title.
     *
     * @return the title
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the credit weight.
     *
     * @return the credit
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Returns the enrollment limit.
     *
     * @return the maximum number of enrolled students
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Validates the representation invariant.
     *
     * @throws AssertionError when the invariant is broken and assertions are enabled
     */
    private void checkRep() {
        assert cid != null && !cid.isBlank() : "course id must not be blank";
        assert name != null && !name.isBlank() : "course name must not be blank";
        assert credit > 0 : "credit must be positive";
        assert maxCapacity >= 1 : "max capacity must be >= 1";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Course)) {
            return false;
        }
        return Objects.equals(cid, ((Course) other).cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid);
    }

    @Override
    public String toString() {
        return name + " (" + cid + ", " + credit + " credits, capacity " + maxCapacity + ")";
    }
}
