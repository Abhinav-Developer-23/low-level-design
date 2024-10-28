package org.lowLevelDesign.LowLevelDesign.Scratch.SnakeAndLadder;

import java.util.HashMap;
import java.util.Objects;

public class Board {

    private Integer size;

    private HashMap<Integer,Integer> ladders;
    private HashMap<Integer,Integer> snakes;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public HashMap<Integer,Integer> getLadders() {
        return ladders;
    }

    public void setLadders(HashMap<Integer,Integer> ladders) {
        this.ladders = ladders;
    }

    public HashMap<Integer,Integer> getSnakes() {
        return snakes;
    }

    public void setSnakes(HashMap<Integer,Integer> snakes) {
        this.snakes = snakes;
    }


    public Integer checkLadderOrSnake(Integer currentPosition)
    {
        if(Objects.nonNull(this.ladders.get(currentPosition)))
        {
            return ladders.get(currentPosition);

        }
        if(Objects.nonNull(this.snakes.get(currentPosition)))
        {
            return snakes.get(currentPosition);
        }
        return currentPosition;

    }
}
