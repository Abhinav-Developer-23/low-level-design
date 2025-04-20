package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame implements BoardGames {
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameContext gameContext;

    public TicTacToeGame(int boardSize, List<PlayerStrategy> strategies) {
        board = new Board(boardSize, boardSize);
        players = new ArrayList<>();
        for (int i = 0; i < strategies.size(); i++) {
            Symbol symbol = Symbol.values()[i + 1]; // Skip EMPTY
            players.add(new Player(symbol, strategies.get(i)));
        }
        currentPlayerIndex = 0;
        gameContext = new GameContext();
        board.addListener(new ConsoleGameEventListener());
    }

    @Override
    public void play() {
        do {
            board.printBoard();
            Player currentPlayer = getCurrentPlayer();
            Position move = currentPlayer.makeMove(board);
            board.makeMove(move, currentPlayer.getSymbol());
            board.checkGameState(gameContext);
            switchPlayer();
        } while (!gameContext.isGameOver());

        announceResult();
    }

    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void announceResult() {
        GameState state = gameContext.getCurrentState();
        if (state instanceof XWonState) {
            System.out.println("Player X wins!");
        } else if (state instanceof OWonState) {
            System.out.println("Player O wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }
} 