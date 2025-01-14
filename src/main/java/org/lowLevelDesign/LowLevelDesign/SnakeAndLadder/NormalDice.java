package org.lowLevelDesign.LowLevelDesign.SnakeAndLadder;

import java.util.Random;

 interface DiceStrategy {
    int rollDice();
}

public class NormalDice implements DiceStrategy {
    private Random random;

    public NormalDice() {
        this.random = new Random();
    }

    @Override
    public int rollDice() {
        return random.nextInt(6) + 1;
    }
}

// The Loaded Dice is an example of a biased dice that doesn't generate numbers randomly.
// Instead, it skews the probabilities, typically favoring higher numbers, making it "loaded" to produce certain outcomes more frequently.
class LoadedDice implements DiceStrategy {
    private Random random;

    public LoadedDice() {
        this.random = new Random();
    }

    @Override
    public int rollDice() {
        return random.nextInt(3) + 4;  // Rolls between 4 and 6
    }
}

