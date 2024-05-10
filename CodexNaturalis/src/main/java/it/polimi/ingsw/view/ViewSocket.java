package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.messages.clientmessages.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ViewSocket implements View{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private UUID myID;

    public ViewSocket(OutputStream output, InputStream input) throws IOException {
        this.output = new ObjectOutputStream(output);
        this.input = new ObjectInputStream(input);
    }

    @Override
    public Map<String, Integer> getPlayersPoints() throws RemoteException {
        return null;
    }

    @Override
    public int getNumOfResourceCards() throws RemoteException {
        return 0;
    }

    @Override
    public int getNumOfGoldCards() throws RemoteException {
        return 0;
    }

    @Override
    public List<Card> showPlayerHand(UUID uid) throws RemoteException, InvalidUserId {
        return null;
    }

    @Override
    public HashMap<Coordinates, Card> getPlayersField(String name) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getPlayersList() throws RemoteException {
        return null;
    }

    @Override
    public String getCurrentPlayer() throws RemoteException {
        return null;
    }

    @Override
    public List<Coordinates> showPlayersLegalPositions(UUID uid) throws RemoteException, InvalidUserId {
        return null;
    }

    @Override
    public Goal[] getPublicGoals() throws RemoteException {
        return new Goal[0];
    }

    @Override
    public Goal[] showPlayerGoalOptions(UUID uid) throws RemoteException, InvalidUserId {
        return new Goal[0];
    }

    @Override
    public Goal showPrivateGoal(UUID uid) throws RemoteException, InvalidUserId {
        return null;
    }

    @Override
    public List<Card> getVisibleCards() throws RemoteException {
        return null;
    }

    @Override
    public String getWinner() throws RemoteException {
        return null;
    }

    @Override
    public StarterCard getStarterCard(UUID userId) throws RemoteException {
        return null;
    }

    /**
     * Sends to server a message for creating a new Game, and wait for his reply
     * @param numOfPlayers the int representing how many player can join the game
     * @param playerName the username of the player creating the Game
     * @return the UUID that identifies the client on the server
     * @throws IOException when there's a problem during message (de)serialization
     * @throws ClassNotFoundException when Class of a serialized object cannot be found
     * @throws UnacceptableNumOfPlayersException when numOfPlayers is not in range [2:4]
     * @throws OnlyOneGameException when on the server is already hosted a Game
     * @throws IllegalOperationException when in the current phase of the game bootGame is not admissible
     */
    @Override
    public synchronized UUID bootGame(int numOfPlayers, String playerName) throws OnlyOneGameException, UnacceptableNumOfPlayersException, ClassNotFoundException, IllegalOperationException, IOException, InvalidGoalException, HandNotFullException, HandFullException, InvalidChoiceException, NoGameExistsException, IsNotYourTurnException, InvalidUserId, RequirementsNotMetException, PlayerNameNotUniqueException, IllegalPositionException, InvalidCardException, DeckEmptyException {
        BootGameMessage message = new BootGameMessage(numOfPlayers,playerName);
        output.writeObject(message);
        ServerMessage reply= (ServerMessage) input.readObject();
        reply.processMessage();
        // if processMessage didn't throw an exception then the reply of the server is an UserIDMessage
        return ((UserIDMessage) reply).getYourID();
    }

    @Override
    public synchronized UUID joinGame(String playerName) throws IOException, NoGameExistsException, PlayerNameNotUniqueException, IllegalOperationException, ClassNotFoundException, InvalidGoalException, HandNotFullException, HandFullException, InvalidChoiceException, IsNotYourTurnException, InvalidUserId, RequirementsNotMetException, UnacceptableNumOfPlayersException, OnlyOneGameException, IllegalPositionException, InvalidCardException, DeckEmptyException {
        JoinGameMessage message = new JoinGameMessage(playerName);
        output.writeObject(message);
        ServerMessage reply= (ServerMessage) input.readObject();
        reply.processMessage();
        // if processMessage didn't throw an exception then the reply of the server is an UserIDMessage
        return ((UserIDMessage) reply).getYourID();
    }

    @Override
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException, InvalidUserId {

    }

    @Override
    public void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId {

    }

    @Override
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws RemoteException, InvalidUserId, IllegalOperationException, InvalidUserId {

    }

    @Override
    public void chooseGoal(UUID userId, Goal newGoal) throws RemoteException, InvalidGoalException, InvalidUserId, IllegalOperationException, InvalidUserId {

    }

    @Override
    public void drawFromDeck(UUID userId, int choice) throws RemoteException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, InvalidUserId {

    }

    @Override
    public void drawVisibleCard(UUID userId, int choice) throws RemoteException, IsNotYourTurnException, HandFullException, IllegalOperationException, InvalidChoiceException, InvalidUserId {

    }

    @Override
    public void closeGame(UUID userID) throws RemoteException, InvalidUserId {

    }

    @Override
    public boolean isGameEnded() throws RemoteException {
        return false;
    }

    @Override
    public boolean isGameStarted() throws RemoteException {
        return false;
    }

    @Override
    public List<Coordinates> getFieldBuildingHelper(String name) throws RemoteException {
        return null;
    }

    @Override
    public void initializeFieldBuildingHelper(String myName) throws RemoteException {

    }

    @Override
    public Reign getTopOfResourceCardDeck() throws RemoteException {
        return null;
    }

    @Override
    public Reign getTopOfGoldCardDeck() throws RemoteException {
        return null;
    }

    @Override
    public boolean amIPinged(UUID id) throws RemoteException {
        return false;
    }

    @Override
    public void pong(UUID myID) throws RemoteException {

    }

    /**
     * Set the userID,that identifies the client when interacting with the server
     * @param yourID the user's UUID
     */
    public void setMyID(UUID yourID) {
        myID=yourID;
    }
}
