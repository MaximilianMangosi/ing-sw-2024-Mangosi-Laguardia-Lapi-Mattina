package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * @author Giuseppe Laguardia
 * updates the chat list
 */
public class UpdateChatMessage extends ServerMessage{
    private final String user;
    private  UUID updateReceiver;
    private final List<String> messages;

    public UpdateChatMessage(String user,List<String> messages){
        this.messages =new ArrayList<>(messages);
        this.user=user;
    }
    public UpdateChatMessage(UUID receiver,String user,List<String> messages){
        this.messages =new ArrayList<>(messages);
        this.user=user;
        this.updateReceiver=receiver;
    }

    @Override
    public void processMessage(ViewSocket view) {
        if(user.equals("global"))
            view.getGameData().setChatData(messages);
        else
            view.getGameData().setPrivateChat(user,messages);
    }

    public UUID getReceiver() {
        return updateReceiver;
    }
}
