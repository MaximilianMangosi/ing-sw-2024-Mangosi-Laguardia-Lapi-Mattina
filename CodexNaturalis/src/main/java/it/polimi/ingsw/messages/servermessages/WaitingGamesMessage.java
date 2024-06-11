package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WaitingGamesMessage extends ServerMessage{
    Map<UUID, List<String>> gamesWaiting;

    public WaitingGamesMessage(Map<UUID, List<String>> gamesWaiting) {
        this.gamesWaiting = gamesWaiting;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setJoinableGames(gamesWaiting);
    }
}
