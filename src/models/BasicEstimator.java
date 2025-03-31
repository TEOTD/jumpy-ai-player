package models;

/**
 * Basic static estimation function as specified in the Jumpy3 project handout.
 * <p>
 * Provides a simple heuristic evaluating board positions based on:
 * <ul>
 *   <li>Immediate win/loss detection (100/-100)</li>
 *   <li>Relative king positions for non-terminal states</li>
 * </ul>
 *
 * <p>The heuristic formula for non-terminal positions is:</p>
 * <pre>(WhiteKingPosition + BlackKingPosition - 15)</pre>
 *
 * <p>This encourages White to advance right (higher positions) and Black to
 * retreat left (lower positions). Values increase when White's king progresses
 * or Black's king is pushed back.</p>
 *
 * <p>Note: This implementation does not use the {@code player} parameter -
 * returns absolute values from White's perspective regardless of current player.</p>
 */
public class BasicEstimator implements StaticEstimator {

    /**
     * Computes board value using project-specified heuristic rules
     *
     * @param board  Current game state to evaluate
     * @param player <b>Not used</b> in this implementation (heuristic remains
     *               absolute from White's perspective)
     * @return Estimated value where:
     * <ul>
     *   <li>100 = White victory</li>
     *   <li>-100 = Black victory</li>
     *   <li>i + j - 15 otherwise (i=White king position, j=Black king position)</li>
     * </ul>
     * @Example <pre>
     * White king at 3, Black king at 12 → 3 + 12 - 15 = 0
     * White king exits (win) → returns 100 immediately
     * </pre>
     */
    @Override
    public int estimate(Board board, Player player) {
        // Immediate terminal state checks
        if (board.isWhiteWin()) return 100;
        if (board.isBlackWin()) return -100;

        // King position-based heuristic
        int whiteKing = board.getWhiteKingPosition();
        int blackKing = board.getBlackKingPosition();
        return whiteKing + blackKing - 15;
    }
}