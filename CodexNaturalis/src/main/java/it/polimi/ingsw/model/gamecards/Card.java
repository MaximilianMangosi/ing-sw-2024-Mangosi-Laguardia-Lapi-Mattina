package it.polimi.ingsw.model.gamecards;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class Card {
    private Angle NW;
    private Angle NE;
    private Angle SW;
    private Angle SE;
    public void setIsFront(boolean b) {
    }

    public void setAngleCovered(String angleToBeCovered) {

    }

    public Resource getResource(String angleToBeCovered) {
        Angle angle = getAngle(angleToBeCovered);
        if (angle!=null){
            return angle.getResource();
        }
        throw (new NoSuchElementException());
    }

    /**
     * Angle getter
     * @author Giuseppe Laguardia
     * @param x string that determine the desired angle's position
     * @return the Angle in position x
     */
    public Angle getAngle(String x){
        if(x.equals("NE"))
            return new Angle(NE);
        if(x.equals("NW"))
            return new Angle(NW);
        if(x.equals("SE"))
            return new Angle(SE);
        if(x.equals("SW"))
            return new Angle(SW);
        else
            return null;
    }
    /**
     * @author Maximilian Mangosi
     * Retourns a list with all the resources present in the card
     * @return idem
     */
    public List<Resource> getCardResources() {
        //cycle through the card angles and return a list with all the resources in the card
        List<Resource> cardResources = new ArrayList<>();
        if(getAngle("NW")!=null && !(getAngle("NW").isCovered())){
            cardResources.add(getAngle("NW").getResource());
        }
        if(getAngle("SW")!=null && !(getAngle("SW").isCovered())){
            cardResources.add(getAngle("SW").getResource());
        }
        if(getAngle("NE")!=null && !(getAngle("NE").isCovered())){
            cardResources.add(getAngle("NE").getResource());
        }
        if(getAngle("SE")!=null && !(getAngle("SE").isCovered())){
            cardResources.add(getAngle("SE").getResource());
        }

        return cardResources;
    }

}
