package org.example;

import java.util.*;

/**
 * Snake and Ladder Game - Low Level Design
 * Following SOLID principles and Design Patterns
 */

// ========================= ENUMS =========================

/**
 * Enum representing the type of board entity
 */
enum EntityType {
    SNAKE,
    LADDER,
    NORMAL
}

/**
 * Enum representing the current state of the game
 */
enum GameState {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

// ========================= INTERFACES =========================

/**
 * Strategy Pattern: Interface for dice rolling mechanism
 * Allows different dice implementations (normal, weighted, etc.)
 */
interface DiceStrategy {
    int roll();
}

/**
 * Interface for board entities (Snake/Ladder)
 * Follows Interface Segregation Principle
 */
interface BoardEntity {
    int getStart();
    int getEnd();
    EntityType getType();
    String getDescription();
}

/**
 * Observer Pattern: Interface for game event observers
 * Allows monitoring game events (moves, wins, etc.)
 */
interface GameObserver {
    void onPlayerMove(Player player, int from, int to, int diceRoll);
    void onSnakeEncounter(Player player, int from, int to);
    void onLadderEncounter(Player player, int from, int to);
    void onPlayerWin(Player player);
}

// ========================= MODELS =========================

/**
 * Represents a Snake on the board
 * Encapsulates snake-specific behavior
 */
class Snake implements BoardEntity {
    private final int head;
    private final int tail;

    public Snake(int head, int tail) {
        if (head <= tail) {
            throw new IllegalArgumentException("Snake head must be greater than tail");
        }
        this.head = head;
        this.tail = tail;
    }

    @Override
    public int getStart() {
        return head;
    }

    @Override
    public int getEnd() {
        return tail;
    }

    @Override
    public EntityType getType() {
        return EntityType.SNAKE;
    }

    @Override
    public String getDescription() {
        return String.format("Snake from %d to %d", head, tail);
    }

    public int getHead() {
        return head;
    }

    public int getTail() {
        return tail;
    }
}

/**
 * Represents a Ladder on the board
 * Encapsulates ladder-specific behavior
 */
class Ladder implements BoardEntity {
    private final int bottom;
    private final int top;

    public Ladder(int bottom, int top) {
        if (bottom >= top) {
            throw new IllegalArgumentException("Ladder bottom must be less than top");
        }
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public int getStart() {
        return bottom;
    }

    @Override
    public int getEnd() {
        return top;
    }

    @Override
    public EntityType getType() {
        return EntityType.LADDER;
    }

    @Override
    public String getDescription() {
        return String.format("Ladder from %d to %d", bottom, top);
    }

    public int getBottom() {
        return bottom;
    }

    public int getTop() {
        return top;
    }
}

/**
 * Represents a Player in the game
 * Encapsulates player state and behavior
 */
class Player {
    private final String playerId;
    private final String name;
    private int currentPosition;

    public Player(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
        this.currentPosition = 0; // Players start at position 0 (before square 1)
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) at position %d", name, playerId, currentPosition);
    }
}

/**
 * Represents the game board
 * Single Responsibility: Manages board state and entities
 */
class Board {
    private final int size;
    private final Map<Integer, BoardEntity> entities; // Maps position to Snake/Ladder

    public Board(int size) {
        if (size < 10) {
            throw new IllegalArgumentException("Board size must be at least 10");
        }
        this.size = size;
        this.entities = new HashMap<>();
    }

    /**
     * Adds a snake to the board
     */
    public void addSnake(Snake snake) {
        validatePosition(snake.getHead());
        validatePosition(snake.getTail());
        if (entities.containsKey(snake.getHead())) {
            throw new IllegalArgumentException("Position " + snake.getHead() + " already has an entity");
        }
        entities.put(snake.getHead(), snake);
    }

    /**
     * Adds a ladder to the board
     */
    public void addLadder(Ladder ladder) {
        validatePosition(ladder.getBottom());
        validatePosition(ladder.getTop());
        if (entities.containsKey(ladder.getBottom())) {
            throw new IllegalArgumentException("Position " + ladder.getBottom() + " already has an entity");
        }
        entities.put(ladder.getBottom(), ladder);
    }

