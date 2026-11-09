package paradigm.functional;

/**
 * Immutable student value.
 *
 * @param sid student id, never blank
 * @param name display name, never blank
 */
public record Student(String sid, String name) {

    /**
     * Validates the record components.
     *
     * @throws IllegalArgumentException when the id or the name is blank
     */
    public Student {
        if (sid == null || sid.isBlank()) {
            throw new IllegalArgumentException("student id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("student name must not be blank");
        }
    }
}
