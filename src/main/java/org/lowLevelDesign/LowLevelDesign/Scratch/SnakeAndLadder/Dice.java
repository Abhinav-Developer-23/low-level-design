package org.lowLevelDesign.LowLevelDesign.Scratch.SnakeAndLadder;

public class Dice {

    Integer maxNumber;


    public Integer rollDice()
    {
        Integer number= (int) ((Math.random()*10)%maxNumber)+1;

        return number;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }
}
