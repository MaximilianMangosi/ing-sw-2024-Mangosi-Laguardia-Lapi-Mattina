package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

public class IdenticalGoal extends Goal {
    private final Resource resource;
    private final int numOfResource;

    public IdenticalGoal(int points, Resource resource, int numOfResource) {
        super(points);
        this.resource = resource;
        this.numOfResource = numOfResource;
    }

    public int getNumOfResource(){
        return numOfResource;
    }
    public Resource getResource() {
        return resource;
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal "tris of resources"
     * @param player player
     * @return the points for the goal
     */
    public int calculateGoal( Player player){
        int totNum = player.getResourceCounter(resource) / numOfResource;
        return  totNum * points;
    }
}
