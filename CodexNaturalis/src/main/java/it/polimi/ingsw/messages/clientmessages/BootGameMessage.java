package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.OnlyOneGameMessage;
import it.polimi.ingsw.messages.exceptionmessages.PlayerNameNotUniqueMessage;
import it.polimi.ingsw.messages.exceptionmessages.UnacceptableNumOfPlayersMessage;
import it.polimi.ingsw.messages.servermessages.GameKeyMessage;
import it.polimi.ingsw.messages.servermessages.PlayersListMessage;
import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.messages.servermessages.UserIDMessage;
import it.polimi.ingsw.model.gamelogic.exceptions.InvalidGameID;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.CloseGame;
import it.polimi.ingsw.server.DisconnectionHandler;
import it.polimi.ingsw.server.ViewUpdater;
import it.polimi.ingsw.view.ViewRMI;
import it.polimi.ingsw.view.ViewRMIContainer;

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
            ViewRMIContainer viewContainer= clientHandler.getViewContainer();
            GameKey gameKey =viewContainer.bootGame(numPlayers,username);
            Controller c= viewContainer.getController(gameKey.gameID());
            clientHandler.setController(c);
            clientHandler.addViewUpdater(gameKey.gameID(),new ViewUpdater());
            GameKeyMessage answer = new GameKeyMessage(gameKey);
            PlayersListMessage msg = new PlayersListMessage(c.getPlayersList());
            clientHandler.answerClient(answer);
            Thread.sleep(100); // maybe better with atomic boolean
            clientHandler.broadCast(msg);
            new CloseGame(c).start();
            new DisconnectionHandler(c).start();
        } catch (UnacceptableNumOfPlayersException e) {
            UnacceptableNumOfPlayersMessage answer = new UnacceptableNumOfPlayersMessage();
            clientHandler.answerClient(answer);

        } catch (IllegalOperationException e) {
            IllegalOperationMessage answer = new IllegalOperationMessage(new IllegalOperationException("Illegal Operation\n"));
            clientHandler.answerClient(answer);

        }catch (PlayerNameNotUniqueException e) {
            clientHandler.answerClient(new PlayerNameNotUniqueMessage());
        } catch (InterruptedException | InvalidGameID ignore) {}

    }

}