    /**
     * Gets the final position after considering snakes and ladders
     */
    public int getFinalPosition(int position) {
        if (!entities.containsKey(position)) {
            return position;
        }
        return entities.get(position).getEnd();
    }

    /**
     * Checks if a position has a snake or ladder
     */
    public BoardEntity getEntityAt(int position) {
        return entities.get(position);
    }

    public int getSize() {
        return size;
    }

    public Map<Integer, BoardEntity> getEntities() {
        return new HashMap<>(entities); // Return copy for encapsulation
    }

    private void validatePosition(int position) {
        if (position < 1 || position > size) {
            throw new IllegalArgumentException("Position must be between 1 and " + size);
        }
        if (position == 1 || position == size) {
            throw new IllegalArgumentException("Cannot place entity at start or end position");
        }
    }
}

// ========================= STRATEGIES =========================

/**
 * Standard dice implementation (1-6)
 * Strategy Pattern implementation
 */
class StandardDice implements DiceStrategy {
    private final Random random;
    private final int sides;

    public StandardDice() {
        this(6);
    }

    public StandardDice(int sides) {
        this.random = new Random();
        this.sides = sides;
    }

    @Override
    public int roll() {
        return random.nextInt(sides) + 1;
    }
}

/**
 * Weighted dice for testing purposes
 * Can be configured to return specific values
 */
class WeightedDice implements DiceStrategy {
    private final Queue<Integer> predefinedRolls;
    private final DiceStrategy fallbackDice;

    public WeightedDice(List<Integer> predefinedRolls) {
        this.predefinedRolls = new LinkedList<>(predefinedRolls);
        this.fallbackDice = new StandardDice();
    }

    @Override
    public int roll() {
        if (!predefinedRolls.isEmpty()) {
            return predefinedRolls.poll();
        }
        return fallbackDice.roll();
    }
}

// ========================= OBSERVERS =========================

/**
 * Console logger for game events
 * Observer Pattern implementation
 */
class ConsoleGameObserver implements GameObserver {
    @Override
    public void onPlayerMove(Player player, int from, int to, int diceRoll) {
        System.out.println(String.format("[MOVE] %s rolled %d and moved from %d to %d",
                player.getName(), diceRoll, from, to));
    }

    @Override
    public void onSnakeEncounter(Player player, int from, int to) {
        System.out.println(String.format("[SNAKE] %s encountered a snake! Slid down from %d to %d",
                player.getName(), from, to));
    }

    @Override
    public void onLadderEncounter(Player player, int from, int to) {
        System.out.println(String.format("[LADDER] %s found a ladder! Climbed up from %d to %d",
                player.getName(), from, to));
    }

    @Override
    public void onPlayerWin(Player player) {
        System.out.println(String.format("\n*** [WINNER] %s has won the game! ***\n",
                player.getName()));
    }
}

// ========================= GAME ENGINE =========================

/**
 * Main game controller
 * Follows Single Responsibility Principle: Manages game flow
 * Open/Closed Principle: Open for extension via strategies and observers
 */
class SnakeAndLadderGame {
    private final Board board;
    private final DiceStrategy dice;
    private final List<Player> players;
    private final Queue<Player> turnQueue;
    private final List<GameObserver> observers;
    private GameState gameState;
    private Player winner;

    /**
     * Builder Pattern for game construction
     */
    public static class Builder {
        private int boardSize = 100;
        private DiceStrategy dice = new StandardDice();
        private List<Player> players = new ArrayList<>();
        private List<Snake> snakes = new ArrayList<>();
        private List<Ladder> ladders = new ArrayList<>();
        private List<GameObserver> observers = new ArrayList<>();

        public Builder withBoardSize(int size) {
            this.boardSize = size;
            return this;
        }

        public Builder withDice(DiceStrategy dice) {
            this.dice = dice;
            return this;
        }

        public Builder addPlayer(Player player) {
            this.players.add(player);
            return this;
        }

        public Builder addSnake(Snake snake) {
            this.snakes.add(snake);
            return this;
        }

        public Builder addLadder(Ladder ladder) {
            this.ladders.add(ladder);
            return this;
        }

