package paradigm.functional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Task C - functional style.
 *
 * <p>The grade book is an immutable list of records; every statistic is a
 * stream pipeline exposed by {@link GradeRank}. The only side effect is the
 * final print at the boundary of the program.
 */
public final class Main {

    private Main() {
    }

    /**
     * Builds the sample grade book and prints the five required reports.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("S001", "Alice"),
                new Student("S002", "Bob"),
                new Student("S003", "Charlie"),
                new Student("S004", "David"),
                new Student("S005", "Eve"));

        Course softwareConstruction = new Course("C001", "Software Construction", 3);
        Course algorithm = new Course("C002", "Algorithm", 4);

        List<GradeRecord> records = List.of(
                new GradeRecord(students.get(0), softwareConstruction, 88),
                new GradeRecord(students.get(0), algorithm, 76),
                new GradeRecord(students.get(1), softwareConstruction, 55),
                new GradeRecord(students.get(1), algorithm, 62),
                new GradeRecord(students.get(2), softwareConstruction, 92),
                new GradeRecord(students.get(2), algorithm, 81),
                new GradeRecord(students.get(3), softwareConstruction, 48),
                new GradeRecord(students.get(3), algorithm, 73),
                new GradeRecord(students.get(4), softwareConstruction, 66),
                new GradeRecord(students.get(4), algorithm, 59));

        printReport(students, records);
    }

    private static String label(GradeRecord record) {
        Student student = record.student();
        Course course = record.course();
        return student.name() + " (" + student.sid() + ") | " + course.name()
                + " (" + course.cid() + ", " + course.credit() + " credits) | " + record.score();
    }

    private static void printReport(List<Student> students, List<GradeRecord> records) {
        System.out.println("=== 1. Grade rank of every record ===");
        IntStream.range(0, records.size())
                .forEach(index -> System.out.println(label(records.get(index)) + " | "
                        + records.get(index).rank()));

        System.out.println("=== 2. Failed records (rank D) ===");
        GradeRank.failed(records)
                .forEach(record -> System.out.println(label(record) + " | " + record.rank()));

        System.out.println("=== 3. Weighted average of passing records per course ===");
        Map<Course, Double> averages = GradeRank.weightedAverageByCourse(records);
        averages.forEach((course, value) -> System.out.println(course.name() + " ("
                + course.cid() + ", " + course.credit() + " credits) | "
                + String.format(Locale.ROOT, "%.2f", value)));

        System.out.println("=== 4. Student with the highest total score ===");
        Student best = GradeRank.bestStudent(records, students);
        System.out.println(best.name() + " (" + best.sid() + ") | total "
                + GradeRank.totalOf(records, best));

        System.out.println("=== 5. Passing records sorted by raw score (desc) ===");
        GradeRank.passingSortedByScoreDesc(records)
                .forEach(record -> System.out.println(record.student().name() + " | "
                        + record.course().name() + " | " + record.score() + " | " + record.rank()));
    }
}
