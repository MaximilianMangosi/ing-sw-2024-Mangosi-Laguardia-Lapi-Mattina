package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.InvalidGameIDMessage;
import it.polimi.ingsw.messages.exceptionmessages.NoGameExistsMessage;
import it.polimi.ingsw.messages.exceptionmessages.PlayerNameNotUniqueMessage;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.InvalidGameID;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.view.ViewRMIContainer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class JoinGameMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID= -337423706076800830L;
     private String username;
     private UUID gameId;

    /**
     * Constructor of JoinGameMessage
     * @author Giorgio Mattina
     * @param username
     */
    public JoinGameMessage(UUID gameId,String username){
         this.username=username;
         this.gameId=gameId;
     }

    /**
     * Override, gets the controller from the clientHandler and ccalls joinGame on the controller
     * @author Giorgio Mattina
     * @param clientHandler
     */
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
       try {
           ViewRMIContainer container= clientHandler.getViewContainer();
           Controller c = container.getController(gameId);

           clientHandler.setController(c);
           UUID newId =container.joinGame(gameId,username);
           clientHandler.setMyViewUpdater(gameId);
           UserIDMessage idMessage = new UserIDMessage(newId);
           clientHandler.answerClient(idMessage);

           Thread.sleep(100); // to give client time to connect to the other port for view updates

           if(c.getView().isGameStarted()){
               for(UUID id: clientHandler.getAllClients().keySet() ){
                   clientHandler.sendTo(id,new HandMessage(c.getPlayersHands().get(id)));
                   clientHandler.sendTo(id,new GoalOptionsMessage(c.getGoalOptions().get(id)));
                   clientHandler.sendTo(id,new StarterCardMessage(c.getPlayersStarterCards().get(id)));

                   String playerName=c.getUserIDs().get(id).getName();
                   for (String p: c.getPlayersList()){
                       if(!p.equals(playerName))
                           clientHandler.sendTo(id, new UpdateChatMessage(p,c.getPrivateChat(p,id)));
                   }
               }
               GameStartMessage gameStartMessage = new GameStartMessage(c.getPublicGoals(),c.getVisibleCards(),c.getCurrentPlayer(),c.getGlobalChat(),c.getPlayerToColor());
               clientHandler.broadCast(gameStartMessage);
           }
           PlayersListMessage playersListMessage = new PlayersListMessage(c.getPlayersList());
           clientHandler.broadCast(playersListMessage);

       } catch (PlayerNameNotUniqueException e) {
           PlayerNameNotUniqueMessage answer = new PlayerNameNotUniqueMessage();
           clientHandler.answerClient(answer);
       } catch (IllegalOperationException e) {
           IllegalOperationMessage answer = new IllegalOperationMessage(e);
           clientHandler.answerClient(answer);
       } catch (InvalidGameID e) {
           InvalidGameIDMessage answer=new InvalidGameIDMessage();
           clientHandler.answerClient(answer);
       } catch (InterruptedException ignore) {}
    }

}
