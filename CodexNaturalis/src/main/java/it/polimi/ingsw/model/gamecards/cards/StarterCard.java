package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.util.ArrayList;
import java.util.List;

public class StarterCard extends Card {
    private final Reign BackNW;
    private final Reign BackNE;
    private final Reign BackSW;
    private final Reign BackSE;
    private final List<Reign> centralResources;
    public StarterCard(Resource NW, Resource NE, Resource SW, Resource SE, Reign backNW, Reign backNE, Reign backSW, Reign backSE, ArrayList<Reign> centralResources) {
        super(NW, NE, SW, SE);
        BackNW = backNW;
        BackNE = backNE;
        BackSW = backSW;
        BackSE = backSE;
        this.centralResources = centralResources;
    }
    /**
     * Resource getter
     * @author Giuseppe Laguardia
     * @param angle string that determine the desired angle's position, accepted values:(NW,SW,NE,SE)
     * @return the Resource in position "angle" on the back of the card. If the angle isn't accepted returns null
     */

    public Reign getResourceBack(String angle) {
        if(angle.equals("NW"))
            return BackNW;
        if(angle.equals("SW"))
            return BackSW;
        if (angle.equals("NE"))
            return BackNE;
        if(angle.equals("SE"))
            return BackSE;
        return null;
    }

    public List<Resource> getCentralResource() {
        return new ArrayList<>(centralResources);
    }

    /**
     * Returns a  list of resources, when starter card is played back
     * @author Giorgio Mattina
     * @return
     */
    public List<Resource> getBackResources(){
        //cycle through the card angles and return a list with all the resources in the card
        List<Resource> cardResources;
        cardResources=new ArrayList<>();

        String[] angles ={"NW","SW","NE","SE"};

        for (String angle: angles)
            if(getResourceBack(angle)!=null && !getResourceBack(angle).isEmpty())
                cardResources.add(getResourceBack(angle));

        return cardResources;
    }

}
