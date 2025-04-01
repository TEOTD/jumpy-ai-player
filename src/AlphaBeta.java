import models.AlphaBetaAlgorithm;
import models.BasicEstimator;
import models.Board;
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
 * Implements Part II of the project: computes White's optimal move using Alpha-Beta pruning.
 * <p>
 * Produces identical estimates to MiniMax but with fewer static evaluations.
 * Usage: java -cp [output_folder] AlphaBeta [input_file] [output_file] [depth]
 * </p>
 * <p>
 * Example: java -cp bin/ AlphaBeta input.txt output.txt 2
 * </p>
 */
public class AlphaBeta {
    /**
     * Main entry point for Alpha-Beta implementation.
     *
     * @param args Three arguments: input path, output path, and search depth.
     *             Example: {"board1.txt", "board2.txt", "3"}
     */
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeAlphaBeta(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing AlphaBeta", e);
        }
    }

    /**
     * Core Alpha-Beta computation workflow:
     * <ol>
     *   <li>Load and validate board state</li>
     *   <li>Configure Alpha-Beta search with basic estimator</li>
     *   <li>Compute White's optimal move</li>
     *   <li>Save and output results</li>
     * </ol>
     *
     * @param params Requires:
     *               <ul>
     *                 <li>"inputDir" - Source board file (e.g., "input.txt")</li>
     *                 <li>"outputDir" - Destination file for new board state</li>
     *                 <li>"depth" - Search depth (e.g., "2")</li>
     *               </ul>
     */
    private static void computeAlphaBeta(Map<String, String> params) throws IOException {
        // Load and validate input
        String inputString = getInputStringFromFile(params.get("inputDir"));
        validateInputString(inputString);  // Ensures 16-character format and board compliance

        // Initialize Alpha-Beta search
        StaticEstimator staticEstimator = new BasicEstimator();
        AlphaBetaAlgorithm alphaBetaAlgorithm = new AlphaBetaAlgorithm(staticEstimator);

        // Compute best move for White (same player as MiniMax)
        Board board = new Board(inputString);
        Result result = alphaBetaAlgorithm.computeBestMove(
                board,
                Integer.parseInt(params.get("depth")),
                Player.WHITE  // Same as MiniMax but with pruning
        );

        // Write output with ALPHA_BETA problem type
        output(result, params.get("outputDir"), ProblemType.ALPHA_BETA);
    }
}