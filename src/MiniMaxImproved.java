import models.Board;
import models.ImprovedEstimator;
import models.MiniMaxAlgorithm;
import models.Player;
import models.ProblemType;
import models.Result;
import models.StaticEstimator;

import java.io.IOException;
import java.util.Map;

import static models.Utils.convertArgsToMap;
import static models.Utils.getInputStringFromFile;
import static models.Utils.output;
import static models.Utils.validateInputString;

/**
 * Implements Part IV of the project: MiniMax with an enhanced static estimation function.
 * <p>
 * Uses custom heuristic rules to improve move selection compared to the basic estimator.
 * Usage: MiniMaxImproved [input_file] [output_file] [depth]
 * </p>
 */
public class MiniMaxImproved {
    /**
     * Main entry point for improved MiniMax implementation.
     *
     * @param args Three arguments: input path, output path, and search depth.
     *             Example: {"board1.txt", "improved.txt", "3"}
     */
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeMiniMaxImproved(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing MiniMaxImproved", e);  // Fixed error message
        }
    }

    /**
     * Enhanced MiniMax computation workflow:
     * <ol>
     *   <li>Load and validate board state</li>
     *   <li>Use improved static estimation function</li>
     *   <li>Compute White's optimal move with MiniMax</li>
     *   <li>Save results with improved evaluation metrics</li>
     * </ol>
     *
     * @param params Requires:
     *               <ul>
     *                 <li>"inputDir" - Source board file (e.g., "input.txt")</li>
     *                 <li>"outputDir" - Destination file for new board state</li>
     *                 <li>"depth" - Search depth (e.g., "2")</li>
     *               </ul>
     */
    private static void computeMiniMaxImproved(Map<String, String> params) throws IOException {
        // Load and validate input
        String inputString = getInputStringFromFile(params.get("inputDir"));
        validateInputString(inputString);  // Ensures valid 16-character board format

        // Initialize MiniMax with IMPROVED static estimation
        StaticEstimator staticEstimator = new ImprovedEstimator();  // Key difference from original MiniMax
        MiniMaxAlgorithm miniMaxAlgorithm = new MiniMaxAlgorithm(staticEstimator);

        // Compute best move for White using enhanced heuristic
        Board board = new Board(inputString);
        Result result = miniMaxAlgorithm.computeBestMove(
                board,
                Integer.parseInt(params.get("depth")),
                Player.WHITE
        );

        // Write output with improved evaluation metrics
        output(result, params.get("outputDir"), ProblemType.MIN_MAX_IMPROVED);
    }
}