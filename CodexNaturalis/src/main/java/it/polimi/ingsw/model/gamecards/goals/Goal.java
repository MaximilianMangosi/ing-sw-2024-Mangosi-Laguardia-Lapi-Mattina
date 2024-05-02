package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serializable;

public abstract class Goal implements Serializable {
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
    /**
     * Overrides Object.equals(), two Goal are equal if they have the same points
     * @author Giuseppe Laguardia
     * @param other the Object to compare with this
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof Goal temp)
            return this.points==temp.points;
        return false;

    }
    public int getNumOfResource(){
        return 0;
    }
    public Reign getPrimaryReign(){
        return null;
    }
    public int getPoints(){
        return points;
    }
}
