package models;

import java.util.Arrays;

public enum ProblemType {
    ALL("All"),
    MIN_MAX("MiniMax"),
    MIN_MAX_BLACK("MiniMaxBlack"),
    MIN_MAX_IMPROVED("MiniMaxImproved"),
    ALPHA_BETA("AlphaBeta");


    private final String name;

    ProblemType(String name) {
        this.name = name;
    }

    public static ProblemType fromName(String name) {
        return Arrays.stream(ProblemType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid problem type: " + name));
    }

    public String getName() {
        return name;
    }
}
