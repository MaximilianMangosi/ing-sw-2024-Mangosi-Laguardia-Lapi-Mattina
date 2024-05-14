package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.Map;

public class GoldCardAngles extends GoldCard{
    /**
     * @author Giuseppe Laguardia
     * @param NW coordinates of the angle
     * @param NE coordinates of the angle
     * @param SW coordinates of the angle
     * @param SE coordinates of the angle
     * @param reign of the card
     * @param points of the card
     * @param requirements of the card
     */
    public GoldCardAngles(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements, int id) {
        super(NW, NE, SW, SE, reign, points, requirements, id);
    }

    /**
     * @author Giorgio Mattina
     * adds point to the current player
     * @param player current player
     */
    public void addPoints(Player player){
        Coordinates position = new Coordinates(0,0);

        //for loop that finds the coordinates of a given card in the field Map
        for (Map.Entry<Coordinates, Card> instance : player.getField().entrySet()) {

            if (instance.getValue().equals(this)) {
                position = instance.getKey();
                break;
            }
        }

        int x = position.x;
        int y = position.y;

        if(player.look(x-1, y+1)){
            player.setPoints(player.getPoints() + this.points);
        }
        if(player.look(x+1, y+1)){
            player.setPoints(player.getPoints() + this.points);
        }
        if(player.look(x-1, y-1)){
            player.setPoints(player.getPoints() + this.points);
        }
        if(player.look(x+1, y-1)){
            player.setPoints(player.getPoints() + this.points);
        }
    }
}
