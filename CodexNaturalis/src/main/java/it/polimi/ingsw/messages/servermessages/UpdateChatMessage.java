package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;

public class UpdateChatMessage extends ServerMessage{
    private final List<String> messages;

    public UpdateChatMessage(List<String> messages){
        this.messages =new ArrayList<>(messages);
    }

    @Override
    public void processMessage(ViewSocket view) {
        System.out.println(messages);
        view.getGameData().setChatData(messages);
    }
}
