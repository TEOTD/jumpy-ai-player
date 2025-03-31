package models;

public class BasicEstimator implements StaticEstimator {
    @Override
    public int estimate(Board board, Player player) {
        if (board.isWhiteWin()) return 100;
        if (board.isBlackWin()) return -100;
        int whiteKing = board.getWhiteKingPosition();
        int blackKing = board.getBlackKingPosition();
        return whiteKing + blackKing - 15;
    }
}