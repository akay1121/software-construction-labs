# Software Construction Labs (Experiment 1-4)

Four Java labs for the *Software Construction* course, kept together in one repository.
Each experiment was developed in its own Git repository (its own commit history is preserved
here through `git subtree`), and each folder still contains the lab handout and the
completed report.

Java 17, JUnit 5, no Maven/Gradle — plain `javac`/`java` so the labs can be graded without
a build tool.

## Layout

| Folder | Topic | Report |
|---|---|---|
| `experiment 1/` | Coding standards, Checkstyle + SpotBugs, Git branching | `Experiment 1 Report.docx` |
| `experiment 2/` | Same grade-book in procedural, OOP and functional style | `Experiment 2 Report.docx` |
| `experiment 3/` | Design by Contract vs defensive programming, JUnit 5 | `Experiment 3 Report.docx` |
| `experiment 4/` | Code-smell refactoring with tests, AI-assisted review | `Experiment 4 Report.docx` |

Every folder holds `src/main/java`, `src/test/java`, the analysis logs under `reports/`,
and the original handout `experiment N.docx`.

## Experiment 1 - coding standards

`src/main/java/lab/score/` contains `StudentScoreManager` (statistics, constants, defensive
copy, Javadoc) and `ScoreApp` (console I/O, fail-fast validation, UTF-8 `Scanner`).
The supplied handout code lives in `test.java` next to `checkstyle.original.xml`.

| Tool | Before | After |
|---|---|---|
| Checkstyle 10.12.4 | 117 warnings | 0 |
| SpotBugs 4.8.6 | 3 findings | 0 |

The 3 SpotBugs findings were real: `ICAST` (integer division in `double avg = sum / n`,
printed 83.0 instead of 83.2), `DM_DEFAULT_ENCODING`, and non-capitalised class name.
The provided `checkstyle.xml` could not even be loaded by Checkstyle 10.x; a syntax-compatible
copy is used and no rule was weakened.

## Experiment 2 - three paradigms

The same grade book implemented three times, each with its own `Main`:

- `paradigm.procedural` - six parallel arrays, static helpers, bubble sort, explicit loops.
- `paradigm.oop` - `Student`, `Course`, `GradeRecord`, `GradeRankService` ADTs, each with
  spec / abstraction function / rep invariant / `checkRep()`, all immutable.
- `paradigm.functional` - records plus a pure `GradeRank` built from streams
  (`filter`, `groupingBy(LinkedHashMap)`, `collectingAndThen`, `max`, `sorted().toList()`),
  no explicit loops.

All three print byte-identical output; `output-reference.txt` is the baseline.

## Experiment 3 - contracts vs defensive programming

`reliability.model` holds the shared immutable entities. Two services enforce the same
rules differently:

- `reliability.contract` - preconditions only documented (`@requires`), `assert` used for
  invariants; no checking inside the body.
- `reliability.defensive` - every public method validates, fail-fast, typed exceptions
  (`EnrollmentConflictException`, `CourseFullException`, `NotEnrolledException`,
  `InvalidEntityException`).

17 JUnit tests. With `-ea` all 17 pass; with assertions disabled 4 fail, three of them in the
contract version, which shows why assertions cannot replace validation in production.

## Experiment 4 - refactoring and AI review

`refactor.baseline` is the supplied 62-line service (`EnrollSvc`); `refactor.clean` is the
refactored `EnrollmentService` plus guard helpers and `Course.hasFreeSeat(int)`.

| Check | Baseline | Refactored |
|---|---|---|
| Checkstyle | 46 | 0 |
| SpotBugs | 0 | 0 (one `ASE` introduced during refactoring was fixed) |
| JUnit | 5/5 | 15/15 (5 legacy + 10 boundary) |

The `ASE` finding - `assert pairs.add(key)` inside `checkRep()` - is kept as a worked example:
an assertion with a side effect silently disables the invariant check when `-ea` is off.

## Build and run

The tools (JUnit console standalone, Checkstyle and its jars, SpotBugs) are shared and live
outside the repository, in `../.lib` of the original working copy.

```bash
# compile
javac -d out $(find src/main/java -name '*.java')

# run one of the demos
java -cp out lab.score.ScoreApp            # experiment 1
java -cp out paradigm.procedural.Main      # experiment 2 (also .oop / .functional)
java -ea -cp out reliability.contract.Main # experiment 3

# tests
javac -cp "<junit-console-jar>" -d out-test $(find src/main/java src/test/java -name '*.java')
java -ea -jar "<junit-console-jar>" --class-path "out:out-test" --scan-class-path

# static analysis (from the experiment folder)
../.lib/checkstyle.sh checkstyle.xml src/main/java
../.lib/spotbugs-4.8.6/bin/spotbugs -textui -effort:max -low out
```

## Git history

Each experiment kept a linear, message-labelled history; experiment 1 additionally shows a
`refactor` branch merged back with `--no-ff`. The original repositories' commits are merged
here as subtrees, so `git log --graph` still shows them per folder.
