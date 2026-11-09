package refactor.common;

import refactor.common.exception.InvalidEntityException;

/**
 * Immutable student entity.
 *
 * @param sid student id, never blank
 * @param name display name, never blank
 * @author Student
 */
public record Student(String sid, String name) {

    /**
     * Validates the entity invariants.
     *
     * @throws InvalidEntityException when the id or the name is blank
     */
    public Student {
        if (sid == null || sid.isBlank()) {
            throw new InvalidEntityException("student id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidEntityException("student name must not be blank");
        }
    }
}
