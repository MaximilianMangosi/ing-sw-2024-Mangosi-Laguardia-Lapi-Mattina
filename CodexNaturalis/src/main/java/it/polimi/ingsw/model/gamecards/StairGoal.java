package it.polimi.ingsw.model.gamecards;

public class StairGoal extends Goal{

    public Reign getReign() {
        return reign;
    }

    private Reign reign;

    public boolean isToLowerRight() {
        return isToLowerRight;
    }

    private boolean isToLowerRight;

}
