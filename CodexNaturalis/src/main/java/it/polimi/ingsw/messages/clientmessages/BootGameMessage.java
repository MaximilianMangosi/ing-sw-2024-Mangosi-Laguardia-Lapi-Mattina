package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.OnlyOneGameMessage;
import it.polimi.ingsw.messages.exceptionmessages.UnacceptableNumOfPlayersMessage;
import it.polimi.ingsw.messages.servermessages.PlayersListMessage;
import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.messages.servermessages.UserIDMessage;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.ClientHandler;
import java.io.IOException;
import java.util.UUID;
public class BootGameMessage extends ClientMessage{
    private int numPlayers;
    private String username;

    /**
     * Constructor of BootGameMessage
     * @author Giuseppe Laguardia
     * @param numOfPlayers, the number of players that will be playing in the newly booted game
     * @param playerName the name of the player that called bootGame
     */
    public BootGameMessage(int numOfPlayers, String playerName) {
       this.numPlayers=numOfPlayers;
       this.username=playerName;
    }

    /**
     * Override, gets the controller from the clientHandler and calls bootGame on
     * the controller
     * @author Giorgio Mattina
     * @param clientHandler the object that called processMessage
     */
    public void processMessage( ClientHandler clientHandler) throws IOException {
        try {
            Controller c= clientHandler.getController();
            UUID newId =c.bootGame(numPlayers,username);
            UserIDMessage answer = new UserIDMessage(newId);
            PlayersListMessage msg = new PlayersListMessage(c.getPlayersList());
            clientHandler.broadCast(msg);
            clientHandler.answerClient(answer);
            clientHandler.addClient(newId,clientHandler);
        } catch (UnacceptableNumOfPlayersException e) {
            UnacceptableNumOfPlayersMessage answer = new UnacceptableNumOfPlayersMessage();
            clientHandler.answerClient(answer);

        } catch (IllegalOperationException e) {
            IllegalOperationMessage answer = new IllegalOperationMessage(new IllegalOperationException("Illegal Operation\n"));
            clientHandler.answerClient(answer);

        } catch (OnlyOneGameException e) {
            OnlyOneGameMessage answer = new OnlyOneGameMessage();
            clientHandler.answerClient(answer);
        }
    }

}
