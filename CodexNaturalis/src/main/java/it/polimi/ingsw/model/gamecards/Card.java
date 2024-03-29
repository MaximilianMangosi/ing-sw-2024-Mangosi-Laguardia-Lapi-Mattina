package it.polimi.ingsw.model.gamecards;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class Card {
    private final Resource NW;
    private final Resource NE;
    private final Resource SW;
    private final Resource SE;

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
     * Returns a list with all the resources present in the card
     * @return idem
     */
    public List<Resource> getCardResources() {
        //cycle through the card angles and return a list with all the resources in the card
        List<Resource> cardResources = new ArrayList<>();
        String[] angles ={"NW","SW","NE","SE"};

        for (String angle: angles)
            if(getResource(angle)!=null && !getResource(angle).isEmpty())
                cardResources.add(getResource(angle));

        return cardResources;
    }

    public void setIsFront(boolean b) {
        isFront=b;
    }

    public boolean IsFront() {
        return isFront;
    }
}
