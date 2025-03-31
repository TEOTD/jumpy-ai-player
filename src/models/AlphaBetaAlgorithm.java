package models;

import java.util.List;

public class AlphaBetaAlgorithm {
    private final StaticEstimator estimator;

    public AlphaBetaAlgorithm(StaticEstimator estimator) {
        this.estimator = estimator;
    }

    public Result computeBestMove(Board board, int depth, Player player) {
        return alphaBeta(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player == Player.WHITE, player);
    }

    private Result alphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizing, Player currentPlayer) {
        if (depth == 0 || board.isTerminal()) {
            return new Result(estimator.estimate(board, currentPlayer), null, 1);
        }

        List<Board> moves = currentPlayer == Player.WHITE ? board.generateWhiteMoves() : board.generateBlackMoves();
        if (moves.isEmpty()) {
            return new Result(estimator.estimate(board, currentPlayer), null, 1);
        }

        int bestEstimate = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Board bestBoard = null;
        int totalEval = 0;

        for (Board move : moves) {
            Player nextPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
            Result result = alphaBeta(move, depth - 1, alpha, beta, !isMaximizing, nextPlayer);
            totalEval += result.getPositionsEvaluated();

            if (isMaximizing) {
                if (result.getEstimate() > bestEstimate) {
                    bestEstimate = result.getEstimate();
                    bestBoard = move;
                }
                alpha = Math.max(alpha, bestEstimate);
            } else {
                if (result.getEstimate() < bestEstimate) {
                    bestEstimate = result.getEstimate();
                    bestBoard = move;
                }
                beta = Math.min(beta, bestEstimate);
            }

            if (beta <= alpha) break;
        }

        return new Result(bestEstimate, bestBoard, totalEval);
    }
}