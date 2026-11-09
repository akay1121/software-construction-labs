package lab.score;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Console front end of the student score manager.
 *
 * <p>Reads the number of students and their scores from standard input, then
 * prints the average, the descending ranking and the highest score. All input
 * is validated before any computation happens (fail fast).
 *
 * @author Student
 */
public final class ScoreApp {

    private static final String PROMPT_STUDENT_COUNT = "请输入学生人数";
    private static final String PROMPT_SCORE = "请输入第%d个学生的成绩（0-100）";
    private static final String ERROR_COUNT = "学生人数必须大于 0";
    private static final String ERROR_NUMBER = "请输入合法的整数";
    private static final String LABEL_AVERAGE = "平均分：%.2f";
    private static final String LABEL_SORTED = "成绩降序排列：";
    private static final String LABEL_HIGHEST = "最高分：%d";
    private static final String SEPARATOR = " ";

    private final Scanner scanner;
    private final PrintStream out;

    private ScoreApp(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * Starts the interactive program.
     *
     * @param args command line arguments, not used
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
            new ScoreApp(scanner, System.out).run();
        }
    }

    private void run() {
        int count = readStudentCount();
        int[] scores = readScores(count);
        StudentScoreManager manager = new StudentScoreManager(scores);
        printReport(manager);
    }

    private int readStudentCount() {
        out.println(PROMPT_STUDENT_COUNT);
        int count = readInt();
        if (count <= 0) {
            throw new IllegalArgumentException(ERROR_COUNT);
        }
        return count;
    }

    private int[] readScores(int count) {
        int[] scores = new int[count];
        for (int i = 0; i < count; i++) {
            out.printf(PROMPT_SCORE + "%n", i + 1);
            scores[i] = readInt();
        }
        return scores;
    }

    private int readInt() {
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException(ERROR_NUMBER);
        }
        return scanner.nextInt();
    }

    private void printReport(StudentScoreManager manager) {
        out.printf(LABEL_AVERAGE + "%n", manager.average());
        out.println(LABEL_SORTED);
        out.println(join(manager.sortedDescending()));
        out.printf(LABEL_HIGHEST + "%n", manager.highest());
    }

    private static String join(int[] values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(SEPARATOR);
            }
            builder.append(values[i]);
        }
        return builder.toString();
    }
}
