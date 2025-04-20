package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

public class Player {
    private final Symbol symbol;
    private final PlayerStrategy strategy;

    public Player(Symbol symbol, PlayerStrategy strategy) {
        this.symbol = symbol;
        this.strategy = strategy;
    }

    public Position makeMove(Board board) {
        return strategy.makeMove(board);
    }

    public Symbol getSymbol() {
        return symbol;
    }
} 