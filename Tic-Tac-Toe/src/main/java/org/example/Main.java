package org.example;

import java.util.*;

/**
 * Tic-Tac-Toe Low-Level Design Implementation
 * 
 * Design Patterns Used:
 * 1. Strategy Pattern - For different win checking strategies
 * 2. Builder Pattern - For creating game configurations
 * 3. State Pattern - For managing game states
 * 4. Observer Pattern - For notifying game events
 * 
 * SOLID Principles Applied:
 * 1. Single Responsibility - Each class has one specific purpose
 * 2. Open/Closed - Extendable without modifying existing code
 * 3. Liskov Substitution - Interfaces can be substituted
 * 4. Interface Segregation - Small, focused interfaces
 * 5. Dependency Inversion - Depends on abstractions, not concretions
 */

// ========================= ENUMS =========================

/**
 * Enum representing different symbols that can be placed on the board
 */
enum Symbol {
    X('X'),
    O('O'),
    EMPTY('-');

    private final char displayChar;

    Symbol(char displayChar) {
        this.displayChar = displayChar;
    }

    public char getDisplayChar() {
        return displayChar;
    }
}

/**
 * Enum representing different game states
 */
enum GameState {
    NOT_STARTED,
    IN_PROGRESS,
    PLAYER_X_WON,
    PLAYER_O_WON,
    DRAW,
    TERMINATED
}

// ========================= INTERFACES =========================

/**
 * Interface for win checking strategies
 * Allows different win conditions to be implemented
 */
interface WinStrategy {
    /**
     * Checks if there's a winner on the board
     * @param board The current game board
     * @param lastMove The last move made
     * @return true if the move resulted in a win
     */
    boolean checkWin(Board board, Move lastMove);
}

/**
 * Interface for game event observers
 * Follows Observer Pattern
 */
interface GameEventObserver {
    /**
     * Called when a move is made
     * @param player The player who made the move
     * @param move The move that was made
     */
    void onMoveMade(Player player, Move move);

    /**
     * Called when game state changes
     * @param oldState Previous game state
     * @param newState New game state
     */
    void onGameStateChanged(GameState oldState, GameState newState);

    /**
     * Called when a player wins
     * @param winner The winning player
     */
    void onPlayerWon(Player winner);

    /**
     * Called when game ends in a draw
     */
    void onGameDraw();
}

/**
 * Interface for input handling
 * Allows different input methods (console, GUI, network, etc.)
 */
interface InputHandler {
    /**
     * Gets the next move from the player
     * @param player The current player
     * @param boardSize The size of the board
     * @return The move chosen by the player
     */
    Move getMove(Player player, int boardSize);
}

/**
 * Interface for displaying game output
 * Allows different display methods
 */
interface DisplayHandler {
    /**
     * Displays the current board state
     * @param board The board to display
     */
    void displayBoard(Board board);

    /**
     * Displays a message to the user
     * @param message The message to display
     */
    void displayMessage(String message);

    /**
     * Displays game result
     * @param result The game result message
     */
    void displayResult(String result);
}

// ========================= MODEL CLASSES =========================

/**
 * Represents a cell on the board
 * Encapsulates cell state and position
 */
class Cell {
    private final int row;
    private final int col;
    private Symbol symbol;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.symbol = Symbol.EMPTY;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public boolean isEmpty() {
        return symbol == Symbol.EMPTY;
    }

    public void reset() {
        this.symbol = Symbol.EMPTY;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol.getDisplayChar());
    }
}

/**
 * Represents a move made by a player
 * Immutable class following value object pattern
 */
class Move {
    private final int row;
    private final int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && col == move.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "Move{row=" + row + ", col=" + col + '}';
    }
}

/**
 * Represents a player in the game
 * Encapsulates player information
 */
class Player {
    private final String name;
    private final Symbol symbol;
    private int wins;

    public Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
        this.wins = 0;
    }

    public String getName() {
        return name;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    @Override
    public String toString() {
        return name + " (" + symbol.getDisplayChar() + ")";
    }
}

