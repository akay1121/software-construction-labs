package paradigm.procedural;

import java.util.Locale;

/**
 * Task A - procedural style.
 *
 * <p>All data lives in parallel arrays. There is no Student / Course /
 * GradeRecord class, and no encapsulation: every helper receives the arrays it
 * needs as parameters, which makes the parameter lists long and index coupling
 * fragile (a record index must stay aligned in three arrays).
 *
 * <p>Mutation / side effects are marked with "MUTATION" / "SIDE EFFECT" comments.
 */
public final class Main {

    private static final String[] STUDENT_IDS = {"S001", "S002", "S003", "S004", "S005"};
    private static final String[] STUDENT_NAMES = {"Alice", "Bob", "Charlie", "David", "Eve"};

    private static final String[] COURSE_IDS = {"C001", "C002"};
    private static final String[] COURSE_NAMES = {"Software Construction", "Algorithm"};
    private static final int[] COURSE_CREDITS = {3, 4};

    private static final String[] RECORD_SIDS =
            {"S001", "S001", "S002", "S002", "S003", "S003", "S004", "S004", "S005", "S005"};
    private static final String[] RECORD_CIDS =
            {"C001", "C002", "C001", "C002", "C001", "C002", "C001", "C002", "C001", "C002"};
    private static final int[] RECORD_SCORES = {88, 76, 55, 62, 92, 81, 48, 73, 66, 59};

    private static final int PASS_LINE_A = 85;
    private static final int PASS_LINE_B = 70;
    private static final int PASS_LINE_C = 60;
    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;
    private static final int NOT_FOUND = -1;

    private Main() {
    }

