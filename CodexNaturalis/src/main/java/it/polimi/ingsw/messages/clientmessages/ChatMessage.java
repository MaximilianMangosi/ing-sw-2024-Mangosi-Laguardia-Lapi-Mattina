package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.messages.servermessages.UpdateChatMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.view.ViewRMI;

import java.io.IOException;

public class ChatMessage extends ClientMessage {
    private String chatMessage;

    public ChatMessage(String chatMessage){
        this.chatMessage = chatMessage;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        ViewRMI view = clientHandler.getController().getView();
        view.sendChatMessage(chatMessage);
        System.out.println(view.getChatList());
        clientHandler.answerClient(new SuccessMessage());
        clientHandler.broadCast(new UpdateChatMessage(view.getChatList()));
    }
}
