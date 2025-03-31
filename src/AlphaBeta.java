import models.*;

import java.io.IOException;
import java.util.Map;

import static models.Utils.*;

public class AlphaBeta {
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeAlphaBeta(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing alpha Beta", e);
        }
    }

    private static void computeAlphaBeta(Map<String, String> params) throws IOException {
        String inputOneString = getInputStringFromFile(params.get("inputDir"));
        StaticEstimator staticEstimator = new BasicEstimator();
        AlphaBetaAlgorithm alphaBetaAlgorithm = new AlphaBetaAlgorithm(staticEstimator);
        Board board = new Board(inputOneString);
        Result result = alphaBetaAlgorithm.computeBestMove(board, Integer.parseInt(params.get("depth")), Player.WHITE);
        output(result, params.get("outputDir"), ProblemType.ALPHA_BETA);
    }
}
