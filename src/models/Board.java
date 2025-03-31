package models;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final ArrayList<Piece> positions;

    public Board(String positionStr) {
        this.positions = new ArrayList<>();
        for (char c : positionStr.toCharArray()) {
            positions.add(Piece.fromChar(c));
        }
    }

    private Board(ArrayList<Piece> positions) {
        this.positions = new ArrayList<>(positions);
    }

    public ArrayList<Piece> getPositions() {
        return positions;
    }

    public Board copy() {
        return new Board(new ArrayList<>(positions));
    }

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

    public List<Board> generateBlackMoves() {
        Board flipped = flip();
        List<Board> flippedMoves = flipped.generateWhiteMoves();
        List<Board> originalMoves = new ArrayList<>();
        for (Board fm : flippedMoves) {
            originalMoves.add(fm.flip());
        }
        return originalMoves;
    }

    public boolean isWhiteWin() {
        return positions.stream().noneMatch(piece -> piece == Piece.WHITE_KING);
    }

    public boolean isBlackWin() {
        return positions.stream().noneMatch(piece -> piece == Piece.BLACK_KING);
    }

    public boolean isTerminal() {
        return isWhiteWin() || isBlackWin();
    }

    public int getWhiteKingPosition() {
        return positions.indexOf(Piece.WHITE_KING);
    }

    public int getBlackKingPosition() {
        return positions.indexOf(Piece.BLACK_KING);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Piece piece : positions) {
            stringBuilder.append(piece.getPiece());
        }
        return stringBuilder.toString();
    }
}