package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

public class NumOfGoldCardsMessage extends  ServerMessage {
    int num;

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setNumOfGoldCards(num);
    }
}
