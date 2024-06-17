package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;

public class PlayersListMessage extends ServerMessage{
    List<String> players;

    public PlayersListMessage(List<String> players) {
        this.players =new ArrayList<>(players);
    }

    @Override
    public void processMessage(ViewSocket view) {
        System.out.println(players);
        view.getGameData().setPlayersList(players);
    }
}
