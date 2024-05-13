package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

public class TurnMessage extends ServerMessage{
    String currentPlayer;

    public TurnMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setCurrentPlayer(currentPlayer);
    }
}
