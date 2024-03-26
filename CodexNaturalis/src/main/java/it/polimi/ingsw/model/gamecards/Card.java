package it.polimi.ingsw.model.gamecards;

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
    }
}
