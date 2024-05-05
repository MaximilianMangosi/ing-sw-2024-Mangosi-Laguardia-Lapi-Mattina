package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Card implements Serializable {
    private Resource NW;
    private Resource NE;
    private Resource SW;
    private Resource SE;

    private boolean isFront;

    /**
     * Card constructor
     * @param NW resource in left-up angle
     * @param NE resource in right-up angle
     * @param SW resource in left-down angle
     * @param SE resource in right-down angle
     */
     public Card(Resource NW,Resource NE,Resource SW,Resource SE){
         this.NW=NW;
         this.NE=NE;
         this.SW=SW;
         this.SE=SE;
     }

     /**
     * Resource getter
     * @author Giuseppe Laguardia
     * @param angle string that determine the desired angle's position, accepted values:(NW,SW,NE,SE)
     * @return the Resource in position angle. If the angle isn't accepted returns null
     */
     public Resource getResource(String angle) {
         if(angle.equals("NW"))
             return NW;
         if(angle.equals("SW"))
             return SW;
         if (angle.equals("NE"))
             return NE;
         if(angle.equals("SE"))
             return SE;
         return null;
     }

    /**
     * @author Maximilian Mangosi
     * @param angle selected angle
     * @return a string format the cardinal position of the selected angle
     */
     public String getResourceName(Resource angle) {
         String temp;
         if(angle == NW) {
            temp = "NW";
            return temp;
         }
         if(angle == SW) {
            temp = "SW";
            return temp;
         }
         if(angle == NE) {
            temp = "NE";
            return temp;
         }
         if(angle == SE) {
            temp = "SE";
            return temp;
         }
         return null;
     }

     /**
      * @author Maximilian Mangosi
      * Returns a list with all the resources present in the card
      * @return idem
      */
    public List<Resource> getCardResources() {
        //cycle through the card angles and return a list with all the resources in the card
        List<Resource> cardResources = new ArrayList<>();
        String[] angles ={"NW","NE","SW","SE"};

        for (String angle: angles)
            if(getResource(angle)!=null && !getResource(angle).isEmpty())
                cardResources.add(getResource(angle));

        return cardResources;
    }

    public void setIsFront(boolean b) {
        isFront = b;
    }

    public boolean IsFront() {
        return isFront;
    }

    public Reign getReign() {
        return null;
    }

    public boolean checkRequirements(HashMap<Resource, Integer> resourceCounters) {
        return true;
    }

    public void addPoints(Player currentPlayer) {

    }
    public void setAngle(Reign reign, String angle){
        if(angle.equals("NW"))
            this.NW = reign;
        if(angle.equals("SW"))
            this.SW=reign;
        if (angle.equals("NE"))
            this.NE = reign;
        if(angle.equals("SE"))
            this.SE=reign;
    }

    public int getPoints() {
        return 0;
    }

    /**
     * default implementation of getRequirements
     * @author Giorgio Mattina
     * @return an empty hash map
     */
    public HashMap<Reign, Integer> getRequirements() {
        return new HashMap<>();
    }
    public Tool getTool() {
        return null;
    }
    /**
     * Overrides Object.equals(), two Card are equal if they each angle have the same Resource
     * @author Giuseppe Laguardia
     * @param other the Object to compare with
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof Card temp)
            return this.NW==temp.NW && this.NE==temp.NE && this.SW==temp.SW && this.SE== temp.SE;
        return false;

    }
}
