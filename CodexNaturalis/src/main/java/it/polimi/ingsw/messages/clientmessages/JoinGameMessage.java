package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.NoGameExistsMessage;
import it.polimi.ingsw.messages.exceptionmessages.PlayerNameNotUniqueMessage;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
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
           UserIDMessage idMessage = new UserIDMessage(newId);
           PlayersListMessage playersListMessage = new PlayersListMessage(c.getPlayersList());
           GameStartMessage gameStartMessage = new GameStartMessage(c.getPublicGoals(),c.getVisibleCards());
           clientHandler.answerClient(idMessage);
           clientHandler.addClient(newId,clientHandler);
           if(c.getView().isGameStarted()){
               for(Map.Entry<UUID,ClientHandler> entry : clientHandler.getAllClients().entrySet() ){
                   UUID id = entry.getKey();
                   ClientHandler target = entry.getValue();
                   target.answerClient(new HandMessage(c.getPlayersHands().get(id)));
                   target.answerClient(new GoalOptionsMessage(c.getGoalOptions().get(id)));
                   target.answerClient(new StarterCardMessage(c.getPlayersStarterCards().get(id)));
               }

           }
           clientHandler.broadCast(playersListMessage);
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
