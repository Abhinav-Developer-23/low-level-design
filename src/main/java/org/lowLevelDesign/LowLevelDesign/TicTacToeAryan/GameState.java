package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public interface GameState {
    void next(GameContext context);
    boolean isGameOver();
} 