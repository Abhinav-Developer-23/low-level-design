package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class GameContext {
    private GameState currentState;

    public GameContext() {
        currentState = new XTurnState(); // Start with X's turn
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void next() {
        currentState.next(this);
    }

    public boolean isGameOver() {
        return currentState.isGameOver();
    }

    public GameState getCurrentState() {
        return currentState;
    }
} 