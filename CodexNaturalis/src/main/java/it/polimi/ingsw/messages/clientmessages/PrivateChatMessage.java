package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.messages.servermessages.UpdateChatMessage;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PrivateChatMessage extends ClientMessage{
    private String message;
    private String receiver;
    private UUID sender;
    /**
     * updates message
     * @author Giuseppe Laguardia
     */
    public PrivateChatMessage(String message, String receiver, UUID sender) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }
    /**
     * processes the message
     * @author Giuseppe Laguardia
     * @param clientHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        try {
            Controller controller = clientHandler.getController();
            controller.addMessage(receiver,message,sender);
            clientHandler.answerClient(new SuccessMessage());
            List<String> chat=controller.getPrivateChat(receiver,sender);
            UpdateChatMessage msg= new UpdateChatMessage(receiver,chat);
            clientHandler.sendTo(sender,msg);
            UUID receiverID=controller.getUserIDs().entrySet().stream().filter(entry->entry.getValue().getName().equals(receiver)).findAny().get().getKey();
            if(clientHandler.getAllClients().containsKey(receiverID))
                clientHandler.sendTo(receiverID,msg);

        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        }

    }
}
