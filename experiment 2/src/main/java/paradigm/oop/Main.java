package paradigm.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Task B - object-oriented style.
 *
 * <p>Data and behaviour are bundled in ADTs: {@link Student}, {@link Course},
 * {@link GradeRecord} and {@link GradeRankService}. The Main only wires the
 * objects together and prints the report.
 *
 * <p>Mutation / side effects are marked with "MUTATION" / "SIDE EFFECT" comments.
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
        List<Student> students = new ArrayList<>(); // MUTATION: list filled below
        students.add(new Student("S001", "Alice"));
        students.add(new Student("S002", "Bob"));
        students.add(new Student("S003", "Charlie"));
        students.add(new Student("S004", "David"));
        students.add(new Student("S005", "Eve"));

        Course softwareConstruction = new Course("C001", "Software Construction", 3);
        Course algorithm = new Course("C002", "Algorithm", 4);

        List<GradeRecord> records = new ArrayList<>(); // MUTATION
        records.add(new GradeRecord(students.get(0), softwareConstruction, 88));
        records.add(new GradeRecord(students.get(0), algorithm, 76));
        records.add(new GradeRecord(students.get(1), softwareConstruction, 55));
        records.add(new GradeRecord(students.get(1), algorithm, 62));
        records.add(new GradeRecord(students.get(2), softwareConstruction, 92));
        records.add(new GradeRecord(students.get(2), algorithm, 81));
        records.add(new GradeRecord(students.get(3), softwareConstruction, 48));
        records.add(new GradeRecord(students.get(3), algorithm, 73));
        records.add(new GradeRecord(students.get(4), softwareConstruction, 66));
        records.add(new GradeRecord(students.get(4), algorithm, 59));

        printReport(students, records);
    }

    private static void printReport(List<Student> students, List<GradeRecord> records) {
        GradeRankService service = new GradeRankService();

        System.out.println("=== 1. Grade rank of every record ===");
        for (GradeRecord record : records) {
            System.out.println(label(record) + " | " + record.getRank());
        }

        System.out.println("=== 2. Failed records (rank D) ===");
        for (GradeRecord record : service.failedRecords(records)) {
            System.out.println(label(record) + " | " + record.getRank());
        }

        System.out.println("=== 3. Weighted average of passing records per course ===");
        for (Map.Entry<Course, Double> entry : service.weightedAverageByCourse(records).entrySet()) {
            System.out.println(entry.getKey() + " | "
                    + String.format(Locale.ROOT, "%.2f", entry.getValue()));
        }

        System.out.println("=== 4. Student with the highest total score ===");
        Student best = service.bestStudent(records, students);
        System.out.println(best.getName() + " (" + best.getSid() + ") | total "
                + totalOf(best, records));

        System.out.println("=== 5. Passing records sorted by raw score (desc) ===");
        for (GradeRecord record : service.passingSortedByScoreDesc(records)) {
            System.out.println(record); // SIDE EFFECT: printing at the boundary only
        }
    }

    private static String label(GradeRecord record) {
        Student student = record.getStudent();
        Course course = record.getCourse();
        return student.getName() + " (" + student.getSid() + ") | " + course.getName()
                + " (" + course.getCid() + ", " + course.getCredit() + " credits) | "
                + record.getRawScore();
    }

    private static int totalOf(Student student, List<GradeRecord> records) {
        int total = 0; // MUTATION
        for (GradeRecord record : records) {
            if (record.getStudent().equals(student)) {
                total += record.getRawScore(); // MUTATION
            }
        }
        return total;
    }
}
