package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.ObjectOutputStream;

public class JoinGameMessage extends ClientMessage {
     private String username;

    /**
     * Constructor of JoinGameMessage
     * @author Giorgio Mattina
     * @param username
     */
     JoinGameMessage(String username){
         this.username=username;
     }

    /**
     * Override, gets the controller from the clientHandler and ccalls joinGame on the controller
     * @author Giorgio Mattina
     * @param clientHandler
     */
    @Override
    public void processMessage ( ClientHandler clientHandler)  {
       try {
           clientHandler.getController().joinGame(username);
       } catch (NoGameExistsException e) {
           throw new RuntimeException(e);
       } catch (PlayerNameNotUniqueException e) {
           throw new RuntimeException(e);
       } catch (IllegalOperationException e) {
           throw new RuntimeException(e);
       }
    }

}
