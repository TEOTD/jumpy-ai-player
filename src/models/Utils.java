package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Utility class containing common operations like file I/O, argument parsing, and board validation.
 * <p>Provides methods for:
 * <ul>
 *   <li>Command-line argument handling</li>
 *   <li>Board state input/output operations</li>
 *   <li>Board configuration validation</li>
 * </ul>
 */
public class Utils {

    /**
     * Converts command-line arguments to a parameter map. Supports both CLI arguments and interactive input.
     * <p> For input "MiniMax board1.txt board2.txt 2", returns map with: {inputDir: "board1.txt", outputDir: "board2.txt", depth: "2"}
     *
     * @param args Command-line arguments in format: [input_file] [output_file] [depth]
     * @return Map with keys: "inputDir", "outputDir", "depth"
     * @throws IllegalArgumentException If invalid number of arguments or malformed input
     */
    public static Map<String, String> convertArgsToMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        if (args.length > 0) {
            if (args.length != 3) {
                throw new IllegalArgumentException("Invalid argument count - "
                        + "Required format: [input_file] [output_file] [depth]");
            }
            map.put("inputDir", args[0]);
            map.put("outputDir", args[1]);
            map.put("depth", args[2]);
        } else {
            // Interactive mode
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter input (e.g., MiniMax board1.txt board2.txt 2):");
            String[] tokens = scanner.nextLine().split(" ");
            scanner.close();

            if (tokens.length != 3) {
                throw new IllegalArgumentException("Invalid input format - "
                        + "Expected: [command] [input_file] [output_file] [depth]");
            }
            map.put("inputDir", tokens[0]);
            map.put("outputDir", tokens[1]);
            map.put("depth", tokens[2]);
        }
        return map;
    }

    /**
     * Writes game results to file and console. Ensures UTF-8 encoding for file output.
     *
     * @param result          Complete game result containing best move and metrics
     * @param outputBoardName Target output file path
     * @param problemType     Algorithm type for proper metric labeling
     * @throws IOException If file write operation fails
     */
    public static void output(Result result, String outputBoardName, ProblemType problemType) throws IOException {
        // Console output
        System.out.println("Output board position: " + result.getBestBoard().toString());
        System.out.println("Positions evaluated by static estimation: " + result.getPositionsEvaluated());
        System.out.println(problemType.getName() + " estimate: " + result.getEstimate());

        // File output
        Files.write(
                Paths.get(outputBoardName),
                result.getBestBoard().toString().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Reads board configuration from file using UTF-8 encoding.
     *
     * @param fileName Source file path
     * @return Raw board string (16 characters)
     * @throws IOException If file read operation fails
     */
    public static String getInputStringFromFile(String fileName) throws IOException {
        return new String(
                Files.readAllBytes(Paths.get(fileName)),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Validates board configuration against game rules.
     *
     * @param inputOneString Board string to validate
     * @throws IllegalArgumentException If any validation fails:
     *                                  <ul>
     *                                    <li>Incorrect length (16 characters)</li>
     *                                    <li>Multiple kings of same color</li>
     *                                    <li>Excessive pawn counts (>3 per color)</li>
     *                                  </ul>
     */
    public static void validateInputString(String inputOneString) {
        if (inputOneString.length() != 16) {
            throw new IllegalArgumentException("Invalid board - Must contain 16 positions");
        }
        if (countPiece(inputOneString, Piece.WHITE_KING) > 1) {
            throw new IllegalArgumentException("Invalid White pieces - Maximum 1 king allowed");
        }
        if (countPiece(inputOneString, Piece.BLACK_KING) > 1) {
            throw new IllegalArgumentException("Invalid Black pieces - Maximum 1 king allowed");
        }
        if (countPiece(inputOneString, Piece.WHITE_PAWN) > 3) {
            throw new IllegalArgumentException("Invalid White pawns - Maximum 3 allowed");
        }
        if (countPiece(inputOneString, Piece.BLACK_PAWN) > 3) {
            throw new IllegalArgumentException("Invalid Black pawns - Maximum 3 allowed");
        }
    }

    /**
     * Counts occurrences of a specific piece type in board string
     *
     * @param inputOneString Board configuration to analyze
     * @param piece          Target piece type to count
     * @return Number of occurrences (0-16)
     */
    private static int countPiece(String inputOneString, Piece piece) {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (char c : inputOneString.toCharArray()) {
            pieces.add(Piece.fromChar(c));
        }
        return countPiece(pieces, piece);
    }

    /**
     * Counts occurrences of a specific piece type on the board
     *
     * @param positions Board configuration to analyze
     * @param piece     Target piece type to count
     * @return Number of matching pieces (0-16)
     */
    public static int countPiece(ArrayList<Piece> positions, Piece piece) {
        return (int) positions.stream().filter(p -> p.equals(piece)).count();
    }
}