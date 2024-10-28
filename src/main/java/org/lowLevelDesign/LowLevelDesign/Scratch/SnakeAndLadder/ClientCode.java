package org.lowLevelDesign.LowLevelDesign.Scratch.SnakeAndLadder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientCode {

    public static void main(String[] args) {

        Board board = new Board();

        // Set the size of the board
        board.setSize(100); // Example board size

        // Set the ladders
        HashMap<Integer, Integer> ladders = new HashMap<>();
        ladders.put(2, 38); // Ladder from 2 to 38
        ladders.put(7, 14); // Ladder from 7 to 14
        ladders.put(8, 31); // Ladder from 8 to 31
        board.setLadders(ladders);

        // Set the snakes
        HashMap<Integer, Integer> snakes = new HashMap<>();
        snakes.put(16, 6); // Snake from 16 to 6
        snakes.put(47, 26); // Snake from 47 to 26
        snakes.put(49, 11); // Snake from 49 to 11
        board.setSnakes(snakes);

        Dice dice=new Dice();
        dice.setMaxNumber(10);


        List<Player> players = new ArrayList<>();

        // Create and set up five players
        for (int i = 1; i <= 5; i++) {
            Player player = new Player();
            player.setId(i);
            player.setName("Player " + i); // Assign a default name like "Player 1", "Player 2", etc.
            player.setCurrentPosition(0); // Starting position for each player

            // Add player to the list
            players.add(player);
        }

        Game game=new Game();
        game.setDice(dice);
        game.setBoard(board);
        game.setPlayerList(players);
        game.startGame();


    }
}
