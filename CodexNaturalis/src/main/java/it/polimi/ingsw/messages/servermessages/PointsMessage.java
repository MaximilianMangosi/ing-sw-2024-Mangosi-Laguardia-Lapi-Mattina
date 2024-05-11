package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.HashMap;

public class PointsMessage extends ServerMessage{
    HashMap<String,Integer> newPoints;

    public PointsMessage(HashMap<String, Integer> newPoints) {
        this.newPoints = newPoints;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setPlayersPoints(newPoints);
    }
}
