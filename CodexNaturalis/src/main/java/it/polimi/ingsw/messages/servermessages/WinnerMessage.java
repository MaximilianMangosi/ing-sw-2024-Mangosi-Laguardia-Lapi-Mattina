package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

public class WinnerMessage extends ServerMessage{
    String winner;

    public WinnerMessage(String winner) {
        this.winner = winner;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setWinner(winner);
    }
}
