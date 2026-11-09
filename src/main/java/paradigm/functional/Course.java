package paradigm.functional;

/**
 * Immutable course value.
 *
 * @param cid course id, never blank
 * @param name course title, never blank
 * @param credit credit weight, strictly positive
 */
public record Course(String cid, String name, int credit) {

    /**
     * Validates the record components.
     *
     * @throws IllegalArgumentException when the id/title is blank or the credit is not positive
     */
    public Course {
        if (cid == null || cid.isBlank()) {
            throw new IllegalArgumentException("course id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("course name must not be blank");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("credit must be positive");
        }
    }
}
