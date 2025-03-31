package models;

import java.util.List;

/**
 * Implements the Alpha-Beta pruning algorithm for optimal move selection in Jumpy3.
 * <p>
 * Optimizes MiniMax by pruning branches that cannot influence the final decision. Produces
 * identical results to MiniMax but with significantly fewer position evaluations.
 * </p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Depth-limited search with configurable ply</li>
 *   <li>Alpha-beta window pruning</li>
 *   <li>Player-specific move generation</li>
 *   <li>Terminal state detection</li>
 * </ul>
 */
public class AlphaBetaAlgorithm {
    private final StaticEstimator estimator;

    /**
     * Creates an Alpha-Beta solver with a specific evaluation strategy
     *
     * @param estimator Heuristic function for board evaluation
     */
    public AlphaBetaAlgorithm(StaticEstimator estimator) {
        this.estimator = estimator;
    }

    /**
     * Initiates Alpha-Beta search to find optimal move for specified player
     *
     * @param board  Initial board state
     * @param depth  Search depth (ply)
     * @param player WHITE (maximizing) or BLACK (minimizing)
     * @return Result containing best move, estimate, and evaluation metrics
     */
    public Result computeBestMove(Board board, int depth, Player player) {
        return alphaBeta(
                board,
                depth,
                Integer.MIN_VALUE,  // Initial alpha
                Integer.MAX_VALUE,  // Initial beta
                player == Player.WHITE,  // Maximizing flag
                player
        );
    }

    /**
     * Recursive Alpha-Beta implementation with pruning
     *
     * @param board         Current board state
     * @param depth         Remaining search depth
     * @param alpha         Best already explored value for maximizer
     * @param beta          Best already explored value for minimizer
     * @param isMaximizing  True for White's turn, false for Black's
     * @param currentPlayer Player making the current move
     * @return Best result for current search subtree
     */
    private Result alphaBeta(Board board, int depth, int alpha, int beta,
                             boolean isMaximizing, Player currentPlayer) {
        // Base case: leaf node or terminal state
        if (depth == 0 || board.isTerminal()) {
            return new Result(
                    estimator.estimate(board, currentPlayer),
                    null,  // No move tracking at leaves
                    1  // Single position evaluated
            );
        }

        // Generate legal moves for current player
        List<Board> moves = currentPlayer == Player.WHITE
                ? board.generateWhiteMoves()
                : board.generateBlackMoves();

        // Handle no legal moves situation
        if (moves.isEmpty()) {
            return new Result(
                    estimator.estimate(board, currentPlayer),
                    null,
                    1
            );
        }

        int bestEstimate = isMaximizing
                ? Integer.MIN_VALUE  // Initialize for maximizer
                : Integer.MAX_VALUE; // Initialize for minimizer
        Board bestBoard = null;
        int totalEval = 0;

        // Evaluate moves with pruning
        for (Board move : moves) {
            // Recursive search with updated depth and player
            Result result = alphaBeta(
                    move,
                    depth - 1,
                    alpha,
                    beta,
                    !isMaximizing,
                    currentPlayer.opposite()
            );
            totalEval += result.getPositionsEvaluated();

            // Update best value and move
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

            // Prune remaining branches if possible
            if (beta <= alpha) break;
        }

        return new Result(bestEstimate, bestBoard, totalEval);
    }
}