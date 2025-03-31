package models;

/**
 * Defines a contract for static estimation functions used to evaluate board positions in Jumpy3.
 * <p>
 * Implementations provide heuristic values representing the desirability of a board configuration
 * for a specific player. Used by search algorithms like MiniMax and Alpha-Beta for decision-making.
 * </p>
 *
 * <p>Example implementations:</p>
 * <ul>
 *   <li>{@code BasicEstimator} - Uses simple king/pawn positions as per project handout</li>
 *   <li>{@code ImprovedEstimator} - Custom heuristic with enhanced positional analysis</li>
 * </ul>
 */
public interface StaticEstimator {

    /**
     * Computes a heuristic value for the given board state from the perspective of the specified player.
     * Higher values indicate more favorable positions for the player.
     *
     * @param board  Current game state to evaluate. Must not be null.
     * @param player Perspective player (WHITE or BLACK). Must not be null.
     * @return Estimated advantage score. Positive values favor the player.
     * @throws NullPointerException if {@code board} or {@code player} is null
     * @Example <pre>
     * // Basic implementation might return:
     * White king at position 3, Black king at position 12 → 3 + 12 - 15 = 0
     * </pre>
     */
    int estimate(Board board, Player player);
}