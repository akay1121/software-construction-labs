package paradigm.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business service of the grade book: rank conversion and the five required
 * statistics. The service is stateless, so it exposes behaviour instead of
 * storing parallel data.
 */
public final class GradeRankService {

    /** Lower bound of rank A. */
    public static final int RANK_A_MIN = 85;

    /** Lower bound of rank B. */
    public static final int RANK_B_MIN = 70;

    /** Lower bound of rank C. */
    public static final int RANK_C_MIN = 60;

    private static final String RANK_A = "A";
    private static final String RANK_B = "B";
    private static final String RANK_C = "C";
    private static final String RANK_D = "D";

    /**
     * Converts a raw score into its rank (pure rule, no state).
     *
     * @param rawScore score in [0, 100]
     * @return A, B, C or D
     * @throws IllegalArgumentException when the score is out of range
     */
    public static String rankOf(int rawScore) {
        if (rawScore < GradeRecord.MIN_RAW_SCORE || rawScore > GradeRecord.MAX_RAW_SCORE) {
            throw new IllegalArgumentException("raw score out of range: " + rawScore);
        }
        if (rawScore >= RANK_A_MIN) {
            return RANK_A;
        }
        if (rawScore >= RANK_B_MIN) {
            return RANK_B;
        }
        if (rawScore >= RANK_C_MIN) {
            return RANK_C;
        }
        return RANK_D;
    }

    /**
     * Task 2: keeps the records whose rank is D.
     *
     * @param records all grade records
     * @return a new list holding the failed records
     */
    public List<GradeRecord> failedRecords(List<GradeRecord> records) {
        List<GradeRecord> failed = new ArrayList<>();
        for (GradeRecord record : records) {
            if (RANK_D.equals(record.getRank())) {
                failed.add(record);
            }
        }
        return failed;
    }

    /**
     * Task 3: weighted average of the passing scores, grouped by course.
     *
     * @param records all grade records
     * @return a map course -> weighted average, in the order the courses appear
     */
    public Map<Course, Double> weightedAverageByCourse(List<GradeRecord> records) {
        Map<Course, Double> weightedSum = new LinkedHashMap<>();
        Map<Course, Integer> creditSum = new LinkedHashMap<>();
        for (GradeRecord record : records) {
            if (RANK_D.equals(record.getRank())) {
                continue;
            }
            Course course = record.getCourse();
            double previous = weightedSum.getOrDefault(course, 0.0);
            weightedSum.put(course, previous + (double) record.getRawScore() * course.getCredit());
            creditSum.put(course, creditSum.getOrDefault(course, 0) + course.getCredit());
        }
        Map<Course, Double> average = new LinkedHashMap<>();
        for (Map.Entry<Course, Double> entry : weightedSum.entrySet()) {
            Course course = entry.getKey();
            average.put(course, entry.getValue() / creditSum.get(course));
        }
        return average;
    }

    /**
     * Task 4: finds the student with the highest total score over all courses.
     *
     * @param records all grade records
     * @param students the known students
     * @return the student with the largest total
     */
    public Student bestStudent(List<GradeRecord> records, List<Student> students) {
        Student best = null;
        int bestTotal = Integer.MIN_VALUE;
        for (Student student : students) {
            int total = 0;
            for (GradeRecord record : records) {
                if (record.getStudent().equals(student)) {
                    total += record.getRawScore();
                }
            }
            if (total > bestTotal) {
                bestTotal = total;
                best = student;
            }
        }
        return best;
    }

    /**
     * Task 5: passing records ordered by raw score, descending.
     *
     * @param records all grade records
     * @return a new list of passing records, highest score first
     */
    public List<GradeRecord> passingSortedByScoreDesc(List<GradeRecord> records) {
        List<GradeRecord> passing = new ArrayList<>();
        for (GradeRecord record : records) {
            if (!RANK_D.equals(record.getRank())) {
                passing.add(record);
            }
        }
        passing.sort((left, right) -> Integer.compare(right.getRawScore(), left.getRawScore()));
        return passing;
    }

    /**
     * Task 1: returns the rank of every record.
     *
     * @param records all grade records
     * @return a new list holding the rank of each record, same order as input
     */
    public List<String> ranksOf(List<GradeRecord> records) {
        List<String> ranks = new ArrayList<>();
        for (GradeRecord record : records) {
            ranks.add(record.getRank());
        }
        return ranks;
    }
}
