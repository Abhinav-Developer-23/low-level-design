package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class DrawState implements GameState {
    @Override
    public void next(GameContext context) {
        // Game over, no next state
    }

    @Override
    public boolean isGameOver() {
        return true;
    }
} 