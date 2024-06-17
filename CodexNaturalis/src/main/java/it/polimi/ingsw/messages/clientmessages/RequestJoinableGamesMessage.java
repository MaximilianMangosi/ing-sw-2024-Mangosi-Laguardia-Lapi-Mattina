package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.messages.servermessages.JoinableGamesMessage;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestJoinableGamesMessage extends ClientMessage{

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.answerClient(new JoinableGamesMessage(clientHandler.getViewContainer().getJoinableGames()));
    }
}
