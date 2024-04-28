package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public String getUIDescription() {
        return "Identical Goal: " + points + " points every " + numOfResource +" identical Resources visible on the Field";
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal "tris of resources"
     * @param player player
     * @return the points for the goal
     */
    public int calculateGoal( Player player){

        List<Integer> numResourcesPerCard = new ArrayList<Integer>();

        for(Map.Entry<Coordinates, Card> card : player.getField().entrySet()){
            List<Resource> currentResources = card.getValue().getCardResources();
            int numValidResources = (int)currentResources.stream().filter(c -> c.equals(resource)).count();
            numResourcesPerCard.add(numValidResources);
        }
        numResourcesPerCard = numResourcesPerCard.stream().sorted((o1, o2) -> o1 > o2 ? o1 : o2).collect(Collectors.toList());

        int counterRes = 0;
        int totIdentical = 0;
        for(int numRes : numResourcesPerCard){
            counterRes += numRes;
            if(counterRes >= numOfResource){
                totIdentical ++;
                counterRes = 0;
            }
        }

        return  totIdentical * points;
    }
}
