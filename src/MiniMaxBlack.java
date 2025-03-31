import models.*;

import java.io.IOException;
import java.util.Map;

import static models.Utils.*;

public class MiniMaxBlack {
    public static void main(String[] args) {
        Map<String, String> params = convertArgsToMap(args);
        try {
            computeMiniMaxBlack(params);
        } catch (IOException e) {
            throw new RuntimeException("Error while computing alpha Beta", e);
        }
    }

    private static void computeMiniMaxBlack(Map<String, String> params) throws IOException {
        String inputOneString = getInputStringFromFile(params.get("inputDir"));
        StaticEstimator staticEstimator = new BasicEstimator();
        MiniMaxAlgorithm miniMaxAlgorithm = new MiniMaxAlgorithm(staticEstimator);
        Board board = new Board(inputOneString);
        Result result = miniMaxAlgorithm.computeBestMove(board, Integer.parseInt(params.get("depth")), Player.BLACK);
        output(result, params.get("outputDir"), ProblemType.MIN_MAX_BLACK);
    }
}
