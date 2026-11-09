package paradigm.functional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Pure functions of the grade book. Every statistic is a stateless stream
 * pipeline: no loop, no mutable accumulator, and no side effect.
 */
public final class GradeRank {

    /** Lower bound of rank A. */
    public static final int RANK_A_MIN = 85;

    /** Lower bound of rank B. */
    public static final int RANK_B_MIN = 70;

    /** Lower bound of rank C. */
    public static final int RANK_C_MIN = 60;

    private static final String RANK_D = "D";

    private GradeRank() {
    }

    /**
     * Converts a raw score into its rank.
     *
     * @param score score in [0, 100]
     * @return A, B, C or D
     * @throws IllegalArgumentException when the score is out of range
     */
    public static String rankOf(int score) {
        if (score < GradeRecord.MIN_SCORE || score > GradeRecord.MAX_SCORE) {
            throw new IllegalArgumentException("score out of range: " + score);
        }
        if (score >= RANK_A_MIN) {
            return "A";
        }
        if (score >= RANK_B_MIN) {
            return "B";
        }
        if (score >= RANK_C_MIN) {
            return "C";
        }
        return RANK_D;
    }

    /**
     * Tells whether a record counts as passing.
     *
     * @param record the record to test
     * @return true when the rank is not D
     */
    public static boolean isPassing(GradeRecord record) {
        return !RANK_D.equals(rankOf(record.score()));
    }

    /**
     * Task 2: the failed records, as a lazy pipeline result.
     *
     * @param records all records
     * @return immutable list of failed records
     */
    public static List<GradeRecord> failed(List<GradeRecord> records) {
        return records.stream()
                .filter(record -> !isPassing(record))
                .toList();
    }

    /**
     * Task 3: weighted average of passing scores per course.
     *
     * @param records all records
     * @return course -> weighted average, insertion ordered by first appearance
     */
    public static Map<Course, Double> weightedAverageByCourse(List<GradeRecord> records) {
        return records.stream()
                .filter(GradeRank::isPassing)
                .collect(Collectors.groupingBy(
                        GradeRecord::course,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), GradeRank::weightedAverage)));
    }

    private static double weightedAverage(List<GradeRecord> group) {
        double weightedSum = group.stream()
                .mapToDouble(record -> (double) record.score() * record.course().credit())
                .sum();
        int creditSum = group.stream()
                .mapToInt(record -> record.course().credit())
                .sum();
        return weightedSum / creditSum;
    }

    /**
     * Task 4: the student with the highest total score.
     *
     * @param records all records
     * @param students all students
     * @return the student with the largest total
     */
    public static Student bestStudent(List<GradeRecord> records, List<Student> students) {
        return students.stream()
                .max(Comparator.comparingInt(student -> totalOf(records, student)))
                .orElseThrow(() -> new IllegalArgumentException("no student in the grade book"));
    }

    /**
     * Sums the scores of one student.
     *
     * @param records all records
     * @param student the student to total
     * @return the total score
     */
    public static int totalOf(List<GradeRecord> records, Student student) {
        return records.stream()
                .filter(record -> record.student().equals(student))
                .mapToInt(GradeRecord::score)
                .sum();
    }

    /**
     * Task 5: passing records ordered by raw score, descending.
     *
     * @param records all records
     * @return immutable list, highest score first
     */
    public static List<GradeRecord> passingSortedByScoreDesc(List<GradeRecord> records) {
        return records.stream()
                .filter(GradeRank::isPassing)
                .sorted(Comparator.comparingInt(GradeRecord::score).reversed())
                .toList();
    }

    /**
     * Task 1: the rank of every record.
     *
     * @param records all records
     * @return immutable list of ranks, in input order
     */
    public static List<String> ranksOf(List<GradeRecord> records) {
        return records.stream()
                .map(GradeRecord::rank)
                .toList();
    }
}
