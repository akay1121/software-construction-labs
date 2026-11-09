package reliability.model;

import java.util.Objects;

import reliability.exception.InvalidEntityException;

/**
 * Immutable student entity.
 *
 * <p><b>Abstraction function</b>: AF(s) = a student identified by
 * {@code s.sid} and displayed as {@code s.name}.
 *
 * <p><b>Representation invariant</b>: {@code s.sid} and {@code s.name} are
 * non-null and non-blank.
 *
 * @author Student
 */
public final class Student {

    private final String sid;
    private final String name;

    /**
     * Creates a student.
     *
     * @param sid student id, must not be blank
     * @param name display name, must not be blank
     * @throws InvalidEntityException when the id or the name is blank
     */
    public Student(String sid, String name) {
        if (sid == null || sid.isBlank()) {
            throw new InvalidEntityException("student id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidEntityException("student name must not be blank");
        }
        this.sid = sid;
        this.name = name;
        checkRep();
    }

    /**
     * Returns the student id.
     *
     * @return the id
     */
    public String getSid() {
        return sid;
    }

    /**
     * Returns the display name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Validates the representation invariant.
     *
     * @throws AssertionError when the invariant is broken and assertions are enabled
     */
    private void checkRep() {
        assert sid != null && !sid.isBlank() : "student id must not be blank";
        assert name != null && !name.isBlank() : "student name must not be blank";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Student)) {
            return false;
        }
        return Objects.equals(sid, ((Student) other).sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid);
    }

    @Override
    public String toString() {
        return name + " (" + sid + ")";
    }
}
