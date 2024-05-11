package it.polimi.ingsw.view;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.exceptions.*;
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
import java.util.*;

public class ViewSocket implements View{
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final GameData gd;

    public ViewSocket(OutputStream output, InputStream input, GameData gameData) throws IOException {
        this.output = new ObjectOutputStream(output);
        this.input = new ObjectInputStream(input);
        this.gd=gameData;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    @Override
    public Map<String, Integer> getPlayersPoints()  {
        return gd.getPlayersPoints();
    }

    @Override
    public int getNumOfResourceCards()  {
       return gd.getNumOfResourceCards();
    }

    @Override
    public int getNumOfGoldCards() {
        return gd.getNumOfGoldCards();
    }

    @Override
    public List<Card> showPlayerHand(UUID uid) {
        return gd.getHand();
    }

    @Override
    public HashMap<Coordinates, Card> getPlayersField(String name) {
        return gd.getPlayerField(name);
    }

    @Override
    public List<String> getPlayersList() {
        return gd.getPlayersList();
    }

    @Override
    public String getCurrentPlayer() throws RemoteException {
        return gd.getCurrentPlayer();
    }

    @Override
    public List<Coordinates> showPlayersLegalPositions(UUID uid) {
        return gd.getLegalPositions();
    }

    @Override
    public Goal[] getPublicGoals() {
        return gd.getPublicGoals();
    }

    @Override
    public Goal[] showPlayerGoalOptions(UUID uid) {
        return gd.getGoalOptions();
    }

    @Override
    public Goal showPrivateGoal(UUID uid) {
        return gd.getPrivateGoal();
    }

    @Override
    public List<Card> getVisibleCards() {
        return gd.getVisibleCards();
    }

    @Override
    public String getWinner() {
        return gd.getWinner();
    }

    @Override
    public StarterCard getStarterCard(UUID userId) throws RemoteException {
        return gd.getStarterCard();
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
    public UUID bootGame(int numOfPlayers, String playerName) throws OnlyOneGameException, UnacceptableNumOfPlayersException, ClassNotFoundException, IllegalOperationException, IOException, HandNotFullException, HandFullException, InvalidChoiceException, NoGameExistsException, IsNotYourTurnException, InvalidUserId, RequirementsNotMetException, PlayerNameNotUniqueException, IllegalPositionException, InvalidCardException, DeckEmptyException, InvalidGoalException {
        BootGameMessage message = new BootGameMessage(numOfPlayers,playerName);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply = (ServerMessage) input.readObject();
            reply.processMessage();
            // if processMessage didn't throw an exception then the reply of the server is an UserIDMessage
            UUID myID = ((UserIDMessage) reply).getYourID();
            return myID;
        }
    }


    @Override
    public UUID joinGame(String playerName) throws IOException, PlayerNameNotUniqueException, IllegalOperationException, ClassNotFoundException, InvalidGoalException, HandNotFullException, HandFullException, InvalidChoiceException, IsNotYourTurnException, InvalidUserId, RequirementsNotMetException, UnacceptableNumOfPlayersException, OnlyOneGameException, IllegalPositionException, InvalidCardException, DeckEmptyException, NoGameExistsException {
        JoinGameMessage message = new JoinGameMessage(playerName);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply = (ServerMessage) input.readObject();
            reply.processMessage();
            // if processMessage didn't throw an exception then the reply of the server is an UserIDMessage
            UUID myID = ((UserIDMessage) reply).getYourID();
            return myID;
        }
    }
    /**
     *Send a message for play a Card, then waits the server's reply
     * @param selectedCard the Card played
     * @param position the Coordinates where to play the Card
     * @param userId the UUID that identifies the user
     * @throws IllegalOperationException when in the current phase of the game bootGame is not admissible
     * @throws ClassNotFoundException when Class of a serialized object cannot be found
     * @throws HandNotFullException when client plays the second card in the same turn
     * @throws IsNotYourTurnException when client plays out of his turn
     * @throws InvalidUserId when the UserID is not in userId map on the Controller
     * @throws RequirementsNotMetException when the player doesn't have the rights to play the card
     * @throws IllegalPositionException when the client plays the card in an inadmissible position
     * @throws InvalidCardException when the client plays a card that is
     */
    @Override
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException, InvalidUserId, ClassNotFoundException, InvalidChoiceException, NoGameExistsException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, DeckEmptyException, IOException, InvalidGoalException, HandFullException {
        PlayCardMessage message= new PlayCardMessage(selectedCard,position,userId,true);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply=(ServerMessage) input.readObject();
            reply.processMessage();
        }

    }
    /**
     *Send a message for play a Card, then waits the server's reply
     * @param selectedCard the Card played
     * @param position the Coordinates where to play the Card
     * @param userId the UUID that identifies the user
     * @throws IOException when there's a problem during message (de)serialization
     * @throws IllegalOperationException when in the current phase of the game bootGame is not admissible
     * @throws ClassNotFoundException when Class of a serialized object cannot be found
     * @throws HandNotFullException when client plays the second card in the same turn
     * @throws IsNotYourTurnException when client plays out of his turn
     * @throws InvalidUserId when the UserID is not in userId map on the Controller
     * @throws IllegalPositionException when the client plays the card in an inadmissible position
     * @throws InvalidCardException when the client plays a card that is
     */
    @Override
    public void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IOException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId, ClassNotFoundException, InvalidGoalException, HandFullException, InvalidChoiceException, NoGameExistsException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, DeckEmptyException {
        PlayCardMessage message= new PlayCardMessage(selectedCard,position,userId,false);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply=(ServerMessage) input.readObject();
            reply.processMessage();
        }
    }

    /**
     * Sends a message for choose the side on which play the StarterCard, then wait for server's reply
     * @param isFront the side on which you play the StarterCard
     * @param userId the UUID that identifies the user
     * @throws IOException when there's a problem during message (de)serialization
     * @throws IllegalOperationException when in the current phase of the game bootGame is not admissible
     * @throws ClassNotFoundException when Class of a serialized object cannot be found
     * @throws InvalidUserId when the UserID is not in userId map on the Controller
     */
    @Override
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws IOException, IllegalOperationException, InvalidUserId, ClassNotFoundException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException {
        ChooseStarterCardMessage message= new ChooseStarterCardMessage(isFront,userId);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply=(ServerMessage) input.readObject();
            reply.processMessage();
        }
    }

    /**
     *
     * @param userId
     * @param newGoal
     * @throws InvalidGoalException
     * @throws InvalidUserId
     * @throws IllegalOperationException
     * @throws InvalidUserId
     */
  @Override
    public void chooseGoal(UUID userId, Goal newGoal) throws InvalidGoalException, IllegalOperationException, InvalidUserId {
//        ChooseGoalMessage message= new ChooseGoalMessage(userId,newGoal);
//        output.writeObject(message);
//        synchronized (input) {
//            ServerMessage reply=(ServerMessage) input.readObject();
//            reply.processMessage();
//        }
    }

    @Override
    public void drawFromDeck(UUID userId, int choice) throws IOException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, InvalidUserId, InvalidGoalException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalPositionException, InvalidCardException, ClassNotFoundException {
        DrawFromDeckMessage message= new DrawFromDeckMessage(choice,userId);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply=(ServerMessage) input.readObject();
            reply.processMessage();
        }
    }

    @Override
    public void drawVisibleCard(UUID userId, int choice) throws IOException, IsNotYourTurnException, HandFullException, IllegalOperationException, InvalidChoiceException, InvalidUserId, InvalidGoalException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalPositionException, InvalidCardException, DeckEmptyException, ClassNotFoundException {
        DrawVisibleMessage message= new DrawVisibleMessage(choice,userId);
        output.writeObject(message);
        synchronized (input) {
            ServerMessage reply=(ServerMessage) input.readObject();
            reply.processMessage();
        }
    }

    @Override
    public void closeGame(UUID userID) throws RemoteException, InvalidUserId {

    }

    @Override
    public boolean isGameEnded() throws RemoteException {
        return gd.isGameEnded();
    }

    @Override
    public boolean isGameStarted() throws RemoteException {
        return gd.isGameStarted();
    }

    @Override
    public List<Coordinates> getFieldBuildingHelper(String name) {
        return gd.getFieldBuilderHelper(name);
    }

    @Override
    public void initializeFieldBuildingHelper(String myName) {
        gd.setFieldBuilderHelper(myName,new ArrayList<>());
    }

    @Override
    public Reign getTopOfResourceCardDeck() {
        return null;
    }

    @Override
    public Reign getTopOfGoldCardDeck() {
        return null;
    }

    @Override
    public boolean amIPinged(UUID id) {
        return false;
    }

    @Override
    public void pong(UUID myID) {

    }

    public GameData getGameData() {
      return gd;
    }
}
