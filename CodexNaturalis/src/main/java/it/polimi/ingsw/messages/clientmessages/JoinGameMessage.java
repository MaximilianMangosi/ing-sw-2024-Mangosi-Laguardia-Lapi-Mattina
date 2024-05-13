package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.NoGameExistsMessage;
import it.polimi.ingsw.messages.exceptionmessages.PlayerNameNotUniqueMessage;
import it.polimi.ingsw.messages.servermessages.PlayersListMessage;
import it.polimi.ingsw.messages.servermessages.UserIDMessage;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class JoinGameMessage extends ClientMessage {
     private String username;

    /**
     * Constructor of JoinGameMessage
     * @author Giorgio Mattina
     * @param username
     */
    public JoinGameMessage(String username){
         this.username=username;
     }

    /**
     * Override, gets the controller from the clientHandler and ccalls joinGame on the controller
     * @author Giorgio Mattina
     * @param clientHandler
     */
    @Override
    public void processMessage ( ClientHandler clientHandler) throws IOException {
       try {
           Controller c= clientHandler.getController();
           UUID newId =c.joinGame(username);
           UserIDMessage answer = new UserIDMessage(newId);
           PlayersListMessage msg = new PlayersListMessage(c.getPlayersList());
           clientHandler.answerClient(answer);
           clientHandler.broadCast(msg);
       } catch (NoGameExistsException e) {
           NoGameExistsMessage answer = new NoGameExistsMessage();
           clientHandler.answerClient(answer);
       } catch (PlayerNameNotUniqueException e) {
           PlayerNameNotUniqueMessage answer = new PlayerNameNotUniqueMessage();
           clientHandler.answerClient(answer);
       } catch (IllegalOperationException e) {
           IllegalOperationMessage answer = new IllegalOperationMessage(e);
           clientHandler.answerClient(answer);
       }
    }

}
