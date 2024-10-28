package org.lowLevelDesign.LowLevelDesign.Scratch.SnakeAndLadder;

public class Player {

    Integer Id;
    String Name;
    Integer currentPosition=0;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }
}
