package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class ConsoleGameEventListener implements GameEventListener {
    @Override
    public void onMoveMade(Position position, Symbol symbol) {
        System.out.println("Move made at position: " + position + " by " + symbol);
    }

    @Override
    public void onGameStateChanged(GameState state) {
        System.out.println("Game state changed to: " + state.getClass().getSimpleName());
    }
} 