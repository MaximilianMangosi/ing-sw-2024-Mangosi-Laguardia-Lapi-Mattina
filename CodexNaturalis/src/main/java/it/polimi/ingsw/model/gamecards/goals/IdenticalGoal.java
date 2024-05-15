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

    /**
     * @author Riccardo Lapi
     * @param points of the goal
     * @param resource of the goal
     * @param numOfResource of the goal
     */
    public IdenticalGoal(int points, Resource resource, int numOfResource, int id) {
        super(points, id);
        this.resource = resource;
        this.numOfResource = numOfResource;
    }

    /**
     * @author Riccardo Lapi
     * @return the number of resources
     */
    public int getNumOfResource(){
        return numOfResource;
    }

    /**
     * @author Riccardo Lapi
     * @return resource of the goal
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @author Riccardo Lapi
     * @return the ui description
     */
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
    /**
     * Overrides Object.equals() , two IdenticalGoal are equal if they have the same resource
     * @author Giuseppe Laguardia
     * @param other the Object to compare with this
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof IdenticalGoal temp)
            return this.resource==temp.resource;
        return false;

    }
}
