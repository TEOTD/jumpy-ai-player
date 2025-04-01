package models;

import java.util.List;

/**
 * Implements the MiniMax algorithm for determining optimal moves in Jumpy3.
 * <p>
 * This class handles both White (maximizing player) and Black (minimizing player) perspectives
 * through recursive depth-first search. Uses a provided {@link StaticEstimator} for position evaluation.
 * </p>
 *
 * <p>Key functionality:</p>
 * <ul>
 *   <li>Supports configurable search depth</li>
 *   <li>Tracks total positions evaluated during search</li>
 *   <li>Handles terminal states and empty move lists</li>
 * </ul>
 */
public class MiniMaxAlgorithm {
    private final StaticEstimator estimator;

    /**
     * Creates a MiniMax solver with a specific evaluation strategy
     *
     * @param estimator Heuristic function for board evaluation
     */
    public MiniMaxAlgorithm(StaticEstimator estimator) {
        this.estimator = estimator;
    }

    /**
     * Initiates MiniMax search to find optimal move for specified player
     *
     * @param board  Initial board state
     * @param depth  Search depth (one turn by one player)
     * @param player WHITE (maximizing) or BLACK (minimizing)
     * @return Result containing best move, estimate, and evaluation metrics
     */
    public Result computeBestMove(Board board, int depth, Player player) {
        return minimax(board, depth, player == Player.WHITE, player);
    }

    /**
     * Recursive MiniMax implementation with depth-limited search
     *
     * @param board         Current board state
     * @param depth         Remaining search depth
     * @param isMaximizing  True for White's maximizing turn, false for Black's minimizing
     * @param currentPlayer Player making the current move
     * @return Best result for current search subtree
     */
    private Result minimax(Board board, int depth, boolean isMaximizing, Player currentPlayer) {
        // Base case: leaf node or terminal state
        if (depth == 0 || board.isTerminal()) {
            return new Result(
                    estimator.estimate(board),
                    null, // Best move not tracked at leaves
                    1 // Single position evaluated
            );
        }

        // Generate legal moves for current player
        List<Board> moves = currentPlayer == Player.WHITE
                ? board.generateWhiteMoves()
                : board.generateBlackMoves();

        // Handle no legal moves situation
        if (moves.isEmpty()) {
            return new Result(
                    estimator.estimate(board),
                    null,
                    1
            );
        }

        int bestEstimate = isMaximizing
                ? Integer.MIN_VALUE  // Initialize for maximizer
                : Integer.MAX_VALUE; // Initialize for minimizer
        Board bestBoard = null;
        int totalEval = 0;

        // Evaluate all possible moves
        for (Board move : moves) {
            // Alternate players for next level
            Player nextPlayer = currentPlayer.opposite();

            // Recursive depth-first search
            Result result = minimax(move, depth - 1, !isMaximizing, nextPlayer);
            totalEval += result.getPositionsEvaluated();

            // Update best value and move
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