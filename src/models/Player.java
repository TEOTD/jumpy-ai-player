package models;

/**
 * Represents the two players in a Jumpy3 game session.
 * <p>
 * Used throughout the program to:
 * <ul>
 *   <li>Track current turn state</li>
 *   <li>Determine move generation perspective</li>
 *   <li>Calculate player-specific static estimates</li>
 * </ul>
 *
 * <p>The game follows standard turn order:</p>
 * <ol>
 *   <li>WHITE moves first</li>
 *   <li>BLACK responds</li>
 *   <li>Turns alternate until victory</li>
 * </ol>
 *
 * @see MiniMaxAlgorithm#computeBestMove(Board, int, Player)
 */
public enum Player {
    /**
     * First-moving player starting on left side of board.
     * Controls white pieces (W=King, w=pawns).
     */
    WHITE,

    /**
     * Second-moving player starting on right side of board.
     * Controls black pieces (B=King, b=pawns).
     */
    BLACK;

    /**
     * Returns the opposing player for turn alternation.
     *
     * @return The opposite player enum value
     * <pre>
     * Player.WHITE.opposite() → BLACK
     * Player.BLACK.opposite() → WHITE
     * </pre>
     */
    public Player opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}