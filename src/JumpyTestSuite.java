import models.ProblemType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class JumpyTestSuite {
    static final List<TestCase> TEST_CASES = Collections.singletonList(
            new TestCase("WwwwxxxxxxxxbbbB", 2, new HashMap<ProblemType, ExpectedResult>() {{
                put(ProblemType.MIN_MAX, new ExpectedResult("xwwwWxxxxxxxbbbB", 16, 0));
                put(ProblemType.MIN_MAX_BLACK, new ExpectedResult("WwwwxxxxxxxBbbbx", 16, 0));
                put(ProblemType.MIN_MAX_IMPROVED, new ExpectedResult("xwwwWxxxxxxxbbbB", 16, 12));
                put(ProblemType.ALPHA_BETA, new ExpectedResult("xwwwWxxxxxxxbbbB", 7, 0));
            }})
    );

    public static void main(String[] args) throws IOException {
        int total = 0;
        int passed = 0;
        Map<ProblemType, Integer> typeCounts = new EnumMap<>(ProblemType.class);

        for (TestCase testCase : TEST_CASES) {
            for (Map.Entry<ProblemType, ExpectedResult> entry : testCase.expectedResults.entrySet()) {
                total++;
                ProblemType type = entry.getKey();
                ExpectedResult expected = entry.getValue();

                File inputFile = null;
                try {
                    inputFile = File.createTempFile("jumpy_input", ".txt");
                    Files.write(inputFile.toPath(),
                            testCase.inputBoard.getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.WRITE);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PrintStream originalOut = System.out;
                    System.setOut(new PrintStream(bos));
                    runProgram(type, inputFile.getAbsolutePath(), testCase.depth);
                    System.setOut(originalOut);
                    if (validateConsoleOutput(bos.toString(), expected, type)) {
                        passed++;
                        typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                    }
                } catch (Exception e) {
                    System.err.println("Test failed: " + type + " depth " + testCase.depth);
                    e.printStackTrace();
                } finally {
                    if (inputFile != null) inputFile.delete();
                    File outputFile = new File("dummy_output.txt");
                    Files.deleteIfExists(outputFile.toPath());
                }
            }
        }

        System.out.println("\nTest Results Summary:");
        System.out.printf("Total tests: %d\nPassed: %d\nFailed: %d\n", total, passed, total - passed);
        System.out.println("\nBreakdown by problem type:");
        for (ProblemType type : ProblemType.values()) {
            if (type != ProblemType.ALL) {
                System.out.printf("%-25s: %3d/%d\n",
                        type,
                        typeCounts.getOrDefault(type, 0),
                        TEST_CASES.size());
            }
        }

        if (passed < total) System.exit(1);
    }

    private static void runProgram(ProblemType problemType, String inputPath, int depth) throws Exception {
        String[] params = {inputPath, "dummy_output.txt", String.valueOf(depth)};
        Class<?> cls;
        switch (problemType) {
            case MIN_MAX:
                cls = MiniMax.class;
                break;
            case MIN_MAX_BLACK:
                cls = MiniMaxBlack.class;
                break;
            case MIN_MAX_IMPROVED:
                cls = MiniMaxImproved.class;
                break;
            case ALPHA_BETA:
                cls = AlphaBeta.class;
                break;
            default:
                throw new IllegalArgumentException("Unknown problem type: " + problemType);
        }
        Method mainMethod = cls.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) params);
    }

    private static boolean validateConsoleOutput(String consoleOutput, ExpectedResult expected, ProblemType type) {
        try {
            String[] lines = consoleOutput.split("\r?\n");
            String actualBoard = lines[0].split(": ")[1].trim();
            boolean outputBoardSame = actualBoard.equals(expected.outputBoard);
            int actualPositions = Integer.parseInt(lines[1].split(": ")[1].trim());
            boolean positionsEqual = actualPositions == expected.positionsEvaluated;
            int actualEstimate = Integer.parseInt(lines[2].split(": ")[1].trim());
            boolean estimateEqual = actualEstimate == expected.estimate;
            return outputBoardSame && positionsEqual && estimateEqual;
        } catch (AssertionError e) {
            System.err.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error parsing output for " + type + ": " + e.getMessage());
            return false;
        }
    }

    static class TestCase {
        String inputBoard;
        Map<ProblemType, ExpectedResult> expectedResults;
        int depth;

        public TestCase(String input, int depth, Map<ProblemType, ExpectedResult> expected) {
            this.inputBoard = input;
            this.depth = depth;
            this.expectedResults = expected;
        }
    }

    static class ExpectedResult {
        String outputBoard;
        int positionsEvaluated;
        int estimate;

        public ExpectedResult(String board, int positions, int estimate) {
            this.outputBoard = board;
            this.positionsEvaluated = positions;
            this.estimate = estimate;
        }
    }
}