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

    @Override
    public void processMessage (Controller controller, ClientHandler clientHandler)  {
       try {
           controller.joinGame(username);
       } catch (NoGameExistsException e) {
           throw new RuntimeException(e);
       } catch (PlayerNameNotUniqueException e) {
           throw new RuntimeException(e);
       } catch (IllegalOperationException e) {
           throw new RuntimeException(e);
       }
    }

}
