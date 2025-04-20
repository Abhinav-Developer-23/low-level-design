package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class XTurnState implements GameState {
    @Override
    public void next(GameContext context) {
        context.setState(new OTurnState());
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
} 