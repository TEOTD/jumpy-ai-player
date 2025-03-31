package models;

/**
 * Represents the outcome of a game tree search algorithm (MiniMax/Alpha-Beta).
 * <p>
 * Immutable container storing:
 * <ul>
 *   <li>The optimal board position found</li>
 *   <li>Static estimation value of that position</li>
 *   <li>Total number of positions evaluated during search</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * Result r = new Result(1500, bestBoard, 42);
 * System.out.println(r.getEstimate()); // 1500
 * </pre>
 */
public class Result {
    private final int estimate;
    private final Board bestBoard;
    private final int positionsEvaluated;

    /**
     * Constructs a search result container
     *
     * @param estimate           Static estimation value of the best board position
     * @param bestBoard          Optimal board configuration found by the algorithm
     * @param positionsEvaluated Total number of board positions evaluated
     */
    public Result(int estimate, Board bestBoard, int positionsEvaluated) {
        this.estimate = estimate;
        this.bestBoard = bestBoard;
        this.positionsEvaluated = positionsEvaluated;
    }

    /**
     * @return Number of board positions evaluated during search.
     * Indicates algorithm performance.
     */
    public int getPositionsEvaluated() {
        return positionsEvaluated;
    }

    /**
     * @return Optimal board configuration determined by the search algorithm.
     * Represents the best move to make.
     */
    public Board getBestBoard() {
        return bestBoard;
    }

    /**
     * @return Static estimation value for the best board position.
     * Higher values indicate better positions for White.
     */
    public int getEstimate() {
        return estimate;
    }
}