package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class XWonState implements GameState {
    @Override
    public void next(GameContext context) {
        // Game over, no next state
    }

    @Override
    public boolean isGameOver() {
        return true;
    }
} 