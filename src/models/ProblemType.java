package models;

import java.util.Arrays;

/**
 * Represents different algorithm variants supported by the program.
 * <p>
 * Used to categorize computation results and configure output formatting.
 * Each type corresponds to a specific program implementation:
 * <ul>
 *   <li>MIN_MAX - Basic MiniMax algorithm for White</li>
 *   <li>MIN_MAX_BLACK - MiniMax for Black's moves</li>
 *   <li>MIN_MAX_IMPROVED - MiniMax with enhanced static estimation</li>
 *   <li>ALPHA_BETA - Alpha-Beta pruning implementation</li>
 * </ul>
 * </p>
 */
public enum ProblemType {

    /**
     * Basic MiniMax algorithm implementation for White player moves
     */
    MIN_MAX("MiniMax"),

    /**
     * MiniMax variant computing moves for Black player
     */
    MIN_MAX_BLACK("MiniMaxBlack"),

    /**
     * MiniMax with custom improved static estimation function
     */
    MIN_MAX_IMPROVED("MiniMaxImproved"),

    /**
     * Alpha-Beta pruning optimization implementation
     */
    ALPHA_BETA("AlphaBeta");

    private final String name;

    /**
     * Enum constructor for problem type variants
     *
     * @param name Display name used in output reports
     */
    ProblemType(String name) {
        this.name = name;
    }

    /**
     * Converts a string name to its corresponding ProblemType
     *
     * @param name Case-sensitive display name (e.g., "MiniMaxImproved")
     * @return Matching ProblemType enum value
     * @throws IllegalArgumentException If no matching type exists
     *                                  <p>ProblemType.fromName("AlphaBeta") → ALPHA_BETA</p>
     */
    public static ProblemType fromName(String name) {
        return Arrays.stream(ProblemType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid problem type: " + name));
    }

    /**
     * @return The display name used in output reports and UI
     */
    public String getName() {
        return name;
    }
}