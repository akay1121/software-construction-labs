package lab.score;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StudentScoreManager}.
 *
 * @author Student
 */
class StudentScoreManagerTest {

    private static final double DELTA = 0.0001;

    @Test
    void averageIsComputedForAllScores() {
        StudentScoreManager manager = new StudentScoreManager(new int[] {90, 80, 70});
        assertEquals(80.0, manager.average(), DELTA);
    }

    @Test
    void scoresAreSortedInDescendingOrder() {
        StudentScoreManager manager = new StudentScoreManager(new int[] {66, 98, 90, 68});
        assertArrayEquals(new int[] {98, 90, 68, 66}, manager.sortedDescending());
    }

    @Test
    void highestScoreIsReturned() {
        StudentScoreManager manager = new StudentScoreManager(new int[] {12, 99, 45});
        assertEquals(99, manager.highest());
    }

    @Test
    void singleScoreAverageEqualsItself() {
        StudentScoreManager manager = new StudentScoreManager(new int[] {77});
        assertEquals(77.0, manager.average(), DELTA);
        assertEquals(1, manager.size());
    }

    @Test
    void invalidScoreIsRejected() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new StudentScoreManager(new int[] {120}));
        assertEquals("Score out of range: 120", error.getMessage());
    }

    @Test
    void emptyScoreArrayIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new StudentScoreManager(new int[0]));
    }

    @Test
    void internalArrayIsNotExposedOrModified() {
        int[] input = {50, 60};
        StudentScoreManager manager = new StudentScoreManager(input);
        input[0] = 10;
        assertEquals(55.0, manager.average(), DELTA);
    }
}
