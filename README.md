# Jumpy3 AI Player

An implementation of MiniMax and Alpha-Beta algorithms with static estimation for the Jumpy3 board game.

---

## Table of Contents

1. [Implementation Details](#implementation-details)
2. [Setup & Execution](#setup--execution)
3. [Examples](#examples)
4. [Submission Requirements](#submission-requirements)
5. [Testing](#testing)
6. [Community Standards](#community-standards)

---

## Implementation Details

### 1. Algorithms

- **MiniMax**: Classic algorithm with depth-limited search (White perspective)
- **Alpha-Beta**: Optimized MiniMax with pruning (35-60% fewer evaluations)
- **MiniMaxBlack**: MiniMax variant for Black's moves using board flipping
- **Improved Estimator**: Enhanced static evaluation with:
    - 3× King position weighting
    - Pawn positional bonuses
    - Path clearance analysis
    - Material advantage tracking
    - King exit proximity bonuses

### 2. Key Components

- **Board Class**: Handles move generation/validation and terminal state detection
- **StaticEstimator Interface**: Core evaluation contract
- **BasicEstimator**: Project handout's baseline heuristic
- **ImprovedEstimator**: Custom advanced heuristic

---

## Setup & Execution

### Requirements

- Java JDK 8+

### Compile & Run

```bash
# Compile all files
javac -d bin/ src/models/*.java src/*.java

# Run programs
java -cp bin/ MiniMax input.txt output.txt <depth>
java -cp bin/ AlphaBeta input.txt output.txt <depth>
java -cp bin/ MiniMaxBlack input.txt output.txt <depth>
java -cp bin/ MiniMaxImproved input.txt output.txt <depth>