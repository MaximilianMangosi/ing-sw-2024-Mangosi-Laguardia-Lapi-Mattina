package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

public class NumOfResourceCardsMessage extends ServerMessage{
    int num;

    public NumOfResourceCardsMessage(int num) {
        this.num = num;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setNumOfResourceCards(num);
    }
}
