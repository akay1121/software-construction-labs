package paradigm.oop;

import java.util.Objects;

/**
 * Immutable link between a {@link Student} and a {@link Course}, carrying the
 * raw score obtained in that course.
 *
 * <p><b>Abstraction function</b>: AF(g) = a grade record stating that
 * {@code g.student} obtained {@code g.rawScore} points in {@code g.course}.
 *
 * <p><b>Representation invariant</b>: {@code g.student != null},
 * {@code g.course != null} and {@code 0 <= g.rawScore <= 100}.
 */
public final class GradeRecord {

    /** Lowest accepted raw score. */
    public static final int MIN_RAW_SCORE = 0;

    /** Highest accepted raw score. */
    public static final int MAX_RAW_SCORE = 100;

    private final Student student;
    private final Course course;
    private final int rawScore;

    /**
     * Creates a grade record.
     *
     * @param student the student, must not be null
     * @param course the course, must not be null
     * @param rawScore the raw score, in [0, 100]
     */
    public GradeRecord(Student student, Course course, int rawScore) {
        this.student = student;
        this.course = course;
        this.rawScore = rawScore;
        checkRep();
    }

    /**
     * Returns the student.
     *
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the course.
     *
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Returns the raw score.
     *
     * @return the score, in [0, 100]
     */
    public int getRawScore() {
        return rawScore;
    }

    /**
     * Returns the rank derived from the raw score.
     *
     * @return A, B, C or D
     */
    public String getRank() {
        return GradeRankService.rankOf(rawScore);
    }

    /**
     * Validates the representation invariant.
     *
     * @throws AssertionError when the invariant is broken and assertions are enabled
     */
    private void checkRep() {
        assert student != null : "student must not be null";
        assert course != null : "course must not be null";
        assert rawScore >= MIN_RAW_SCORE && rawScore <= MAX_RAW_SCORE : "raw score out of range";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GradeRecord)) {
            return false;
        }
        GradeRecord that = (GradeRecord) other;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return student.getName() + " | " + course.getName() + " | " + rawScore + " | " + getRank();
    }
}
