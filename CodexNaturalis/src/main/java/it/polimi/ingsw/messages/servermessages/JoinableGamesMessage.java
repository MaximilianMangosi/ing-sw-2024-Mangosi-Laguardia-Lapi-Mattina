package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JoinableGamesMessage extends  ServerMessage{
    Map<UUID, List<String>> joinableGames;

    public JoinableGamesMessage(Map<UUID, List<String>> joinableGames) {
        this.joinableGames = new HashMap<>(joinableGames);
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setJoinableGames(joinableGames);

    }
}
