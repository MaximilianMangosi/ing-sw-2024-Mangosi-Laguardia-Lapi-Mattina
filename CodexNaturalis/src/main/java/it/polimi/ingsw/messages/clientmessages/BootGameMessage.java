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
import java.io.Serial;
import java.util.UUID;
public class BootGameMessage extends ClientMessage{
    @Serial
    private static final long serialVersionUID= 3697664496715415988L;
    private final int numPlayers;
    private final String username;

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
            c.getView().initializeFieldBuildingHelper(username);
            UserIDMessage answer = new UserIDMessage(newId);
            PlayersListMessage msg = new PlayersListMessage(c.getPlayersList());
            clientHandler.answerClient(answer);
            Thread.sleep(100);
            clientHandler.broadCast(msg);
        } catch (UnacceptableNumOfPlayersException e) {
            UnacceptableNumOfPlayersMessage answer = new UnacceptableNumOfPlayersMessage();
            clientHandler.answerClient(answer);

        } catch (IllegalOperationException e) {
            IllegalOperationMessage answer = new IllegalOperationMessage(new IllegalOperationException("Illegal Operation\n"));
            clientHandler.answerClient(answer);

        } catch (OnlyOneGameException e) {
            OnlyOneGameMessage answer = new OnlyOneGameMessage();
            clientHandler.answerClient(answer);
        } catch (InterruptedException ignore) {}
    }

}
