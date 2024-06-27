package it.polimi.ingsw.view;

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
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface View {
    boolean isRMI() throws RemoteException;
    //VIEW
    public Map<String, Integer> getPlayersPoints() throws RemoteException;
    public int getNumOfResourceCards() throws RemoteException;
    public int getNumOfGoldCards()  throws RemoteException;

    public List<Card> showPlayerHand(UUID uid) throws RemoteException, InvalidUserId, IllegalOperationException;
    public List<Card> showPlayerHand() throws RemoteException, InvalidUserId;

    public Map<Coordinates,Card> getPlayersField(String name) throws RemoteException, IllegalOperationException;

    public List<String> getPlayersList() throws RemoteException;

    public String getCurrentPlayer() throws RemoteException;

    public List<Coordinates> showPlayersLegalPositions(UUID uid) throws RemoteException, InvalidUserId, IllegalOperationException;
    public List<Coordinates> showPlayersLegalPositions() throws RemoteException, InvalidUserId;

    public Goal[] getPublicGoals() throws RemoteException, IllegalOperationException;

    public Goal[] showPlayerGoalOptions(UUID uid) throws RemoteException, InvalidUserId, IllegalOperationException;
    public Goal[] showPlayerGoalOptions() throws RemoteException, InvalidUserId;

    public Goal showPrivateGoal(UUID uid) throws RemoteException, InvalidUserId, IllegalOperationException;
    public Goal showPrivateGoal() throws RemoteException, InvalidUserId;

    public List<Card> getVisibleCards() throws RemoteException, IllegalOperationException;

    public String getWinner() throws RemoteException;
    public StarterCard getStarterCard(UUID userId) throws RemoteException;
    public StarterCard getStarterCard() throws RemoteException;
    String getPlayerColor(String player) throws RemoteException;

    //CONTROLLER
    public GameKey bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, ClassNotFoundException, IllegalOperationException, IOException, PlayerNameNotUniqueException;
    public UUID joinGame(UUID gameId, String playerName) throws PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID, IOException, ClassNotFoundException;
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IOException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException, InvalidUserId, ClassNotFoundException;
    public void playCardBack(Card selectedCard, Coordinates position,UUID userId) throws HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId, IOException, ClassNotFoundException;
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws IllegalOperationException, InvalidUserId, IOException, ClassNotFoundException;
    public void chooseGoal(UUID userId, Goal newGoal) throws IOException, InvalidGoalException, IllegalOperationException, ClassNotFoundException, InvalidUserId;
    public void drawFromDeck(UUID userId,int choice) throws IOException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, ClassNotFoundException;
    public void drawVisibleCard (UUID userId,int choice) throws IOException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, ClassNotFoundException;
    void closeGame(UUID userID) throws ClassNotFoundException, IOException;

    public boolean isGameEnded() throws RemoteException;

    boolean isGameStarted() throws RemoteException;

    List<Coordinates> getFieldBuildingHelper(String name) throws RemoteException, IllegalOperationException;

    void initializeFieldBuildingHelper(String myName) throws RemoteException;
    void updateFieldBuildingHelper(Coordinates position, String username) throws RemoteException ;

    Reign getTopOfResourceCardDeck() throws RemoteException, IllegalOperationException;

    Reign getTopOfGoldCardDeck() throws RemoteException, IllegalOperationException;
    boolean amIPinged(UUID id) throws RemoteException;
    void pong(UUID myID) throws IOException, ClassNotFoundException;
    List<String> getChatList() throws RemoteException;
    public void sendPrivateMessage(String receiver, String message, UUID sender) throws IOException, IllegalOperationException, ClassNotFoundException;
    public List<String> getPrivateChat(String receiver, UUID uuid) throws RemoteException;
    public List<String> getPrivateChat(String user) throws RemoteException;
    void sendChatMessage(String message) throws IOException, ClassNotFoundException, IllegalOperationException;
}
