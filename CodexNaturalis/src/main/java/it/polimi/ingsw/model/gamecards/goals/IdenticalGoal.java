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
        numResourcesPerCard = reorderList(numResourcesPerCard);

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
     * @author Riccardo lapi
     * @param list of the number of resources per card
     * @return a new list with elements alternated between 2 and 1
     * Reorders a list of integers such that the elements are alternated
     * between 2 and 1. Assumes the list only contains the integers 1 and 2.
     */
    private static List<Integer> reorderList(List<Integer> list) {
        int countOnes = 0;
        int countTwos = 0;

        for (int num : list) {
            if (num == 1) countOnes++;
            else if (num == 2) countTwos++;
        }

        List<Integer> reorderedList = new ArrayList<>();
        while (countOnes > 0 || countTwos > 0) {
            if (countTwos > 0) {
                reorderedList.add(2);
                countTwos--;
            }
            if (countOnes > 0) {
                reorderedList.add(1);
                countOnes--;
            }
        }

        return reorderedList;
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
