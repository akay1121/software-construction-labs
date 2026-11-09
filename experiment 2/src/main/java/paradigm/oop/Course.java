package paradigm.oop;

import java.util.Objects;

/**
 * Immutable value object describing a course.
 *
 * <p><b>Abstraction function</b>: AF(c) = a course whose course id is
 * {@code c.cid}, whose title is {@code c.name} and whose credit weight is
 * {@code c.credit}.
 *
 * <p><b>Representation invariant</b>: {@code c.cid} and {@code c.name} are
 * non-null and non-blank, and {@code c.credit > 0}.
 */
public final class Course {

    private final String cid;
    private final String name;
    private final int credit;

    /**
     * Creates a course.
     *
     * @param cid course id, must not be blank
     * @param name course title, must not be blank
     * @param credit credit weight, must be positive
     */
    public Course(String cid, String name, int credit) {
        this.cid = cid;
        this.name = name;
        this.credit = credit;
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
     * Validates the representation invariant.
     *
     * @throws AssertionError when the invariant is broken and assertions are enabled
     */
    private void checkRep() {
        assert cid != null && !cid.isBlank() : "course id must not be blank";
        assert name != null && !name.isBlank() : "course name must not be blank";
        assert credit > 0 : "credit must be positive";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Course)) {
            return false;
        }
        Course that = (Course) other;
        return Objects.equals(cid, that.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid);
    }

    @Override
    public String toString() {
        return name + " (" + cid + ", " + credit + " credits)";
    }
}
