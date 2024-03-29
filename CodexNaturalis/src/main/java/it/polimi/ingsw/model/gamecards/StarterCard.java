package it.polimi.ingsw.model.gamecards;

import java.util.ArrayList;

public class StarterCard extends Card{
    private final ArrayList<Reign> starterResources;
    public StarterCard(Resource NW, Resource NE, Resource SW, Resource SE, ArrayList<Reign> starterResources) {
        super(NW, NE, SW, SE);
        this.starterResources=starterResources;
    }
}
