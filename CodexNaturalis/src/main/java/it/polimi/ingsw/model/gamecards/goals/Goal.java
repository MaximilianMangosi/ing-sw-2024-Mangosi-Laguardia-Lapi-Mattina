package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamelogic.Player;

public abstract class Goal {
    protected int points;

    /**
     * @author Riccardo Lapi
     * @return a string that describe the Goal
     */
     abstract public String getUIDescription();

    /**
     * Contractor for the class Goal
     * @author Riccardo Lapi
     * @param points int for the points this goal has
     */
    public Goal(int points) {
        this.points = points;
    }

    /**
     * @author Riccardo Lapi
     * @param player the player whose points need to be calculated
     * @return the calculated point for the player
     */
    public abstract int calculateGoal( Player player);
}
