package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;

public class GoldCard extends Card {
    HashMap<Reign,Integer> requirements;
    Reign reign;
    int points;
    public GoldCard(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign,Integer> requirements) {
        super(NW, NE, SW, SE);
        this.requirements=requirements;
        this.reign=reign;
        this.points=points;
    }

    public int getPoints() {
        return points;
    }

    public HashMap<Reign, Integer> getRequirements() {
        return requirements;
    }

    public Reign getReign() {
        return reign;
    }

    public void addPoints(Player player){
        player.setPoints(player.getPoints() + this.getPoints());
    }
    /**
     * @author Maximilian Mangosi, Giorgio Mattina
     * counts the elements needed for the gold card requirements
     * @param resourceCounter personal player's counter
     * @return returns the value true if requirements are met, false otherwise
     */
    public boolean checkRequirements(HashMap<Resource,Integer> resourceCounter){
        for (Reign reign : requirements.keySet()) {
            if(requirements.get(reign) >= resourceCounter.get(reign)){
                return false;
            }
        }
        return true;

    }
}

