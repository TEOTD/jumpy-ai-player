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
 * This class implements the first part of the project: using the MiniMax algorithm to determine
 * the best move for the White player in the Jumpy3 game. It reads an input board state, computes the optimal
 * move up to a specified depth, and writes the resulting board position to an output file.
 * The evaluation metrics is printed on the console.
 * <p>
 * Usage: java -cp [output_folder] MiniMax [input_file] [output_file] [depth]
 * </p>
 * <p>
 * Example: java -cp bin/ MiniMax input.txt output.txt 2
 * </p>
 */
public class MiniMax {
    /**
     * @param args Command-line arguments: input file, output file, and search depth.
     *             Example: {"board1.txt", "board2.txt", "2"}
     */
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeMiniMax(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing MiniMax", e);
        }
    }

    /**
     * Computes the best move for White using the MiniMax algorithm. This method:
     * <ol>
     *   <li>Reads the input board state from the specified file</li>
     *   <li>Validate the input string obtained</li>
     *   <li>Initializes the static estimation function</li>
     *   <li>Runs the MiniMax algorithm to the given depth</li>
     *   <li>Outputs the result to the specified file and console</li>
     * </ol>
     *
     * @param params A map of containing the following keys:
     *               <ul>
     *                 <li>"inputDir" – Path to the input board file</li>
     *                 <li>"outputDir" – Path to the output board file</li>
     *                 <li>"depth" – Search depth for the MiniMax tree</li>
     *               </ul>
     * @throws IOException If there is an error reading the input file or writing the output.
     */
    private static void computeMiniMax(Map<String, String> params) throws IOException {
        // Read input board state from file
        String inputString = getInputStringFromFile(params.get("inputDir"));

        //Validate the input string obtained form the file
        validateInputString(inputString);

        // Initialize static estimation function (as per Jumpy3 handout)
        StaticEstimator staticEstimator = new BasicEstimator();

        // Configure MiniMax algorithm with the estimator
        MiniMaxAlgorithm miniMaxAlgorithm = new MiniMaxAlgorithm(staticEstimator);

        // Parse the board state
        Board board = new Board(inputString);

        // Compute best move for White using MiniMax at the specified depth
        Result result = miniMaxAlgorithm.computeBestMove(
                board,
                Integer.parseInt(params.get("depth")),
                Player.WHITE
        );

        // Write output to file and print metrics
        output(result, params.get("outputDir"), ProblemType.MIN_MAX);
    }
}