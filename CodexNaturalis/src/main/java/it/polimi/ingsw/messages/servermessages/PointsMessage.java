package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.HashMap;
import java.util.Map;
/**
 * @author Giuseppe Laguardia
 * sends the number of points
 */
public class PointsMessage extends ServerMessage{
    HashMap<String,Integer> newPoints;

    public PointsMessage(HashMap<String, Integer> newPoints) {
        this.newPoints = new HashMap<>(newPoints);
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setPlayersPoints(newPoints);
    }
}
