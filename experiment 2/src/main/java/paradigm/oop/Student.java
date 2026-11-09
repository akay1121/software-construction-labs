package paradigm.oop;

import java.util.Objects;

/**
 * Immutable value object describing a student.
 *
 * <p><b>Abstraction function</b>: AF(s) = a student whose student id is
 * {@code s.sid} and whose display name is {@code s.name}.
 *
 * <p><b>Representation invariant</b>: {@code s.sid} and {@code s.name} are
 * non-null and non-blank strings.
 */
public final class Student {

    private final String sid;
    private final String name;

    /**
     * Creates a student.
     *
     * @param sid student id, must not be blank
     * @param name display name, must not be blank
     */
    public Student(String sid, String name) {
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
        Student that = (Student) other;
        return Objects.equals(sid, that.sid);
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
