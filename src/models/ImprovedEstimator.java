package models;

import java.util.ArrayList;

/**
 * Enhanced static estimation function with multi-factor heuristic analysis.
 * <p>
 * Improves upon {@link BasicEstimator} by evaluating:
 * <ul>
 *   <li>Weighted king advancement (3x base heuristic)</li>
 *   <li>Pawn positional value (forward placement bonuses)</li>
 *   <li>Path blocking/capturable pawn analysis</li>
 *   <li>Pawn quantity advantage</li>
 *   <li>King exit proximity bonuses</li>
 * </ul>
 *
 * <p>Heuristic components from White's perspective:</p>
 * <ol>
 *   <li><b>King Base Value</b>: 3×(WhiteKing + BlackKing - 15)</li>
 *   <li><b>Pawn Position Bonus</b>: ∑(WhitePawn positions) - ∑(15 - BlackPawn positions)</li>
 *   <li><b>Path Analysis</b>: +2×(capturable black pawns - blocking white pawns) ahead of White King</li>
 *   <li><b>Pawn Count</b>: +2×(WhitePawns - BlackPawns)</li>
 *   <li><b>Clear Path</b>: -3 per WhitePawn, +2 per BlackPawn in White King's path</li>
 *   <li><b>King Exit Proximity</b>: +50 if WhiteKing ≥13, -50 if BlackKing ≤2</li>
 * </ol>
 *
 * <p>Note: All evaluations are from White's perspective - the {@code player} parameter
 * is not used in calculations.</p>
 */
public class ImprovedEstimator implements StaticEstimator {

    /**
     * Counts occurrences of a specific piece type on the board
     *
     * @param positions Board configuration to analyze
     * @param piece     Target piece type to count
     * @return Number of matching pieces (0-16)
     */
    private int countPawns(ArrayList<Piece> positions, Piece piece) {
        return (int) positions.stream().filter(p -> p.equals(piece)).count();
    }

    /**
     * Computes board value using enhanced multi-factor heuristic
     *
     * @param board  Current game state to evaluate
     * @param player <b>Not used</b> - evaluations always favor White
     * @return Estimated value with complex weighting. Higher values indicate
     * better positions for White, lower values favor Black.
     * @Example <pre>
     * White King at 12, Black King at 3:
     * Base: 3*(12+3-15) = 0
     * 2 White pawns at 10,11 → 10+11 = 21
     * 1 Black pawn at 4 → 15-4 = 11
     * Pawn Bonus: 21-11 = +10
     * Total estimate: 0+10+...+exit bonuses
     * </pre>
     */
    @Override
    public int estimate(Board board, Player player) {
        // Terminal state checks
        if (board.isWhiteWin()) return 100;
        if (board.isBlackWin()) return -100;

        // King position analysis
        int whiteKing = board.getWhiteKingPosition();
        int blackKing = board.getBlackKingPosition();
        int estimate = 3 * (whiteKing + blackKing - 15);

        // Pawn positional value calculation
        int sumWhitePawns = 0;
        int sumBlackPawns = 0;
        for (int i = 0; i < board.getPositions().size(); i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) sumWhitePawns += i;
            if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) sumBlackPawns += (15 - i);
        }
        estimate += (sumWhitePawns - sumBlackPawns);

        // Path analysis ahead of White King
        int blockingWhite = 0;
        int capturableBlack = 0;
        for (int i = whiteKing + 1; i < 16; i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) blockingWhite++;
            if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) capturableBlack++;
        }
        estimate += 2 * (capturableBlack - blockingWhite);

        // Pawn quantity advantage
        int whitePawns = countPawns(board.getPositions(), Piece.WHITE_PAWN);
        int blackPawns = countPawns(board.getPositions(), Piece.BLACK_PAWN);
        estimate += 2 * (whitePawns - blackPawns);

        // Clear path scoring
        int clearPath = 0;
        for (int i = whiteKing + 1; i < 16; i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) clearPath -= 3;
            else if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) clearPath += 2;
        }
        estimate += clearPath;

        // King exit proximity bonuses
        if (whiteKing >= 13) estimate += 50;
        if (blackKing <= 2) estimate -= 50;

        return estimate;
    }
}