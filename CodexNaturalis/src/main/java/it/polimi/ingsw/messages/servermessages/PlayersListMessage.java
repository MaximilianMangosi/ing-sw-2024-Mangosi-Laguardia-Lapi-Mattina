package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Giuseppe Laguardia
 * updates the list of players in the game
 */
public class PlayersListMessage extends ServerMessage{
    List<String> players;

    public PlayersListMessage(List<String> players) {
        this.players =new ArrayList<>(players);
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setPlayersList(players);
    }
}
