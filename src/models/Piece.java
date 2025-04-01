package models;

import java.util.Arrays;

/**
 * Represents game pieces and empty spaces in Jumpy3 board configurations.
 * <p>
 * Maps piece types to their character symbols used in board string representations.
 * Used for parsing board states and validating game configurations.
 * </p>
 *
 * <p>Board string example: "WwwwxxxxxxxxbbbB" represents:</p>
 * <ul>
 *   <li>WHITE_KING at position 0</li>
 *   <li>3 WHITE_PAWNs at positions 1-3</li>
 *   <li>11 EMPTY spaces</li>
 *   <li>3 BLACK_PAWNs at positions 13-15</li>
 *   <li>BLACK_KING at position 15</li>
 * </ul>
 */
public enum Piece {
    /**
     * White player's king piece represented by 'W'
     */
    WHITE_KING('W'),

    /**
     * White player's pawn piece represented by 'w'
     */
    WHITE_PAWN('w'),

    /**
     * Black player's king piece represented by 'B'
     */
    BLACK_KING('B'),

    /**
     * Black player's pawn piece represented by 'b'
     */
    BLACK_PAWN('b'),

    /**
     * Empty board position represented by 'x'
     */
    EMPTY('x');

    private final char piece;

    /**
     * Enum constructor for piece types
     *
     * @param piece Character symbol used in board strings
     */
    Piece(char piece) {
        this.piece = piece;
    }

    /**
     * Converts board character to corresponding Piece enum
     *
     * @param piece Character from board string (case-sensitive)
     * @return Matching Piece enum value
     * @throws IllegalArgumentException For invalid characters
     *
     *                                  <pre>
     *                                  Piece.fromChar('W') → WHITE_KING
     *                                  Piece.fromChar('x') → EMPTY
     *                                  </pre>
     */
    public static Piece fromChar(char piece) {
        return Arrays.stream(Piece.values())
                .filter(p -> p.piece == piece)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid piece character: '" + piece + "'. " +
                                "Valid values: W, w, B, b, x"
                ));
    }

    /**
     * @return Character symbol used in board position strings
     */
    public char getPiece() {
        return piece;
    }
}