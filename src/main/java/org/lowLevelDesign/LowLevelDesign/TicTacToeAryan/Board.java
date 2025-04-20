package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int columns;
    private Symbol[][] grid;
    private List<GameEventListener> listeners;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new Symbol[rows][columns];
        listeners = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = Symbol.EMPTY;
            }
        }
    }

    public boolean isValidMove(Position pos) {
        return pos.row >= 0 && pos.row < rows && pos.col >= 0 && pos.col < columns
                && grid[pos.row][pos.col] == Symbol.EMPTY;
    }

    public void makeMove(Position pos, Symbol symbol) {
        grid[pos.row][pos.col] = symbol;
        notifyMoveMade(pos, symbol);
    }

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    private void notifyMoveMade(Position position, Symbol symbol) {
        for (GameEventListener listener : listeners) {
            listener.onMoveMade(position, symbol);
        }
    }

    private void notifyGameStateChanged(GameState state) {
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged(state);
        }
    }

    public void checkGameState(GameContext context) {
        // Check rows
        for (int i = 0; i < rows; i++) {
            if (grid[i][0] != Symbol.EMPTY && isWinningLine(grid[i])) {
                GameState newState = grid[i][0] == Symbol.X ? new XWonState() : new OWonState();
                context.setState(newState);
                notifyGameStateChanged(newState);
                return;
            }
        }

        // Check columns
        for (int i = 0; i < columns; i++) {
            Symbol[] column = new Symbol[rows];
            for (int j = 0; j < rows; j++) {
                column[j] = grid[j][i];
            }
            if (column[0] != Symbol.EMPTY && isWinningLine(column)) {
                GameState newState = column[0] == Symbol.X ? new XWonState() : new OWonState();
                context.setState(newState);
                notifyGameStateChanged(newState);
                return;
            }
        }

        // Check diagonals
        Symbol[] diagonal1 = new Symbol[Math.min(rows, columns)];
        Symbol[] diagonal2 = new Symbol[Math.min(rows, columns)];
        for (int i = 0; i < Math.min(rows, columns); i++) {
            diagonal1[i] = grid[i][i];
            diagonal2[i] = grid[i][columns - 1 - i];
        }

        if (diagonal1[0] != Symbol.EMPTY && isWinningLine(diagonal1)) {
            GameState newState = diagonal1[0] == Symbol.X ? new XWonState() : new OWonState();
            context.setState(newState);
            notifyGameStateChanged(newState);
            return;
        }

        if (diagonal2[0] != Symbol.EMPTY && isWinningLine(diagonal2)) {
            GameState newState = diagonal2[0] == Symbol.X ? new XWonState() : new OWonState();
            context.setState(newState);
            notifyGameStateChanged(newState);
            return;
        }

        // Check for draw
        boolean isDraw = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == Symbol.EMPTY) {
                    isDraw = false;
                    break;
                }
            }
        }

        if (isDraw) {
            context.setState(new DrawState());
            notifyGameStateChanged(new DrawState());
        }
    }

    private boolean isWinningLine(Symbol[] line) {
        Symbol first = line[0];
        for (Symbol s : line) {
            if (s != first) {
                return false;
            }
        }
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
} 