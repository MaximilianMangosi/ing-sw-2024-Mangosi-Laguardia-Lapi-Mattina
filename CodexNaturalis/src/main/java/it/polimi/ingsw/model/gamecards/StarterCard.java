package it.polimi.ingsw.model.gamecards;

import java.util.ArrayList;

public class StarterCard extends Card{
    private final Reign BackNW;
    private final Reign BackNE;
    private final Reign BackSW;
    private final Reign BackSE;
    private final ArrayList<Reign> centralResources;
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

    public ArrayList<Reign> getCentralResource() {
        return new ArrayList<>(centralResources);
    }
    //TODO override getCardResources()
}
