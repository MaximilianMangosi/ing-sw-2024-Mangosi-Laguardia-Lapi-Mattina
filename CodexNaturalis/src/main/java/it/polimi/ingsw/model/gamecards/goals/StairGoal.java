package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;

public class StairGoal extends Goal {
    public Reign getReign() {
        return reign;
    }

    private Reign reign;

    public boolean isToLowerRight() {
        return isToLowerRight;
    }

    private boolean isToLowerRight;
}
