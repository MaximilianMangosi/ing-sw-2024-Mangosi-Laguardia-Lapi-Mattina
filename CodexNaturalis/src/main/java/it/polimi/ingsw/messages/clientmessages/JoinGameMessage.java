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
import java.io.Serial;
import java.util.Map;
import java.util.UUID;

public class JoinGameMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID= -337423706076800830L;
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
           c.getView().initializeFieldBuildingHelper(username);

           UserIDMessage idMessage = new UserIDMessage(newId);
           clientHandler.answerClient(idMessage);

           Thread.sleep(100); // to give client time to connect to the other port for view updates

           if(c.getView().isGameStarted()){
               for(UUID id: clientHandler.getAllClients().keySet() ){
                   clientHandler.sendTo(id,new HandMessage(c.getPlayersHands().get(id)));
                   clientHandler.sendTo(id,new GoalOptionsMessage(c.getGoalOptions().get(id)));
                   clientHandler.sendTo(id,new StarterCardMessage(c.getPlayersStarterCards().get(id)));
               }
               GameStartMessage gameStartMessage = new GameStartMessage(c.getPublicGoals(),c.getVisibleCards(),c.getCurrentPlayer());
               clientHandler.broadCast(gameStartMessage);
           }
           PlayersListMessage playersListMessage = new PlayersListMessage(c.getPlayersList());
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
       } catch (InterruptedException ignore) {}
    }

}
