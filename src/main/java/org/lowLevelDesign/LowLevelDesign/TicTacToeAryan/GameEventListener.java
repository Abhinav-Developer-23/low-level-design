package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public interface GameEventListener {
    void onMoveMade(Position position, Symbol symbol);
    void onGameStateChanged(GameState state);
} 