package models;

public class Result {
    private final int estimate;
    private final Board bestBoard;
    private final int positionsEvaluated;

    public Result(int estimate, Board bestBoard, int positionsEvaluated) {
        this.estimate = estimate;
        this.bestBoard = bestBoard;
        this.positionsEvaluated = positionsEvaluated;
    }

    public int getPositionsEvaluated() {
        return positionsEvaluated;
    }

    public Board getBestBoard() {
        return bestBoard;
    }

    public int getEstimate() {
        return estimate;
    }
}