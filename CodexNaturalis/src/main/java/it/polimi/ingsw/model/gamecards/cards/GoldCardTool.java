package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;

public class GoldCardTool extends GoldCard{
    private Tool tool;

    /**
     * @author Giuseppe Laguardia
     * @param NW coordinates of the angle
     * @param NE coordinates of the angle
     * @param SW coordinates of the angle
     * @param SE coordinates of the angle
     * @param reign of the card
     * @param points of the card
     * @param requirements of the card
     * @param tool of the card
     */
    public GoldCardTool(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements, Tool tool, int id) {
        super(NW, NE, SW, SE, reign, points, requirements, id);
        this.tool=tool;
    }

    /**
     * getter of attribute Tool
     * @author Giuseppe Laguardia
     * @return
     */
    public Tool getTool() {
        return tool;
    }

    /**
     * @author Giorgio Mattina
     * adds points to the current player
     * @param player current player
     */
    public void addPoints (Player player){
        player.setPoints(player.getPoints() + player.getResourceCounter(this.getTool()));

    }
}
