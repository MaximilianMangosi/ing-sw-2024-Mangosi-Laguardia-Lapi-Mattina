package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Giuseppe Laguardia
 * sends the list of legal positions
 */
public class LegalPositionMessage extends ServerMessage{
    List<Coordinates> newLegalPositions;

    public LegalPositionMessage(List<Coordinates> newLegalPositions) {
        this.newLegalPositions = new ArrayList<>(newLegalPositions);
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setLegalPositions(newLegalPositions);
    }
}
