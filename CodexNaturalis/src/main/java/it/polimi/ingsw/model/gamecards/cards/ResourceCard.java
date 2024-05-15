package it.polimi.ingsw.model.gamecards.cards;

import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Player;

public class ResourceCard extends Card {
    Reign reign;
    int points;

    /**
     * @author Giuseppe Laguardia
     * @param NW coordinates of the angle
     * @param NE coordinates of the angle
     * @param SW coordinates of the angle
     * @param SE coordinates of the angle
     * @param reign of the card
     * @param points of the card
     */
    public ResourceCard(Resource NW, Resource NE, Resource SW, Resource SE, int points, Reign reign, int id) {
        super(NW, NE, SW, SE, id);
        this.points=points;
        this.reign=reign;
    }

    /**
     * @author Maximilian Mangosi, Giuseppe Laguardia
     * @return the points of the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * @author Giuseppe Laguardia
     * @return the reign of the card
     */
    public Reign getReign() {
        return reign;
    }

    /**
     * @author Giorgio Mattina
     * adds points to the current player
     * @param player current player
     */
    public void addPoints(Player player){
        player.setPoints(player.getPoints() + this.getPoints());
    }
}
