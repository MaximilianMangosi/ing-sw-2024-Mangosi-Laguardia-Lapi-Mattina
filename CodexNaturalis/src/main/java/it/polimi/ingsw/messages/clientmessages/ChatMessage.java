package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.messages.servermessages.UpdateChatMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.view.ViewRMI;

import java.io.IOException;

public class ChatMessage extends ClientMessage {
    private String chatMessage;

    /**
     * updates the chat message
     * @author Maximilian Mangosi
     * @param chatMessage
     */
    public ChatMessage(String chatMessage){
        this.chatMessage = chatMessage;
    }

    /**
     * processes the message
     * @author Maximilian Mangosi
     * @param clientHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        ViewRMI view = clientHandler.getController().getView();
        try {
            view.sendChatMessage(chatMessage);
        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        }
        clientHandler.answerClient(new SuccessMessage());
        clientHandler.broadCast(new UpdateChatMessage("global",view.getChatList()));
    }
}
