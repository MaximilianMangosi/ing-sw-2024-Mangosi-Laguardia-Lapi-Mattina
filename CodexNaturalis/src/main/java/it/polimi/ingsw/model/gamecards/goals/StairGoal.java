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

public class StairGoal extends Goal {
    /**
     * @author Riccardo Lapi
     * @param points of the goal
     * @param reign of the goal
     * @param isToLowerRight of the goal
     */
    public StairGoal(int points, Reign reign, boolean isToLowerRight, int id) {
        super(points, id);
        this.reign = reign;
        this.isToLowerRight = isToLowerRight;
    }

    /**
     * @author Riccardo Lapi
     * @return the reign
     */
    public Reign getReign() {
        return reign;
    }

    private final Reign reign;

    /**
     * @author Riccardo Lapi
     * @return isToLowerRight
     */
    public boolean isToLowerRight() {
        return isToLowerRight;
    }

    private final boolean isToLowerRight;

    /**
     * @author Riccardo Lapi
     * @return the ui descriptor
     */
    @Override
    public String getUIDescription() {
        return "Stair Goal: " + points + " points every Stair with Reign " + reign.toString();
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal of 3 cards in a "stair" position
     * @param player player
     * @return the points for the given goal
     */
    public int calculateGoal(Player player){

        Map<Coordinates, Card> field = player.getField();
        List<Coordinates> usedCards = new ArrayList<>();
        int numOfStairs = 0;

        int modY = isToLowerRight() ? -1 : 1;

        Reign goalReign = getReign();

        for(Map.Entry<Coordinates, Card> card : field.entrySet()){
            Reign currentReign = card.getValue().getReign();
            if(!goalReign.equals(currentReign)) continue;
            Coordinates current = card.getKey();

            if(usedCards.contains(current)) continue;

            Coordinates right = new Coordinates(current.x + 1, current.y + modY);
            Coordinates left = new Coordinates(current.x - 1, current.y - modY);

            boolean isStairs = field.containsKey(right) && field.containsKey(left);
            if(isStairs){
                usedCards.add(current);
                usedCards.add(right);
                usedCards.add(left);
                numOfStairs++;
            }
        }

        return numOfStairs * points;
    }
    /**
     * Overrides Object.equals() , two StairGoal are equal if they have the same reign
     * @author Giuseppe Laguardia
     * @param other the Object to compare with this
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof StairGoal temp)
            return this.reign==temp.reign;
        return false;

    }
}
