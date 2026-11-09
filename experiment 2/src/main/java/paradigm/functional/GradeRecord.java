package paradigm.functional;

/**
 * Immutable link between a student, a course and a raw score.
 *
 * @param student the student, never null
 * @param course the course, never null
 * @param score raw score, in [0, 100]
 */
public record GradeRecord(Student student, Course course, int score) {

    /** Lowest accepted raw score. */
    public static final int MIN_SCORE = 0;

    /** Highest accepted raw score. */
    public static final int MAX_SCORE = 100;

    /**
     * Validates the record components.
     *
     * @throws IllegalArgumentException when a reference is null or the score is out of range
     */
    public GradeRecord {
        if (student == null) {
            throw new IllegalArgumentException("student must not be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("course must not be null");
        }
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("score out of range: " + score);
        }
    }

    /**
     * Returns the rank of this record (pure delegation).
     *
     * @return A, B, C or D
     */
    public String rank() {
        return GradeRank.rankOf(score);
    }
}
