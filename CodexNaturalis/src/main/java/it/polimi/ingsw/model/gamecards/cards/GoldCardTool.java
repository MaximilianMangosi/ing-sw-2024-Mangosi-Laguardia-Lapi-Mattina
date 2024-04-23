package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;

public class GoldCardTool extends GoldCard{
    private Tool tool;
    public GoldCardTool(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements, Tool tool) {
        super(NW, NE, SW, SE, reign, points, requirements);
        this.tool=tool;
    }

    public Tool getTool() {
        return tool;
    }

    public void addPoints (Player player){
        player.setPoints(player.getPoints() + player.getResourceCounter(this.getTool()));

    }
}
