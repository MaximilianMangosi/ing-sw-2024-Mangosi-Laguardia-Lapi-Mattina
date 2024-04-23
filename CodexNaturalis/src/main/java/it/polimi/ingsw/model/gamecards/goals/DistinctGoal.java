package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistinctGoal extends Goal {


    public DistinctGoal(int points) {
        super(points);
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
}
