package models;

import java.util.List;

public class MiniMaxAlgorithm {
    private final StaticEstimator estimator;

    public MiniMaxAlgorithm(StaticEstimator estimator) {
        this.estimator = estimator;
    }

    public Result computeBestMove(Board board, int depth, Player player) {
        return minimax(board, depth, player == Player.WHITE, player);
    }

    private Result minimax(Board board, int depth, boolean isMaximizing, Player currentPlayer) {
        if (depth == 0 || board.isTerminal()) {
            return new Result(estimator.estimate(board, currentPlayer), null, 1);
        }

        List<Board> moves = currentPlayer == Player.WHITE
                ? board.generateWhiteMoves()
                : board.generateBlackMoves();

        if (moves.isEmpty()) {
            return new Result(estimator.estimate(board, currentPlayer), null, 1);
        }

        int bestEstimate = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Board bestBoard = null;
        int totalEval = 0;

        for (Board move : moves) {
            Player nextPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
            Result result = minimax(move, depth - 1, !isMaximizing, nextPlayer);
            totalEval += result.getPositionsEvaluated();

            if (isMaximizing) {
                if (result.getEstimate() > bestEstimate) {
                    bestEstimate = result.getEstimate();
                    bestBoard = move;
                }
            } else {
                if (result.getEstimate() < bestEstimate) {
                    bestEstimate = result.getEstimate();
                    bestBoard = move;
                }
            }
        }

        return new Result(bestEstimate, bestBoard, totalEval);
    }
}