        public Builder addObserver(GameObserver observer) {
            this.observers.add(observer);
            return this;
        }

        public SnakeAndLadderGame build() {
            if (players.isEmpty()) {
                throw new IllegalStateException("At least one player is required");
            }
            return new SnakeAndLadderGame(this);
        }
    }

    private SnakeAndLadderGame(Builder builder) {
        this.board = new Board(builder.boardSize);
        this.dice = builder.dice;
        this.players = new ArrayList<>(builder.players);
        this.turnQueue = new LinkedList<>(builder.players);
        this.observers = new ArrayList<>(builder.observers);
        this.gameState = GameState.NOT_STARTED;

        // Initialize board with snakes and ladders
        for (Snake snake : builder.snakes) {
            board.addSnake(snake);
        }
        for (Ladder ladder : builder.ladders) {
            board.addLadder(ladder);
        }
    }

    /**
     * Starts the game
     */
    public void start() {
        if (gameState != GameState.NOT_STARTED) {
            throw new IllegalStateException("Game has already started");
        }
        gameState = GameState.IN_PROGRESS;
        System.out.println("\n========================================");
        System.out.println("    SNAKE AND LADDER GAME STARTED");
        System.out.println("========================================");
        System.out.println("Board Size: " + board.getSize());
        System.out.println("Players: " + players.size());
        System.out.println("Snakes: " + countEntities(EntityType.SNAKE));
        System.out.println("Ladders: " + countEntities(EntityType.LADDER));
        System.out.println("========================================\n");
    }