/**
 * Represents the game board
 * Encapsulates board state and operations
 */
class Board {
    private final int size;
    private final Cell[][] cells;
    private int movesCount;

    public Board(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board size must be at least 3");
        }
        this.size = size;
        this.cells = new Cell[size][size];
        this.movesCount = 0;
        initializeBoard();
    }

    /**
     * Initializes all cells on the board
     */
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int col) {
        validatePosition(row, col);
        return cells[row][col];
    }

    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Validates if a position is within board bounds
     * @param row Row index
     * @param col Column index
     * @throws IllegalArgumentException if position is invalid
     */
    private void validatePosition(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException(
                "Invalid position: (" + row + ", " + col + "). Must be between 0 and " + (size - 1)
            );
        }
    }

    /**
     * Checks if a move is valid
     * @param move The move to validate
     * @return true if the move is valid
     */
    public boolean isValidMove(Move move) {
        try {
            validatePosition(move.getRow(), move.getCol());
            return cells[move.getRow()][move.getCol()].isEmpty();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Places a symbol on the board
     * @param move The move to make
     * @param symbol The symbol to place
     * @return true if the move was successful
     */
    public boolean makeMove(Move move, Symbol symbol) {
        if (!isValidMove(move)) {
            return false;
        }
        cells[move.getRow()][move.getCol()].setSymbol(symbol);
        movesCount++;
        return true;
    }

    /**
     * Checks if the board is full
     * @return true if all cells are occupied
     */
    public boolean isFull() {
        return movesCount == size * size;
    }

    /**
     * Resets the board to initial state
     */
    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].reset();
            }
        }
        movesCount = 0;
    }

    /**
     * Gets all cells in a specific row
     * @param row Row index
     * @return List of cells in the row
     */
    public List<Cell> getRow(int row) {
        validatePosition(row, 0);
        List<Cell> rowCells = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            rowCells.add(cells[row][j]);
        }
        return rowCells;
    }

    /**
     * Gets all cells in a specific column
     * @param col Column index
     * @return List of cells in the column
     */
    public List<Cell> getColumn(int col) {
        validatePosition(0, col);
        List<Cell> colCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            colCells.add(cells[i][col]);
        }
        return colCells;
    }

    /**
     * Gets all cells in the main diagonal (top-left to bottom-right)
     * @return List of cells in the main diagonal
     */
    public List<Cell> getMainDiagonal() {
        List<Cell> diagonal = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            diagonal.add(cells[i][i]);
        }
        return diagonal;
    }

    /**
     * Gets all cells in the anti-diagonal (top-right to bottom-left)
     * @return List of cells in the anti-diagonal
     */
    public List<Cell> getAntiDiagonal() {
        List<Cell> diagonal = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            diagonal.add(cells[i][size - 1 - i]);
        }
        return diagonal;
    }
}

// ========================= STRATEGY IMPLEMENTATIONS =========================

/**
 * Standard win strategy for Tic-Tac-Toe
 * Checks rows, columns, and diagonals for N consecutive symbols
 */
class StandardWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Board board, Move lastMove) {
        int row = lastMove.getRow();
        int col = lastMove.getCol();
        Symbol symbol = board.getCell(row, col).getSymbol();

        // Check row
        if (checkLine(board.getRow(row), symbol)) {
            return true;
        }

        // Check column
        if (checkLine(board.getColumn(col), symbol)) {
            return true;
        }

        // Check main diagonal (if applicable)
        if (row == col && checkLine(board.getMainDiagonal(), symbol)) {
            return true;
        }

        // Check anti-diagonal (if applicable)
        int size = board.getSize();
        if (row + col == size - 1 && checkLine(board.getAntiDiagonal(), symbol)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if all cells in a line have the same symbol
     * @param cells List of cells to check
     * @param symbol The symbol to match
     * @return true if all cells match the symbol
     */
    private boolean checkLine(List<Cell> cells, Symbol symbol) {
        for (Cell cell : cells) {
            if (cell.getSymbol() != symbol) {
                return false;
            }
        }
        return true;
    }
}

// ========================= OBSERVER IMPLEMENTATIONS =========================

/**
 * Console-based game event observer
 * Logs game events to console
 */
class ConsoleGameObserver implements GameEventObserver {

