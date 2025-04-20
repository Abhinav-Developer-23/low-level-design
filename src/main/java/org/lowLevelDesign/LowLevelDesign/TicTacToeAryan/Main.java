package org.lowLevelDesign.LowLevelDesign.TicTacToeAryan;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<PlayerStrategy> strategies = new ArrayList<>();
        strategies.add(new HumanPlayerStrategy("Player X"));
        strategies.add(new HumanPlayerStrategy("Player O"));

        TicTacToeGame game = new TicTacToeGame(3, strategies);
        game.play();
    }
} 