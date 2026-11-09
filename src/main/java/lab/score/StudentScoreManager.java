package lab.score;

import java.util.Arrays;

/**
 * Computes statistics for an immutable set of student scores.
 *
 * <p>The manager keeps a defensive copy of the given scores and exposes the
 * average, a descending ranking and the highest score. Every stored score must
 * lie in the closed interval [{@value #MIN_SCORE}, {@value #MAX_SCORE}].
 *
 * @author Student
 */
public final class StudentScoreManager {

    /** Lowest score accepted by the manager. */
    public static final int MIN_SCORE = 0;

    /** Highest score accepted by the manager. */
    public static final int MAX_SCORE = 100;

    /** Defensive copy of the scores, never exposed directly. */
    private final int[] scores;

    /**
     * Creates a manager for the given scores.
     *
     * @param scores raw scores; must not be null or empty and every element
     *               must be a valid score
     * @throws IllegalArgumentException if the array is null, empty or contains
     *                                  a value outside the allowed range
     */
    public StudentScoreManager(int[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("Scores must not be null or empty");
        }
        for (int score : scores) {
            if (!isValidScore(score)) {
                throw new IllegalArgumentException("Score out of range: " + score);
            }
        }
        this.scores = Arrays.copyOf(scores, scores.length);
    }

    /**
     * Tells whether a score is inside the accepted range.
     *
     * @param score the value to check
     * @return {@code true} when {@code MIN_SCORE <= score <= MAX_SCORE}
     */
    public static boolean isValidScore(int score) {
        return score >= MIN_SCORE && score <= MAX_SCORE;
    }

    /**
     * Returns the arithmetic mean of all stored scores.
     *
     * @return the average score
     */
    public double average() {
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return (double) sum / scores.length;
    }

    /**
     * Returns all scores sorted from highest to lowest.
     *
     * @return a new array, the internal state is not modified
     */
    public int[] sortedDescending() {
        int[] sorted = Arrays.copyOf(scores, scores.length);
        Arrays.sort(sorted);
        reverse(sorted);
        return sorted;
    }

    /**
     * Returns the highest stored score.
     *
     * @return the maximum score
     */
    public int highest() {
        return sortedDescending()[0];
    }

    /**
     * Returns the number of stored scores.
     *
     * @return the size of the score set
     */
    public int size() {
        return scores.length;
    }

    private static void reverse(int[] values) {
        for (int left = 0, right = values.length - 1; left < right; left++, right--) {
            int tmp = values[left];
            values[left] = values[right];
            values[right] = tmp;
        }
    }
}
