package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.view.ViewSocket;

import java.util.List;

public class LegalPositionMessage extends ServerMessage{
    List<Coordinates> newLegalPositions;

    public LegalPositionMessage(List<Coordinates> newLegalPositions) {
        this.newLegalPositions = newLegalPositions;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setLegalPositions(newLegalPositions);
    }
}
