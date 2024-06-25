package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LGoal extends Goal {
    /**
     * @author Riccardo Lapi
     * @param points of the goal
     * @param primaryReign of the goal
     * @param secondaryReign of the goal
     */
    public LGoal(int points, Reign primaryReign, Reign secondaryReign, int id) {
        super(points, id);
        this.primaryReign = primaryReign;
        this.secondaryReign = secondaryReign;
    }

    private final Reign primaryReign;
    private final Reign secondaryReign;

    /**
     * @author Riccardo Lapi
     * @return the primary reign
     */
    public Reign getPrimaryReign() {
        return primaryReign;
    }

    /**
     * @author Riccardo Lapi
     * @return the secondary reign
     */
    public Reign getSecondaryReign() {
        return secondaryReign;
    }

    /**
     * @author Riccardo Lapi
     * @return ui description
     */
    @Override
    public String getUIDescription() {
        return "L Goal: " + points + " points every L with 1" + primaryReign.toString() + " dx or sx and 2 " + secondaryReign.toString();
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal of 3 cards in a "L" position
     * @param player player
     * @return the points for the given goal
     */
    public int calculateGoal(Player player){

        Map<Coordinates, Card> field = player.getField();
      //  List<Coordinates> usedCards = new ArrayList<>();
        int numOfLs = 0;

        int modY, modX;
        if(primaryReign == Reign.PLANTS){
            modX = -1;
            modY = 1;
        }else if(primaryReign == Reign.BUG){
            modX = 1;
            modY = 1;
        }else if(primaryReign == Reign.MUSHROOM){
            modX = -1;
            modY = -1;
        }else{
            modX = 1;
            modY = -1;
        }

        for(Map.Entry<Coordinates, Card> card : field.entrySet()){

            Reign currentReign = card.getValue().getReign();
            if(!primaryReign.equals(currentReign)) continue;
            Coordinates current = card.getKey();

          //  if(usedCards.contains(current)) continue;

            Coordinates secondaryA = new Coordinates(current.x + modX, current.y + modY);
            Coordinates secondaryB = new Coordinates(current.x + modX, current.y + modY*3);

            Card cardA = field.get(secondaryA);
            if(cardA == null) continue;

            Reign AReign = cardA.getReign();

            Card cardB = field.get(secondaryB);
            if(cardB == null) continue;

            Reign BReign = cardB.getReign();

            if(AReign == null || BReign == null) continue;

            boolean isL =
                    //field.containsKey(secondaryA) &&
                    AReign.equals(secondaryReign)
                   // && field.containsKey(secondaryB)
                    && BReign.equals(secondaryReign);

            if(isL){
                /*
                usedCards.add(current);
                usedCards.add(secondaryA);
                usedCards.add(secondaryB);
                 */
                numOfLs++;
            }
        }

        return numOfLs * points;
    }
    /**
     * Overrides Object.equals() , two LGoal are equal if they have the same primaryReign
     * @author Giuseppe Laguardia
     * @param other the Object to compare with this
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof LGoal temp)
            return this.primaryReign==temp.primaryReign;
        return false;

    }
}
