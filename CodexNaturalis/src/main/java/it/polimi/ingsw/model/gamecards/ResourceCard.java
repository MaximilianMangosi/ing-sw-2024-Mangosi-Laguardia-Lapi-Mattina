package it.polimi.ingsw.model.gamecards;

public class ResourceCard extends Card{
    Reign reign;
    int points;
    public ResourceCard(Resource NW, Resource NE, Resource SW, Resource SE,int points,Reign reign) {
        super(NW, NE, SW, SE);
        this.points=points;
        this.reign=reign;
    }

    public int getPoints() {
        return points;
    }

    public Reign getReign() {
        return reign;
    }
}
