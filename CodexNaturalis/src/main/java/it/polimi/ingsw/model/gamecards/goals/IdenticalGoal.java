package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Resource;

public class IdenticalGoal extends Goal {
    private Resource resource;
    private int numOfResource;

    public int getNumOfResource(){
        return numOfResource;
    }
    public Resource getResource() {
        return resource;
    }
}
