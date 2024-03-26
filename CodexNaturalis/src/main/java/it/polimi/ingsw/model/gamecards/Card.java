package it.polimi.ingsw.model.gamecards;

import java.util.ArrayList;
import java.util.List;

public abstract class Card {
    public void setIsFront(boolean b) {
    }

    public void setAngleCovered(String angleToBeCovered) {

    }

    public Resource getResource(String angleToBeCovered) {
    }

    /**
     * @author Maximilian Mangosi
     * Retourns a list with all the resources present in the card
     * @return idem
     */
    public List<Resource> getCardResources() {
        //cycle through the card angles and return a list with all the resources in the card
        List<Resource> cardResouces = new ArrayList<>();
        if(getAngle("NW")!=null && !(getAngle("NW").isCovered())){
            cardResouces.add(getAngle("NW").getResource());
        }
        if(getAngle("SW")!=null && !(getAngle("SW").isCovered())){
            cardResouces.add(getAngle("SW").getResource());
        }
        if(getAngle("NE")!=null && !(getAngle("NE").isCovered())){
            cardResouces.add(getAngle("NE").getResource());
        }
        if(getAngle("SE")!=null && !(getAngle("SE").isCovered())){
            cardResouces.add(getAngle("SE").getResource());
        }

        return cardResouces;
    }

}
