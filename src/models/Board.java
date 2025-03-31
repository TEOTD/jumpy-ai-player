package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Jumpy3 game board state and provides move generation logic.
 * <p>
 * Handles core game mechanics including:
 * <ul>
 *   <li>Parsing board configurations from strings</li>
 *   <li>Generating valid moves for both players</li>
 *   <li>Detecting terminal/won states</li>
 *   <li>Flipping board perspective for Black's moves</li>
 * </ul>
 *
 * <p>Board positions are stored as 16-element list from left(0) to right(15).</p>
 */
public class Board {
    private final ArrayList<Piece> positions;

    /**
     * Constructs board from 16-character string representation
     *
     * @param positionStr 16-character string using W/w/B/b/x
     * @throws IllegalArgumentException For invalid strings
     */
    public Board(String positionStr) {
        this.positions = new ArrayList<>();
        for (char c : positionStr.toCharArray()) {
            positions.add(Piece.fromChar(c));
        }
    }

    /**
     * Internal constructor for copy operations
     */
    private Board(ArrayList<Piece> positions) {
        this.positions = new ArrayList<>(positions);
    }

    /**
     * @return Current board configuration
     */
    public ArrayList<Piece> getPositions() {
        return positions;
    }

    /**
     * Creates a deep copy of the board
     */
    public Board copy() {
        return new Board(new ArrayList<>(positions));
    }

    /**
     * Generates all valid White player moves
     *
     * @return List of possible next board states with:
     * <ul>
     *   <li>Normal moves (single space right)</li>
     *   <li>Jumps over pieces</li>
     *   <li>Captures (send jumped Black pieces to rightmost empty)</li>
     *   <li>King exits (remove from board)</li>
     * </ul>
     */
    public List<Board> generateWhiteMoves() {
        List<Board> moves = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Piece piece = positions.get(i);
            if (piece == Piece.WHITE_KING || piece == Piece.WHITE_PAWN) {
                if (i == 15) {
                    Board newBoard = copy();
                    newBoard.positions.set(i, Piece.EMPTY);
                    moves.add(newBoard);
                    continue;
                }

                if (positions.get(i + 1) == Piece.EMPTY) {
                    Board newBoard = copy();
                    newBoard.positions.set(i, Piece.EMPTY);
                    newBoard.positions.set(i + 1, piece);
                    moves.add(newBoard);
                    continue;
                }

                int j = i + 1;
                while (j < 16 && positions.get(j) != Piece.EMPTY) j++;

                Board newBoard = copy();
                newBoard.positions.set(i, Piece.EMPTY);

                if (j < 16) {
                    newBoard.positions.set(j, piece);

                    if (j == i + 2) {
                        Piece jumped = positions.get(i + 1);
                        if (jumped == Piece.BLACK_KING || jumped == Piece.BLACK_PAWN) {
                            int k = 15;
                            while (k >= 0 && newBoard.positions.get(k) != Piece.EMPTY) k--;

                            if (k >= 0) {
                                newBoard.positions.set(k, jumped);
                                newBoard.positions.set(i + 1, Piece.EMPTY);
                            }
                        }
                    }
                }
                moves.add(newBoard);
            }
        }
        return moves;
    }

    /**
     * Creates mirrored board perspective for Black move generation
     *
     * @return Board with:
     * <ul>
     *   <li>Positions reversed</li>
     *   <li>White↔Black pieces swapped</li>
     * </ul>
     */
    public Board flip() {
        ArrayList<Piece> flipped = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            Piece piece = positions.get(15 - i);
            switch (piece) {
                case WHITE_KING:
                    piece = Piece.BLACK_KING;
                    break;
                case BLACK_KING:
                    piece = Piece.WHITE_KING;
                    break;
                case WHITE_PAWN:
                    piece = Piece.BLACK_PAWN;
                    break;
                case BLACK_PAWN:
                    piece = Piece.WHITE_PAWN;
                    break;
                default:
                    piece = Piece.EMPTY;
            }
            flipped.add(piece);
        }
        return new Board(flipped);
    }

    /**
     * Generates Black moves using flipped perspective
     *
     * @see #flip()
     */
    public List<Board> generateBlackMoves() {
        Board flipped = flip();
        List<Board> flippedMoves = flipped.generateWhiteMoves();
        List<Board> originalMoves = new ArrayList<>();
        for (Board fm : flippedMoves) {
            originalMoves.add(fm.flip());
        }
        return originalMoves;
    }

    /**
     * @return True if White king has exited the board
     */
    public boolean isWhiteWin() {
        return !positions.contains(Piece.WHITE_KING);
    }

    /**
     * @return True if Black king has exited the board
     */
    public boolean isBlackWin() {
        return !positions.contains(Piece.BLACK_KING);
    }

    /**
     * @return True if either player has won
     */
    public boolean isTerminal() {
        return isWhiteWin() || isBlackWin();
    }

    /**
     * @return Current position index of White king (-1 if exited)
     */
    public int getWhiteKingPosition() {
        return positions.indexOf(Piece.WHITE_KING);
    }

    /**
     * @return Current position index of Black king (-1 if exited)
     */
    public int getBlackKingPosition() {
        return positions.indexOf(Piece.BLACK_KING);
    }

    /**
     * @return 16-character string representation of board state
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Piece piece : positions) {
            stringBuilder.append(piece.getPiece());
        }
        return stringBuilder.toString();
    }
}