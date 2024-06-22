package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ViewRMIInterface extends Remote,View  {

    //VIEW

    boolean isRMI() throws RemoteException;

     String getPlayerColor(String player) throws RemoteException;

    public Map<String, Integer> getPlayersPoints() throws RemoteException;
    public int getNumOfResourceCards() throws RemoteException;
    public int getNumOfGoldCards()  throws RemoteException;

    public List<Card> showPlayerHand(UUID uid) throws RemoteException, InvalidUserId;

    public Map<Coordinates,Card>getPlayersField(String name) throws RemoteException;

    public List<String> getPlayersList() throws RemoteException;

    public String getCurrentPlayer() throws RemoteException;

    public List<Coordinates> showPlayersLegalPositions(UUID uid) throws RemoteException, InvalidUserId;

    public Goal[] getPublicGoals() throws RemoteException;

    public Goal[] showPlayerGoalOptions(UUID uid) throws RemoteException, InvalidUserId;

    public Goal showPrivateGoal(UUID uid) throws RemoteException, InvalidUserId;

    public List<Card> getVisibleCards() throws RemoteException;

    public String getWinner() throws RemoteException;
    public StarterCard getStarterCard(UUID userId) throws RemoteException;
    public List<String> getPrivateChat(String receiver, UUID uuid) throws RemoteException;

    //CONTROLLER
    public GameKey bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException, RemoteException;
    public UUID joinGame(UUID gameId,String playerName) throws PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID, RemoteException;
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException, InvalidUserId;
    public void playCardBack(Card selectedCard, Coordinates position,UUID userId) throws RemoteException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId;
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws RemoteException, InvalidUserId, IllegalOperationException,InvalidUserId;
    public void chooseGoal(UUID userId, Goal newGoal) throws RemoteException, InvalidGoalException, InvalidUserId, IllegalOperationException, InvalidUserId;
    public void drawFromDeck(UUID userId,int choice) throws RemoteException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, InvalidUserId;
    public void drawVisibleCard (UUID userId,int choice) throws RemoteException, IsNotYourTurnException, HandFullException, IllegalOperationException, InvalidChoiceException, InvalidUserId;
    public void closeGame(UUID userID) throws RemoteException;


    public boolean isGameEnded() throws RemoteException;

    boolean isGameStarted() throws RemoteException;


    List<Coordinates> getFieldBuildingHelper(String name) throws RemoteException;

    void initializeFieldBuildingHelper(String myName) throws RemoteException;

    Reign getTopOfResourceCardDeck() throws  RemoteException;

    Reign getTopOfGoldCardDeck() throws RemoteException;
    boolean amIPinged(UUID id) throws  RemoteException;

    void pong(UUID myID)throws RemoteException;

    List<String> getChatList() throws RemoteException;

    @Override
    void sendChatMessage(String message) throws IOException, ClassNotFoundException, IllegalOperationException;

    @Override
     List<String> getPrivateChat(String user) throws RemoteException;

    @Override
    void sendPrivateMessage(String receiver, String message, UUID sender) throws RemoteException, IllegalOperationException;

    Controller getController() throws RemoteException;

    void deleteView() throws RemoteException;

    void setViewContainer(ViewRMIContainer viewRMIContainer) throws RemoteException;
}
