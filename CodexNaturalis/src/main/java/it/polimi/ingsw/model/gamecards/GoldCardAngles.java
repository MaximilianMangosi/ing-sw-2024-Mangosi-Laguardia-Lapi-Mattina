package it.polimi.ingsw.model.gamecards;

import java.util.HashMap;

public class GoldCardAngles extends GoldCard{
    public GoldCardAngles(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements) {
        super(NW, NE, SW, SE, reign, points, requirements);
    }
}