    @Override
    public void onMoveMade(Player player, Move move) {
        System.out.println("\n" + player.getName() + " placed " + 
            player.getSymbol().getDisplayChar() + " at position (" + 
            move.getRow() + ", " + move.getCol() + ")");
    }

    @Override
    public void onGameStateChanged(GameState oldState, GameState newState) {
        System.out.println("\nGame state changed: " + oldState + " -> " + newState);
    }

    @Override
    public void onPlayerWon(Player winner) {
        System.out.println("\n*** Congratulations! " + winner.getName() + " wins! ***");
    }

    @Override
    public void onGameDraw() {
        System.out.println("\n*** Game ended in a draw! ***");
    }
}

// ========================= INPUT/OUTPUT HANDLERS =========================

/**
 * Console-based input handler
 * Gets player input from console
 */
class ConsoleInputHandler implements InputHandler {
    private final Scanner scanner;

    public ConsoleInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Move getMove(Player player, int boardSize) {
        while (true) {
            try {
                System.out.print("\n" + player.getName() + "'s turn. Enter row (0-" + 
                    (boardSize - 1) + "): ");
                int row = scanner.nextInt();

                System.out.print("Enter column (0-" + (boardSize - 1) + "): ");
                int col = scanner.nextInt();

                return new Move(row, col);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter numbers only.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public void close() {
        scanner.close();
    }
}

/**
 * Demo input handler with pre-programmed moves
 * Demonstrates the game without requiring user input
 */
class DemoInputHandler implements InputHandler {
    private final Queue<Move> preProgrammedMoves;
    private int moveCount;

    public DemoInputHandler(Queue<Move> moves) {
        this.preProgrammedMoves = moves;
        this.moveCount = 0;
    }

    @Override
    public Move getMove(Player player, int boardSize) {
        // Add a small delay for better visualization
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (preProgrammedMoves.isEmpty()) {
            // Fallback to random valid moves if we run out
            return new Move(
                (int) (Math.random() * boardSize),
                (int) (Math.random() * boardSize)
            );
        }

        Move move = preProgrammedMoves.poll();
        moveCount++;
        System.out.println("\n" + player.getName() + "'s turn (Move #" + moveCount + ")");
        System.out.println("  -> Playing at position (" + move.getRow() + ", " + move.getCol() + ")");
        
        return move;
    }
}

/**
 * Console-based display handler
 * Displays game state to console
 */
class ConsoleDisplayHandler implements DisplayHandler {

    @Override
    public void displayBoard(Board board) {
        int size = board.getSize();
        System.out.println("\n" + "=".repeat(size * 4 + 1));

        // Print column indices
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.println();

        // Print board with row indices
        for (int i = 0; i < size; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < size; j++) {
                System.out.print("| " + board.getCell(i, j) + " ");
            }
            System.out.println("|");
            System.out.println("   " + "----".repeat(size) + "-");
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayResult(String result) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(result);
        System.out.println("=".repeat(50));
    }
}

// ========================= GAME CONFIGURATION =========================

/**
 * Game configuration using Builder Pattern
 * Allows flexible game setup
 */
class GameConfig {
    private final int boardSize;
    private final WinStrategy winStrategy;
    private final List<Player> players;
    private final InputHandler inputHandler;
    private final DisplayHandler displayHandler;

    private GameConfig(Builder builder) {
        this.boardSize = builder.boardSize;
        this.winStrategy = builder.winStrategy;
        this.players = builder.players;
        this.inputHandler = builder.inputHandler;
        this.displayHandler = builder.displayHandler;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public WinStrategy getWinStrategy() {
        return winStrategy;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public DisplayHandler getDisplayHandler() {
        return displayHandler;
    }

    /**
     * Builder for GameConfig
     * Implements Builder Pattern
     */
    public static class Builder {
        private int boardSize = 3;
        private WinStrategy winStrategy = new StandardWinStrategy();
        private List<Player> players = Arrays.asList(
            new Player("Player 1", Symbol.X),
            new Player("Player 2", Symbol.O)
        );
        private InputHandler inputHandler = new ConsoleInputHandler();
        private DisplayHandler displayHandler = new ConsoleDisplayHandler();

        public Builder setBoardSize(int boardSize) {
            if (boardSize < 3) {
                throw new IllegalArgumentException("Board size must be at least 3");
            }
            this.boardSize = boardSize;
            return this;
        }

        public Builder setWinStrategy(WinStrategy winStrategy) {
            this.winStrategy = winStrategy;
            return this;
        }

        public Builder setPlayers(List<Player> players) {
            if (players == null || players.size() != 2) {
                throw new IllegalArgumentException("Exactly 2 players required");
            }
            this.players = players;
            return this;
        }

        public Builder setInputHandler(InputHandler inputHandler) {
            this.inputHandler = inputHandler;
            return this;
        }

        public Builder setDisplayHandler(DisplayHandler displayHandler) {
            this.displayHandler = displayHandler;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(this);
        }
    }
}

// ========================= GAME ENGINE =========================

/**
 * Main game engine
 * Manages game flow and state
 */
class TicTacToeGame {
    private final Board board;
    private final List<Player> players;
    private final WinStrategy winStrategy;
    private final InputHandler inputHandler;
    private final DisplayHandler displayHandler;
    private final List<GameEventObserver> observers;

    private GameState gameState;
    private int currentPlayerIndex;
    private Player winner;

    public TicTacToeGame(GameConfig config) {
        this.board = new Board(config.getBoardSize());
        this.players = config.getPlayers();
        this.winStrategy = config.getWinStrategy();
        this.inputHandler = config.getInputHandler();
        this.displayHandler = config.getDisplayHandler();
        this.observers = new ArrayList<>();
        this.gameState = GameState.NOT_STARTED;
        this.currentPlayerIndex = 0;
        this.winner = null;
    }

    /**
     * Adds an observer to the game
     * @param observer Observer to add
     */
    public void addObserver(GameEventObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the game
     * @param observer Observer to remove
     */
    public void removeObserver(GameEventObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of a move
     * @param player Player who made the move
     * @param move The move that was made
     */
    private void notifyMoveMade(Player player, Move move) {
        for (GameEventObserver observer : observers) {
            observer.onMoveMade(player, move);
        }
    }

    /**
     * Notifies all observers of a state change
     * @param oldState Previous state
     * @param newState New state
     */
    private void notifyStateChanged(GameState oldState, GameState newState) {
        for (GameEventObserver observer : observers) {
            observer.onGameStateChanged(oldState, newState);
        }
    }

    /**
     * Notifies all observers of a win
     * @param winner The winning player
     */
    private void notifyPlayerWon(Player winner) {
        for (GameEventObserver observer : observers) {
            observer.onPlayerWon(winner);
        }
    }

    /**
     * Notifies all observers of a draw
     */
    private void notifyGameDraw() {
        for (GameEventObserver observer : observers) {
            observer.onGameDraw();
        }
    }

    /**
     * Changes the game state
     * @param newState New game state
     */
    private void changeState(GameState newState) {
        GameState oldState = this.gameState;
        this.gameState = newState;
        notifyStateChanged(oldState, newState);
    }

    /**
     * Gets the current player
     * @return Current player
     */
    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Switches to the next player
     */
    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Starts the game
     */
    public void start() {
        changeState(GameState.IN_PROGRESS);
        displayHandler.displayMessage("\n================================");
        displayHandler.displayMessage("   Welcome to Tic-Tac-Toe!     ");
        displayHandler.displayMessage("================================");
        displayHandler.displayMessage("\nPlayers:");
        for (Player player : players) {
            displayHandler.displayMessage("  - " + player.toString());
        }
        displayHandler.displayMessage("\nBoard size: " + board.getSize() + "x" + board.getSize());

        playGame();
    }

    /**
     * Main game loop
     */
    private void playGame() {
        while (gameState == GameState.IN_PROGRESS) {
            displayHandler.displayBoard(board);

            Player currentPlayer = getCurrentPlayer();
            Move move = getValidMove(currentPlayer);

            // Make the move
            board.makeMove(move, currentPlayer.getSymbol());
            notifyMoveMade(currentPlayer, move);

            // Check for win
            if (winStrategy.checkWin(board, move)) {
                winner = currentPlayer;
                winner.incrementWins();
                handleWin();
                break;
            }

            // Check for draw
            if (board.isFull()) {
                handleDraw();
                break;
            }

            // Switch to next player
            switchPlayer();
        }

        // Display final board
        displayHandler.displayBoard(board);
        displayResult();
    }

    /**
     * Gets a valid move from the current player
     * @param player Current player
     * @return Valid move
     */
    private Move getValidMove(Player player) {
        while (true) {
            Move move = inputHandler.getMove(player, board.getSize());

            if (board.isValidMove(move)) {
                return move;
            }

            displayHandler.displayMessage("[X] Invalid move! Cell is either occupied or out of bounds. Try again.");
        }
    }

    /**
     * Handles a win condition
     */
    private void handleWin() {
        if (winner.getSymbol() == Symbol.X) {
            changeState(GameState.PLAYER_X_WON);
        } else {
            changeState(GameState.PLAYER_O_WON);
        }
        notifyPlayerWon(winner);
    }

    /**
     * Handles a draw condition
     */
    private void handleDraw() {
        changeState(GameState.DRAW);
        notifyGameDraw();
    }

    /**
     * Displays the final game result
     */
    private void displayResult() {
        StringBuilder result = new StringBuilder();

        if (winner != null) {
            result.append("*** GAME OVER - ").append(winner.getName()).append(" WINS! ***\n");
        } else {
            result.append("*** GAME OVER - IT'S A DRAW! ***\n");
        }

        result.append("\nFinal Statistics:\n");
        for (Player player : players) {
            result.append("  ").append(player.getName()).append(": ")
                  .append(player.getWins()).append(" win(s)\n");
        }

        displayHandler.displayResult(result.toString());
    }

    /**
     * Resets the game for a new round
     */
    public void reset() {
        board.reset();
        currentPlayerIndex = 0;
        winner = null;
        changeState(GameState.NOT_STARTED);
        displayHandler.displayMessage("\n>>> Game has been reset! <<<");
    }

    /**
     * Gets the current game state
     * @return Current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Gets the winner (if any)
     * @return Winner or null if no winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Gets the board
     * @return Game board
     */
    public Board getBoard() {
        return board;
    }
}

// ========================= GAME CONTROLLER =========================

/**
 * High-level game controller
 * Manages multiple game sessions
 */
class GameController {
    private TicTacToeGame game;
    private final Scanner scanner;

    public GameController() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game controller
     */
    public void start() {
        System.out.println("\n========================================");
        System.out.println("  Tic-Tac-Toe Game - Controller Start  ");
        System.out.println("========================================");

        boolean continueSession = true;

        while (continueSession) {
            GameConfig config = setupGame();
            game = new TicTacToeGame(config);

            // Add observers
            game.addObserver(new ConsoleGameObserver());

            // Start the game
            game.start();

            // Ask if player wants to play again
            continueSession = askPlayAgain();
        }

        System.out.println("\n*** Thank you for playing! Goodbye! ***");
    }

    /**
     * Sets up game configuration
     * @return Game configuration
     */
    private GameConfig setupGame() {
        System.out.println("\n--- Game Setup ---");

        // Get board size
        int boardSize = getBoardSize();

        // Get player names
        System.out.print("Enter Player 1 name (Symbol X): ");
        String player1Name = scanner.nextLine().trim();
        if (player1Name.isEmpty()) player1Name = "Player 1";

        System.out.print("Enter Player 2 name (Symbol O): ");
        String player2Name = scanner.nextLine().trim();
        if (player2Name.isEmpty()) player2Name = "Player 2";

        // Create players
        List<Player> players = Arrays.asList(
            new Player(player1Name, Symbol.X),
            new Player(player2Name, Symbol.O)
        );

        // Build configuration
        return new GameConfig.Builder()
            .setBoardSize(boardSize)
            .setPlayers(players)
            .setWinStrategy(new StandardWinStrategy())
            .setInputHandler(new ConsoleInputHandler())
            .setDisplayHandler(new ConsoleDisplayHandler())
            .build();
    }

    /**
     * Gets board size from user
     * @return Board size
     */
    private int getBoardSize() {
        while (true) {
            try {
                System.out.print("Enter board size (minimum 3, default 3): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    return 3; // Default size
                }

                int size = Integer.parseInt(input);
                if (size < 3) {
                    System.out.println("Board size must be at least 3. Try again.");
                    continue;
                }
                return size;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Asks if player wants to play again
     * @return true if player wants to play again
     */
    private boolean askPlayAgain() {
        while (true) {
            System.out.print("\nWould you like to play again? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes") || response.equals("y")) {
                return true;
            } else if (response.equals("no") || response.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input! Please enter 'yes' or 'no'.");
            }
        }
    }
}

// ========================= MAIN CLASS =========================

public class Main {
    /**
     * Main entry point for the Tic-Tac-Toe game
     * 
     * Design Highlights:
     * - Modular architecture with clear separation of concerns
     * - Strategy Pattern for win checking algorithms
     * - Builder Pattern for flexible game configuration
     * - Observer Pattern for event handling
     * - Supports variable board sizes
     * - Extensible for future enhancements (AI players, different win conditions, etc.)
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            runDemo();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Runs an automated demo of the Tic-Tac-Toe game
     * Demonstrates all features without requiring user input
     */
    private static void runDemo() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  TIC-TAC-TOE GAME DEMO");
        System.out.println("              Low-Level Design Implementation");
        System.out.println("=".repeat(70));
        System.out.println("\nThis demo showcases:");
        System.out.println("  - SOLID Principles (SRP, OCP, LSP, ISP, DIP)");
        System.out.println("  - Design Patterns (Strategy, Builder, Observer)");
        System.out.println("  - OOP Concepts (Encapsulation, Abstraction, Polymorphism)");
        System.out.println("  - Configurable board size");
        System.out.println("  - Win and draw detection");
        System.out.println("  - Invalid move handling");
        System.out.println("\n" + "=".repeat(70));

        // Demo 1: Player X wins with a row
        System.out.println("\n\n### DEMO 1: Player X Wins (Row Victory) ###\n");
        runGame1();

        pause(2000);

        // Demo 2: Player O wins with a diagonal
        System.out.println("\n\n### DEMO 2: Player O Wins (Diagonal Victory) ###\n");
        runGame2();

        pause(2000);

        // Demo 3: Draw game
        System.out.println("\n\n### DEMO 3: Draw Game ###\n");
        runGame3();

        pause(2000);

        // Demo 4: 4x4 board
        System.out.println("\n\n### DEMO 4: 4x4 Board (Column Victory) ###\n");
        runGame4();

        System.out.println("\n\n" + "=".repeat(70));
        System.out.println("                     DEMO COMPLETED!");
        System.out.println("=".repeat(70));
        System.out.println("\nAll features demonstrated successfully!");
        System.out.println("The design is extensible and follows best practices.");
        System.out.println("\n" + "=".repeat(70));
    }

    /**
     * Demo 1: Player X wins with a horizontal row
     */
    private static void runGame1() {
        Queue<Move> moves = new LinkedList<>();
        // X plays top row, O plays middle row
        moves.add(new Move(0, 0)); // X
        moves.add(new Move(1, 0)); // O
        moves.add(new Move(0, 1)); // X
        moves.add(new Move(1, 1)); // O
        moves.add(new Move(0, 2)); // X wins!

        GameConfig config = new GameConfig.Builder()
            .setBoardSize(3)
            .setPlayers(Arrays.asList(
                new Player("Alice", Symbol.X),
                new Player("Bob", Symbol.O)
            ))
            .setWinStrategy(new StandardWinStrategy())
            .setInputHandler(new DemoInputHandler(moves))
            .setDisplayHandler(new ConsoleDisplayHandler())
            .build();

        TicTacToeGame game = new TicTacToeGame(config);
        game.addObserver(new ConsoleGameObserver());
        game.start();
    }

    /**
     * Demo 2: Player O wins with a diagonal
     */
    private static void runGame2() {
        Queue<Move> moves = new LinkedList<>();
        // O wins with main diagonal
        moves.add(new Move(0, 1)); // X
        moves.add(new Move(0, 0)); // O
        moves.add(new Move(1, 0)); // X
        moves.add(new Move(1, 1)); // O
        moves.add(new Move(2, 1)); // X
        moves.add(new Move(2, 2)); // O wins!

        GameConfig config = new GameConfig.Builder()
            .setBoardSize(3)
            .setPlayers(Arrays.asList(
                new Player("Charlie", Symbol.X),
                new Player("Diana", Symbol.O)
            ))
            .setWinStrategy(new StandardWinStrategy())
            .setInputHandler(new DemoInputHandler(moves))
            .setDisplayHandler(new ConsoleDisplayHandler())
            .build();

        TicTacToeGame game = new TicTacToeGame(config);
        game.addObserver(new ConsoleGameObserver());
        game.start();
    }

    /**
     * Demo 3: Draw game (all cells filled, no winner)
     */
    private static void runGame3() {
        Queue<Move> moves = new LinkedList<>();
        // Draw game
        moves.add(new Move(0, 0)); // X
        moves.add(new Move(0, 1)); // O
        moves.add(new Move(0, 2)); // X
        moves.add(new Move(1, 1)); // O
        moves.add(new Move(1, 0)); // X
        moves.add(new Move(2, 0)); // O
        moves.add(new Move(1, 2)); // X
        moves.add(new Move(2, 2)); // O
        moves.add(new Move(2, 1)); // X - Draw!

        GameConfig config = new GameConfig.Builder()
            .setBoardSize(3)
            .setPlayers(Arrays.asList(
                new Player("Eve", Symbol.X),
                new Player("Frank", Symbol.O)
            ))
            .setWinStrategy(new StandardWinStrategy())
            .setInputHandler(new DemoInputHandler(moves))
            .setDisplayHandler(new ConsoleDisplayHandler())
            .build();

        TicTacToeGame game = new TicTacToeGame(config);
        game.addObserver(new ConsoleGameObserver());
        game.start();
    }

    /**
     * Demo 4: 4x4 board with column victory
     */
    private static void runGame4() {
        Queue<Move> moves = new LinkedList<>();
        // X wins with first column on 4x4 board
        moves.add(new Move(0, 0)); // X
        moves.add(new Move(0, 1)); // O
        moves.add(new Move(1, 0)); // X
        moves.add(new Move(1, 1)); // O
        moves.add(new Move(2, 0)); // X
        moves.add(new Move(2, 1)); // O
        moves.add(new Move(3, 0)); // X wins!

        GameConfig config = new GameConfig.Builder()
            .setBoardSize(4)
            .setPlayers(Arrays.asList(
                new Player("Grace", Symbol.X),
                new Player("Henry", Symbol.O)
            ))
            .setWinStrategy(new StandardWinStrategy())
            .setInputHandler(new DemoInputHandler(moves))
            .setDisplayHandler(new ConsoleDisplayHandler())
            .build();

        TicTacToeGame game = new TicTacToeGame(config);
        game.addObserver(new ConsoleGameObserver());
        game.start();
    }

    /**
     * Pauses execution for better demo visualization
     */
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}