package models;

import java.util.ArrayList;

public class ImprovedEstimator implements StaticEstimator {

    private int countPawns(ArrayList<Piece> positions, Piece piece) {
        return (int) positions.stream().filter(p -> p.equals(piece)).count();
    }

    @Override
    public int estimate(Board board, Player player) {
        if (board.isWhiteWin()) return 100;
        if (board.isBlackWin()) return -100;

        int whiteKing = board.getWhiteKingPosition();
        int blackKing = board.getBlackKingPosition();

        int estimate = 3 * (whiteKing + blackKing - 15);

        int sumWhitePawns = 0;
        int sumBlackPawns = 0;
        for (int i = 0; i < board.getPositions().size(); i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) sumWhitePawns += i;
            if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) sumBlackPawns += (15 - i);
        }
        estimate += (sumWhitePawns - sumBlackPawns);

        int blockingWhite = 0;
        int capturableBlack = 0;
        for (int i = whiteKing + 1; i < 16; i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) blockingWhite++;
            if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) capturableBlack++;
        }
        estimate += 2 * (capturableBlack - blockingWhite);

        int whitePawns = countPawns(board.getPositions(), Piece.WHITE_PAWN);
        int blackPawns = countPawns(board.getPositions(), Piece.BLACK_PAWN);
        estimate += 2 * (whitePawns - blackPawns);

        int clearPath = 0;
        for (int i = whiteKing + 1; i < 16; i++) {
            if (board.getPositions().get(i).equals(Piece.WHITE_PAWN)) clearPath -= 3;
            else if (board.getPositions().get(i).equals(Piece.BLACK_PAWN)) clearPath += 2;
        }
        estimate += clearPath;

        if (whiteKing >= 13) estimate += 50;
        if (blackKing <= 2) estimate -= 50;

        return estimate;
    }
}