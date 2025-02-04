package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends message that the game has ended
 */
public class GameEndMessage extends ServerMessage{
    String winner;

    public GameEndMessage(String winner) {
        this.winner = winner;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setGameEnded(true);
        view.getGameData().setWinner(winner);
    }
}
