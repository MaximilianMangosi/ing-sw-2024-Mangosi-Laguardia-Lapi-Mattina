package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.List;

public class UpdateChat extends ServerMessage{
    private List<String> messages;

    public UpdateChat(List<String> messages){
        this.messages = messages;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setChatData(messages);
    }
}
