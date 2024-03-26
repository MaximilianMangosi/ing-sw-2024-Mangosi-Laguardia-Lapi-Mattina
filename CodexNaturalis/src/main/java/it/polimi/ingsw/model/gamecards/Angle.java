package it.polimi.ingsw.model.gamecards;

public class Angle {

    private  boolean isCovered=false;
    private Resource resource;

    /**
     * Angle constructor
     * @author Giuseppe Laguardia
     * @param isCovered is true if another cards is over the angle
     * @param resource resource contained in the angle
     */
    public Angle(boolean isCovered,Resource resource) {
        this.isCovered=isCovered;
        this.resource=resource;
    }
    public Angle(Angle a) {
        this.isCovered=a.isCovered;
        this.resource=a.resource;

    }


    public boolean isCovered() {
    }

    public Resource getResource() {
    }
}