package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistinctGoal extends Goal {

    @Override
    public String getUIDescription() {
        return "Distinct Goal: " + points + " points every 3 distinct Tool visible on the Field";
    }

    public DistinctGoal(int points, int id) {
        super(points, id);
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal "3 different resources"
     * @param player player
     * @return the points for the goal
     */
    public int calculateGoal( Player player){
        List<Resource> validResources = new ArrayList<Resource>();
        validResources.add(Tool.FEATHER);
        validResources.add(Tool.SCROLL);
        validResources.add(Tool.PHIAL);

        int totNum = player.getResourceCounters().entrySet()
                .stream()
                .filter(entry -> validResources.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .min(Integer::compareTo)
                .orElse(0);

        return  totNum * points;
    }
    /**
     * Overrides Object.equals() , two DistinctGoal are equal if they have the same points
     * @author Giuseppe Laguardia
     * @param other the Object with which to compare
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof DistinctGoal temp)
            return this.points==temp.points;
        return false;

    }
}
