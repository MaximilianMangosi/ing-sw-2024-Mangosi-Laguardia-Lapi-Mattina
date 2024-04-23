package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.Map;

public class GoldCardAngles extends GoldCard{
    public GoldCardAngles(Resource NW, Resource NE, Resource SW, Resource SE, Reign reign, int points, HashMap<Reign, Integer> requirements) {
        super(NW, NE, SW, SE, reign, points, requirements);
    }
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
