package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StarterCard extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Reign backNW;
    private final Reign backNE;
    private final Reign backSW;
    private final Reign backSE;
    private final List<Reign> centralResources;

    /**
     * @author Giuseppe Laguardia
     * @param NW coordinates of the angle front
     * @param NE coordinates of the angle front
     * @param SW coordinates of the angle front
     * @param SE coordinates of the angle front
     * @param backNW coordinates of the angle back
     * @param backNE coordinates of the angle back
     * @param backSW coordinates of the angle back
     * @param backSE coordinates of the angle back
     * @param centralResources central resources
     */
    public StarterCard(Resource NW, Resource NE, Resource SW, Resource SE, Reign backNW, Reign backNE, Reign backSW, Reign backSE, ArrayList<Reign> centralResources, int id) {
        super(NW, NE, SW, SE, id);
        this.backNW = backNW;
        this.backNE = backNE;
        this.backSW = backSW;
        this.backSE = backSE;
        this.centralResources = centralResources;
    }

    public StarterCard(StarterCard starterCard) {
        super(starterCard);
        backNW = starterCard.backNW;
        backNE = starterCard.backNE;
        backSW = starterCard.backSW;
        backSE = starterCard.backSE;
        this.centralResources = starterCard.centralResources;
    }

    /**
     * Resource getter
     * @author Giuseppe Laguardia
     * @param angle string that determine the desired angle's position, accepted values:(NW,SW,NE,SE)
     * @return the Resource in position "angle" on the back of the card. If the angle isn't accepted returns null
     */

    public Reign getResourceBack(String angle) {
        if(angle.equals("NW"))
            return backNW;
        if(angle.equals("SW"))
            return backSW;
        if (angle.equals("NE"))
            return backNE;
        if(angle.equals("SE"))
            return backSE;
        return null;
    }

    /**
     * @author Giorgio Mattina
     * @return the central resources of the starter card
     */
    public List<Resource> getCentralResource() {
        return new ArrayList<>(centralResources);
    }

    /**
     * @author Giorgio Mattina
     * @return a  list of resources, when starter card is played back
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
        if(other instanceof StarterCard temp)
            return super.equals(temp) && backNW==temp.backNW && backNE ==temp.backNE && backSW ==temp.backSW && backSE==temp.backSE;
        return false;

    }

    /**
     * @author Giuseppe Laguardia
     * @return resource of the card
     */
    public List<Resource> getCardResources() {
        //cycle through the card angles and return a list with all the resources in the card

        List<Resource> cardResources = new ArrayList<>();
        if (!isFront()) {
            String[] angles ={"NW","NE","SW","SE"};

            for (String angle: angles)
                if(getResourceBack(angle)!=null && !getResourceBack(angle).isEmpty())
                    cardResources.add(getResourceBack(angle));
        }else {
            String[] angles ={"NW","NE","SW","SE"};

            for (String angle: angles)
                if(getResource(angle)!=null && !getResource(angle).isEmpty())
                    cardResources.add(getResource(angle));

            cardResources.addAll(getCentralResource());
        }

        return cardResources;
    }


}
