package models;

import java.util.Arrays;

public enum Piece {
    WHITE_KING('W'),
    WHITE_PAWN('w'),
    BLACK_KING('B'),
    BLACK_PAWN('b'),
    EMPTY('x');

    private final char piece;

    Piece(char piece) {
        this.piece = piece;
    }

    public static Piece fromChar(char piece) {
        return Arrays.stream(Piece.values())
                .filter(p -> p.piece == piece)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid piece type: " + piece));
    }

    public char getPiece() {
        return piece;
    }
}