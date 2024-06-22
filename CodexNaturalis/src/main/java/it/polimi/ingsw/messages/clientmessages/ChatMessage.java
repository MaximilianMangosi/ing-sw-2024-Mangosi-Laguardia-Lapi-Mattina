package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
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
        try {
            view.sendChatMessage(chatMessage);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientHandler.answerClient(new SuccessMessage());
        clientHandler.broadCast(new UpdateChatMessage("global",view.getChatList()));
    }
}
