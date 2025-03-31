import models.ProblemType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive test suite for Jumpy3 AI implementations.
 * <p>
 * Validates correctness of all algorithm variants (MiniMax, AlphaBeta, etc.) through:
 * <ul>
 *   <li>Automated input/output board comparisons</li>
 *   <li>Position evaluation count verification</li>
 *   <li>Static estimate value validation</li>
 * </ul>
 *
 * <p>Test workflow:</p>
 * <ol>
 *   <li>Creates temporary input files with test board states</li>
 *   <li>Captures program console output via stream redirection</li>
 *   <li>Compares results against expected values</li>
 *   <li>Generates detailed test report</li>
 * </ol>
 */
public class JumpyTestSuite {
    /**
     * Predefined test cases with input boards and expected results per algorithm
     */
    static final List<TestCase> TEST_CASES = Collections.singletonList(
            new TestCase("WwwwxxxxxxxxbbbB", 2, new HashMap<ProblemType, ExpectedResult>() {{
                put(ProblemType.MIN_MAX, new ExpectedResult("xwwwWxxxxxxxbbbB", 16, 0));
                put(ProblemType.MIN_MAX_BLACK, new ExpectedResult("WwwwxxxxxxxBbbbx", 16, 0));
                put(ProblemType.MIN_MAX_IMPROVED, new ExpectedResult("xwwwWxxxxxxxbbbB", 16, 12));
                put(ProblemType.ALPHA_BETA, new ExpectedResult("xwwwWxxxxxxxbbbB", 7, 0));
            }})
    );

    /**
     * Executes test suite and prints validation results
     *
     * @param args Command-line arguments (unused)
     * @throws IOException If temporary file operations fail
     */
    public static void main(String[] args) throws IOException {
        int total = 0;
        int passed = 0;
        Map<ProblemType, Integer> typeCounts = new EnumMap<>(ProblemType.class);

        // Execute all test cases for all problem types
        for (TestCase testCase : TEST_CASES) {
            for (Map.Entry<ProblemType, ExpectedResult> entry : testCase.expectedResults.entrySet()) {
                total++;
                ProblemType type = entry.getKey();
                ExpectedResult expected = entry.getValue();

                File inputFile = null;
                try {
                    // Set up test environment
                    inputFile = File.createTempFile("jumpy_input", ".txt");
                    Files.write(inputFile.toPath(),
                            testCase.inputBoard.getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.WRITE);

                    // Capture console output
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PrintStream originalOut = System.out;
                    System.setOut(new PrintStream(bos));

                    // Execute test
                    runProgram(type, inputFile.getAbsolutePath(), testCase.depth);
                    System.setOut(originalOut);

                    // Validate results
                    if (validateConsoleOutput(bos.toString(), expected, type)) {
                        passed++;
                        typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                    }
                } catch (Exception e) {
                    System.err.println("Test failed: " + type + " depth " + testCase.depth);
                    e.printStackTrace();
                } finally {
                    // Cleanup temporary files
                    if (inputFile != null) inputFile.delete();
                    File outputFile = new File("dummy_output.txt");
                    Files.deleteIfExists(outputFile.toPath());
                }
            }
        }

        // Print test summary
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

    /**
     * Executes target program using reflection
     *
     * @param problemType Algorithm variant to test
     * @param inputPath   Path to input board file
     * @param depth       Search depth for algorithm
     * @throws Exception If reflection invocation fails
     */
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

    /**
     * Validates program output against expected results
     *
     * @param consoleOutput Captured console text
     * @param expected      Expected values for validation
     * @param type          Algorithm type being tested
     * @return True if all validation checks pass
     */
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

    /**
     * Represents a test case with input board and expected outputs
     */
    static class TestCase {
        /**
         * Input board configuration (16-character string)
         */
        String inputBoard;

        /**
         * Expected results per algorithm type
         */
        Map<ProblemType, ExpectedResult> expectedResults;

        /**
         * Search depth for test case
         */
        int depth;

        public TestCase(String input, int depth, Map<ProblemType, ExpectedResult> expected) {
            this.inputBoard = input;
            this.depth = depth;
            this.expectedResults = expected;
        }
    }

    /**
     * Container for expected algorithm output values
     */
    static class ExpectedResult {
        /**
         * Expected output board configuration
         */
        String outputBoard;

        /**
         * Expected number of positions evaluated
         */
        int positionsEvaluated;

        /**
         * Expected static estimate value
         */
        int estimate;

        public ExpectedResult(String board, int positions, int estimate) {
            this.outputBoard = board;
            this.positionsEvaluated = positions;
            this.estimate = estimate;
        }
    }
}