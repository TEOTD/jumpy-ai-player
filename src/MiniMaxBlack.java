import models.BasicEstimator;
import models.Board;
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
 * This class implements Part III of the project: computes Black's optimal move using MiniMax algorithm.
 * <p>
 * Usage: MiniMaxBlack [input_file] [output_file] [depth]
 * </p>
 */
public class MiniMaxBlack {
    /**
     * Main entry point. Handles command-line arguments and error reporting.
     *
     * @param args Three arguments: input file path, output file path, and search depth.
     *             Example: {"board1.txt", "board2.txt", "3"}
     */
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeMiniMaxBlack(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing MiniMaxBlack", e);
        }
    }

    /**
     * Computes Black's best move using MiniMax. Workflow:
     * <ol>
     *   <li>Load board state from input file</li>
     *   <li>Validate board format</li>
     *   <li>Run MiniMax search for Black player</li>
     *   <li>Save result to output file</li>
     * </ol>
     *
     * @param params Requires keys:
     *               <ul>
     *                 <li>"inputDir" - Source board file (e.g., "board1.txt")</li>
     *                 <li>"outputDir" - Target file for new board state</li>
     *                 <li>"depth" - MiniMax search depth (e.g., "2")</li>
     *               </ul>
     */
    private static void computeMiniMaxBlack(Map<String, String> params) throws IOException {
        // Read and validate input board
        String inputString = getInputStringFromFile(params.get("inputDir"));
        validateInputString(inputString);  // Ensures valid 16-character board format

        // Configure MiniMax with basic static estimation
        StaticEstimator staticEstimator = new BasicEstimator();
        MiniMaxAlgorithm miniMaxAlgorithm = new MiniMaxAlgorithm(staticEstimator);

        // Compute optimal move for Black
        Board board = new Board(inputString);
        Result result = miniMaxAlgorithm.computeBestMove(
                board,
                Integer.parseInt(params.get("depth")),
                Player.BLACK  // Critical difference: Black player perspective
        );

        // Write output
        output(result, params.get("outputDir"), ProblemType.MIN_MAX_BLACK);
    }
}