    /**
     * Entry point: computes and prints the five required reports.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        printReport();
    }

    private static void printReport() {
        System.out.println("=== 1. Grade rank of every record ===");
        for (int i = 0; i < RECORD_SCORES.length; i++) {
            System.out.println(studentLabel(RECORD_SIDS[i]) + " | " + courseLabel(RECORD_CIDS[i])
                    + " | " + RECORD_SCORES[i] + " | " + rankOf(RECORD_SCORES[i]));
        }

        System.out.println("=== 2. Failed records (rank D) ===");
        int[] failed = failedIndexes(RECORD_SCORES);
        for (int index : failed) {
            System.out.println(studentLabel(RECORD_SIDS[index]) + " | "
                    + courseLabel(RECORD_CIDS[index]) + " | " + RECORD_SCORES[index]
                    + " | " + rankOf(RECORD_SCORES[index]));
        }

        System.out.println("=== 3. Weighted average of passing records per course ===");
        for (int c = 0; c < COURSE_IDS.length; c++) {
            String courseId = COURSE_IDS[c];
            double weighted = weightedAverageOfCourse(
                    courseId, RECORD_CIDS, RECORD_SCORES, COURSE_IDS, COURSE_CREDITS);
            System.out.println(courseLabel(courseId) + " | "
                    + String.format(Locale.ROOT, "%.2f", weighted));
        }

        System.out.println("=== 4. Student with the highest total score ===");
        int best = bestStudentIndex(STUDENT_IDS, RECORD_SIDS, RECORD_SCORES);
        int total = totalOfStudent(STUDENT_IDS[best], RECORD_SIDS, RECORD_SCORES);
        System.out.println(STUDENT_NAMES[best] + " (" + STUDENT_IDS[best] + ") | total " + total);

        System.out.println("=== 5. Passing records sorted by raw score (desc) ===");
        int[] sorted = passingIndexesSortedDesc(RECORD_SCORES);
        for (int index : sorted) {
            System.out.println(STUDENT_NAMES[indexOfStudent(RECORD_SIDS[index])] + " | "
                    + COURSE_NAMES[indexOfCourse(RECORD_CIDS[index])] + " | "
                    + RECORD_SCORES[index] + " | " + rankOf(RECORD_SCORES[index]));
        }
    }

    private static String studentLabel(String sid) {
        return STUDENT_NAMES[indexOfStudent(sid)] + " (" + sid + ")";
    }

    private static String courseLabel(String cid) {
        return COURSE_NAMES[indexOfCourse(cid)] + " (" + cid + ", "
                + COURSE_CREDITS[indexOfCourse(cid)] + " credits)";
    }

    private static int indexOfStudent(String sid) {
        return indexOfStudent(sid, STUDENT_IDS);
    }

    /**
     * Linear search over the student id array.
     *
     * @param sid the student id
     * @param studentIds the id array to scan
     * @return the index of {@code sid}, or -1 when absent
     */
    static int indexOfStudent(String sid, String[] studentIds) {
        for (int i = 0; i < studentIds.length; i++) {
            if (studentIds[i].equals(sid)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    private static int indexOfCourse(String cid) {
        return indexOfCourse(cid, COURSE_IDS);
    }

    /**
     * Linear search over the course id array.
     *
     * @param cid the course id
     * @param courseIds the id array to scan
     * @return the index of {@code cid}, or -1 when absent
     */
    static int indexOfCourse(String cid, String[] courseIds) {
        for (int i = 0; i < courseIds.length; i++) {
            if (courseIds[i].equals(cid)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Maps a raw score to its rank with a pure static rule.
     *
     * @param score raw score in [0, 100]
     * @return A, B, C or D
     */
    static String rankOf(int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("score out of range: " + score);
        }
        if (score >= PASS_LINE_A) {
            return "A";
        }
        if (score >= PASS_LINE_B) {
            return "B";
        }
        if (score >= PASS_LINE_C) {
            return "C";
        }
        return "D";
    }

    /**
     * Collects the indexes of the failed records.
     *
     * @param scores score array
     * @return new array with the indexes whose rank is D
     */
    static int[] failedIndexes(int[] scores) {
        int count = 0;
        for (int score : scores) {
            if ("D".equals(rankOf(score))) {
                count++; // MUTATION: local accumulator
            }
        }
        int[] result = new int[count];
        int cursor = 0; // MUTATION: cursor over the result array
        for (int i = 0; i < scores.length; i++) {
            if ("D".equals(rankOf(scores[i]))) {
                result[cursor++] = i; // MUTATION
            }
        }
        return result;
    }

    /**
     * Weighted average of the passing scores of one course (weight = credit).
     *
     * @param courseId course to aggregate
     * @param recordCids course id of every record
     * @param recordScores score of every record
     * @param courseIds known course ids
     * @param courseCredits credits parallel to {@code courseIds}
     * @return the weighted average, or NaN when the course has no passing record
     */
    static double weightedAverageOfCourse(String courseId, String[] recordCids,
            int[] recordScores, String[] courseIds, int[] courseCredits) {
        int courseIndex = indexOfCourse(courseId, courseIds);
        int credit = courseCredits[courseIndex];
        double weightedSum = 0.0; // MUTATION
        int weightSum = 0; // MUTATION
        for (int i = 0; i < recordScores.length; i++) {
            if (!recordCids[i].equals(courseId)) {
                continue;
            }
            if ("D".equals(rankOf(recordScores[i]))) {
                continue;
            }
            weightedSum += recordScores[i] * credit; // MUTATION
            weightSum += credit; // MUTATION
        }
        return weightSum == 0 ? Double.NaN : weightedSum / weightSum;
    }

    /**
     * Finds the student whose sum over all courses is the largest.
     *
     * @param studentIds student ids
     * @param recordSids student id of every record
     * @param scores score of every record
     * @return the index of the best student
     */
    static int bestStudentIndex(String[] studentIds, String[] recordSids, int[] scores) {
        int bestIndex = 0; // MUTATION
        int bestTotal = Integer.MIN_VALUE; // MUTATION
        for (int s = 0; s < studentIds.length; s++) {
            int total = 0; // MUTATION
            for (int r = 0; r < recordSids.length; r++) {
                if (recordSids[r].equals(studentIds[s])) {
                    total += scores[r]; // MUTATION
                }
            }
            if (total > bestTotal) { // MUTATION
                bestTotal = total;
                bestIndex = s;
            }
        }
        return bestIndex;
    }

    /**
     * Sums the scores of one student over all courses.
     *
     * @param sid student id
     * @param recordSids student id of every record
     * @param scores score of every record
     * @return the total score
     */
    static int totalOfStudent(String sid, String[] recordSids, int[] scores) {
        int total = 0; // MUTATION
        for (int r = 0; r < recordSids.length; r++) {
            if (recordSids[r].equals(sid)) {
                total += scores[r]; // MUTATION
            }
        }
        return total;
    }

    /**
     * Returns the passing record indexes ordered by raw score, descending.
     *
     * @param scores score array
     * @return new array of record indexes
     */
    static int[] passingIndexesSortedDesc(int[] scores) {
        int count = 0;
        for (int score : scores) {
            if (!"D".equals(rankOf(score))) {
                count++; // MUTATION
            }
        }
        int[] indexes = new int[count];
        int cursor = 0; // MUTATION
        for (int i = 0; i < scores.length; i++) {
            if (!"D".equals(rankOf(scores[i]))) {
                indexes[cursor++] = i; // MUTATION
            }
        }
        for (int i = 0; i < indexes.length - 1; i++) {
            for (int j = 0; j < indexes.length - 1 - i; j++) {
                if (scores[indexes[j]] < scores[indexes[j + 1]]) {
                    int swap = indexes[j]; // MUTATION: manual swap
                    indexes[j] = indexes[j + 1];
                    indexes[j + 1] = swap;
                }
            }
        }
        return indexes;
    }
}
