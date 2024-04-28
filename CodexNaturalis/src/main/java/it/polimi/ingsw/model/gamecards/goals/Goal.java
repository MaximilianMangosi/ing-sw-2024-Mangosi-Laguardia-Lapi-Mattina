package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamelogic.Player;

public abstract class Goal {
    protected int points;
     abstract public String getUIDescription();

    public Goal(int points) {
        this.points = points;
    }

    public abstract int calculateGoal( Player player);
}
