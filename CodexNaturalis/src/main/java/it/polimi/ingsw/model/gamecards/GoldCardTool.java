package it.polimi.ingsw.model.gamecards;

import java.util.HashMap;

public class GoldCardTool extends GoldCard{
    private Tool tool;
    public GoldCardTool(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements,Tool tool) {
        super(NW, NE, SW, SE, reign, points, requirements);
        this.tool=tool;
    }

    public Tool getTool() {
        return tool;
    }
}
