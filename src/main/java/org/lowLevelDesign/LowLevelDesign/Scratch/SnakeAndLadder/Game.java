package org.lowLevelDesign.LowLevelDesign.Scratch.SnakeAndLadder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {


    private Board board;

    private Dice dice;
    private List<Player> playerList;

    private final Set<Integer> wonPlayers = new HashSet<>();

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void startGame() {


        while (wonPlayers.size() != playerList.size()) {
            for (int i = 0; i < playerList.size(); i++) {
                Player currentPlayer = playerList.get(i);
                //System.out.println("turn of player " + i);

                Integer move = dice.rollDice();
                Integer currentPosition = currentPlayer.getCurrentPosition();
                Integer newPosition = currentPosition + move;

                if (wonPlayers.contains(i)) {
                    continue;
                }
                if (wonPlayers.size() == playerList.size()) {
                    System.out.println("game has ended");
                    break;
                }

                if (newPosition > board.getSize()) {
                   // System.out.println("Current position exceeds max board size , please wait for next turn");
                    continue;
                }
                newPosition = board.checkLadderOrSnake(newPosition);
                currentPlayer.setCurrentPosition(newPosition);

                if (newPosition.equals(board.getSize())) {
                    System.out.println("player " + i + " has won ");
                    wonPlayers.add(i);

                }


            }
        }


    }


}
