package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.messages.servermessages.UpdateChat;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.view.View;
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
        clientHandler.answerClient(new SuccessMessage());
        clientHandler.broadCast(new UpdateChat(view.getChatList()));
    }
}