    /**
     * Plays the game until there's a winner
     */
    public void play() {
        if (gameState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        while (gameState == GameState.IN_PROGRESS) {
            playTurn();
        }
    }

    /**
     * Plays a single turn
     */
    public void playTurn() {
        if (gameState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        Player currentPlayer = turnQueue.poll();
        int diceRoll = dice.roll();
        movePlayer(currentPlayer, diceRoll);

        // Check for win condition
        if (currentPlayer.getCurrentPosition() == board.getSize()) {
            gameState = GameState.FINISHED;
            winner = currentPlayer;
            notifyPlayerWin(currentPlayer);
        } else {
            turnQueue.offer(currentPlayer); // Add player back to queue
        }
    }

    /**
     * Moves a player based on dice roll
     */
    private void movePlayer(Player player, int diceRoll) {
        int startPosition = player.getCurrentPosition();
        int newPosition = startPosition + diceRoll;

        // Check if move overshoots the board
        if (newPosition > board.getSize()) {
            System.out.println(String.format("%s rolled %d but cannot move (would overshoot position %d)",
                    player.getName(), diceRoll, board.getSize()));
            return;
        }

        // Move player to new position
        player.setCurrentPosition(newPosition);
        notifyPlayerMove(player, startPosition, newPosition, diceRoll);

        // Check for snake or ladder
        BoardEntity entity = board.getEntityAt(newPosition);
        if (entity != null) {
            int finalPosition = entity.getEnd();
            player.setCurrentPosition(finalPosition);

            if (entity.getType() == EntityType.SNAKE) {
                notifySnakeEncounter(player, newPosition, finalPosition);
            } else if (entity.getType() == EntityType.LADDER) {
                notifyLadderEncounter(player, newPosition, finalPosition);
            }
        }
    }

    /**
     * Gets the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Gets the winner (if game is finished)
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Gets all players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Displays current game status
     */
    public void displayStatus() {
        System.out.println("\n--- Current Game Status ---");
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println("---------------------------\n");
    }

    // Observer notification methods
    private void notifyPlayerMove(Player player, int from, int to, int diceRoll) {
        for (GameObserver observer : observers) {
            observer.onPlayerMove(player, from, to, diceRoll);
        }
    }

    private void notifySnakeEncounter(Player player, int from, int to) {
        for (GameObserver observer : observers) {
            observer.onSnakeEncounter(player, from, to);
        }
    }

    private void notifyLadderEncounter(Player player, int from, int to) {
        for (GameObserver observer : observers) {
            observer.onLadderEncounter(player, from, to);
        }
    }

    private void notifyPlayerWin(Player player) {
        for (GameObserver observer : observers) {
            observer.onPlayerWin(player);
        }
    }

    private long countEntities(EntityType type) {
        return board.getEntities().values().stream()
                .filter(entity -> entity.getType() == type)
                .count();
    }
}

// ========================= MAIN CLASS =========================

/**
 * Main class to demonstrate the Snake and Ladder game
 */
public class Main {
    public static void main(String[] args) {
        // Demo 1: Standard game with 2 players
        System.out.println("\n========================================");
        System.out.println("         DEMO 1: STANDARD GAME          ");
        System.out.println("========================================\n");
        runStandardGame();

        // Demo 2: Game with 3 players
        System.out.println("\n\n========================================");
        System.out.println("      DEMO 2: THREE PLAYER GAME         ");
        System.out.println("========================================\n");
        runThreePlayerGame();

        // Demo 3: Custom board size
        System.out.println("\n\n========================================");
        System.out.println("       DEMO 3: CUSTOM BOARD SIZE        ");
        System.out.println("========================================\n");
        runCustomBoardGame();
    }

    /**
     * Demo 1: Standard 100-square game with 2 players
     */
    private static void runStandardGame() {
        SnakeAndLadderGame game = new SnakeAndLadderGame.Builder()
                .withBoardSize(100)
                .addPlayer(new Player("P1", "Alice"))
                .addPlayer(new Player("P2", "Bob"))
                // Add snakes
                .addSnake(new Snake(99, 54))
                .addSnake(new Snake(70, 55))
                .addSnake(new Snake(52, 42))
                .addSnake(new Snake(25, 2))
                .addSnake(new Snake(95, 72))
                // Add ladders
                .addLadder(new Ladder(6, 25))
                .addLadder(new Ladder(11, 40))
                .addLadder(new Ladder(60, 85))
                .addLadder(new Ladder(46, 90))
                .addLadder(new Ladder(17, 69))
                // Add observer
                .addObserver(new ConsoleGameObserver())
                .build();

        game.start();
        game.play();
        
        System.out.println("\nFinal Status:");
        game.displayStatus();
        System.out.println("Winner: " + game.getWinner().getName());
    }

    /**
     * Demo 2: Game with 3 players
     */
    private static void runThreePlayerGame() {
        SnakeAndLadderGame game = new SnakeAndLadderGame.Builder()
                .withBoardSize(100)
                .addPlayer(new Player("P1", "Charlie"))
                .addPlayer(new Player("P2", "Diana"))
                .addPlayer(new Player("P3", "Eve"))
                // Add snakes
                .addSnake(new Snake(98, 79))
                .addSnake(new Snake(87, 24))
                .addSnake(new Snake(66, 45))
                .addSnake(new Snake(51, 9))
                // Add ladders
                .addLadder(new Ladder(3, 38))
                .addLadder(new Ladder(8, 30))
                .addLadder(new Ladder(28, 76))
                .addLadder(new Ladder(58, 77))
                .addLadder(new Ladder(75, 86))
                // Add observer
                .addObserver(new ConsoleGameObserver())
                .build();

        game.start();
        game.play();
        
        System.out.println("\nFinal Status:");
        game.displayStatus();
        System.out.println("Winner: " + game.getWinner().getName());
    }

    /**
     * Demo 3: Custom board size (50 squares)
     */
    private static void runCustomBoardGame() {
        SnakeAndLadderGame game = new SnakeAndLadderGame.Builder()
                .withBoardSize(50)
                .addPlayer(new Player("P1", "Frank"))
                .addPlayer(new Player("P2", "Grace"))
                // Add snakes
                .addSnake(new Snake(47, 26))
                .addSnake(new Snake(38, 15))
                .addSnake(new Snake(28, 10))
                // Add ladders
                .addLadder(new Ladder(4, 14))
                .addLadder(new Ladder(9, 31))
                .addLadder(new Ladder(20, 42))
                .addLadder(new Ladder(33, 46))
                // Add observer
                .addObserver(new ConsoleGameObserver())
                .build();

        game.start();
        game.play();
        
        System.out.println("\nFinal Status:");
        game.displayStatus();
        System.out.println("Winner: " + game.getWinner().getName());
    }
}