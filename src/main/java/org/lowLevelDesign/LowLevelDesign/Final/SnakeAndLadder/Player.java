package org.lowLevelDesign.LowLevelDesign.Final.SnakeAndLadder;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String message);
}

public class Player implements Observer {
    private String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void update(String message) {
        System.out.println(name + ": " + message);
    }
}

 class Game {
    private List<Player> players;
    private int currentPlayerIndex;

    public Game() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void notifyPlayers(String message) {
        for (Player player : players) {
            player.update(message);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
