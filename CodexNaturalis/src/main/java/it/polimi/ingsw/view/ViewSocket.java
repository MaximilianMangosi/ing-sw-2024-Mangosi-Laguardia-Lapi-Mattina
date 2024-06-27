package it.polimi.ingsw.view;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.GameKey;
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
import it.polimi.ingsw.model.gamelogic.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * A View implementation using Socket, used to interact with the server to send commands and receive data
 */
public class  ViewSocket implements View{
    private final String serverAddress;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final GameData gd;
    /**
     * Constructs a ViewSocket with the specified output stream, input stream, and game data.
     * @author Giuseppe Laguardia
     * @param server  The server socket
     * @param gameData  The game data.
     * @throws IOException if an I/O error occurs.
     */
    public ViewSocket(Socket server, GameData gameData) throws IOException, ClassNotFoundException {
        this.serverAddress=server.getInetAddress().getHostAddress();
        this.output = new ObjectOutputStream(server.getOutputStream());
        this.input = new ObjectInputStream(server.getInputStream());
        this.gd=gameData;
        // get joinable games from server
        readMessage().processMessage(this);

    }
    /**
     * Gets the output stream.
     *
     * @return The output stream.
     */
    public ObjectOutputStream getOutput() {
        return output;
    }
    /**
     * Gets the input stream.
     *
     * @return The input stream.
     */
    public ObjectInputStream getInput() {
        return input;
    }
    /**
     * Indicates whether this view is using RMI.
     *
     * @return Always returns false for socket implementation.
     */
    @Override
    public boolean isRMI() {
        return false;
    }
    /**
     * Retrieves the points of all players in the game.
     *
     * @return A map containing player names as keys and their respective points as values.
     */
    @Override
    public Map<String, Integer> getPlayersPoints()  {
        return gd.getPlayersPoints();
    }
    /**
     * Retrieves the number of resource cards remaining in the deck.
     *
     * @return The number of resource cards.
     */
    @Override
    public int getNumOfResourceCards()  {
       return gd.getNumOfResourceCards();
    }
    /**
     * Retrieves the number of gold cards remaining in the deck.
     *
     * @return The number of resource cards.
     */
    @Override
    public int getNumOfGoldCards() {
        return gd.getNumOfGoldCards();
    }
/**
 * This method is deprecated and will always return null. Use {@link #showPlayerHand()} instead.
 * @return Always returns null.
 */
    @Override
    @Deprecated
    public List<Card> showPlayerHand(UUID uid) {
        return null;
    }
/**
 * Retrieves the hand of the current player.
 * @return a list of cards representing the current player's hand.
 */
    @Override
    public List<Card> showPlayerHand(){
        return gd.getHand();
    }

    /**
     * @author Giuseppe Laguardia
     * @param name
     * @return player field
     */
    @Override
    public Map<Coordinates, Card> getPlayersField(String name) {
        return gd.getPlayerField(name);
    }

    /**
     * @author Giuseppe Laguardia
     * @return player list
     */
    @Override
    public List<String> getPlayersList() {
        return gd.getPlayersList();
    }

    /**
     * @author Giuseppe Laguardia
     * @return curr players
     * @throws RemoteException
     */
    @Override
    public String getCurrentPlayer() throws RemoteException {
        return gd.getCurrentPlayer();
    }
    /**
    * This method is deprecated and will always return null. Use {@link #showPlayersLegalPositions()}  instead.
    * @return Always returns null.
    */
    @Override
    @Deprecated
    public List<Coordinates> showPlayersLegalPositions(UUID uid) {
        return null;
    }
    /**
     * Retrieves the legal positions where the current player can place cards.
     *
     * @return The list of legal positions.
     */
    @Override
    public List<Coordinates> showPlayersLegalPositions()  {
        return gd.getLegalPositions();
    }
    /**
     * Retrieves the public goals available in the game.
     * @return An array of public goals.
     */
    @Override
    public Goal[] getPublicGoals() {
        return gd.getPublicGoals();
    }
    /**
     * This method is deprecated and will always return null. Use {@link #showPlayerGoalOptions()}  instead.
     * @return Always returns null.
     */
    @Override
    @Deprecated
    public Goal[] showPlayerGoalOptions(UUID uid) {
        return null;
    }
    /**
     * Retrieves the list of goal options available for user.
     *
     * @return An array of goal options.
     */
    @Override
    public Goal[] showPlayerGoalOptions()  {
        return gd.getGoalOptions();
    }
    /**
     * This method is deprecated and will always return null. Use {@link #showPrivateGoal()}   instead.
     * @return Always returns null.
     */
    @Override
    @Deprecated
    public Goal showPrivateGoal(UUID uid) {
        return  null;
    }
    /**
     * Retrieves the private goal of user.
     *
     * @return The private goal of the user.
     */
    @Override
    public Goal showPrivateGoal() {
        return gd.getPrivateGoal();
    }
    /**
     * Retrieves the list of visible cards in the game.
     *
     * @return The list of visible cards.
     */
    @Override
    public List<Card> getVisibleCards() {
        return gd.getVisibleCards();
    }
    /**
     * Retrieves the winner of the game.
     *
     * @return The name of the winner.
     */
    @Override
    public String getWinner() {
        return gd.getWinner();
    }
    /**
     * This method is deprecated and will always return null. Use {@link #getStarterCard()}   instead.
     * @return Always returns null.
     */
    @Override
    @Deprecated
    public StarterCard getStarterCard(UUID userId)  {
        return  null;
    }
    /**
     * Retrieves the starter card of the user.
     *
     * @return The starter card assigned to the player.
     * */
    @Override
    public StarterCard getStarterCard() throws RemoteException {
        return gd.getStarterCard();
    }

    /**
     * @author Giuseppe Laguardia
     * @param player
     * @return player color
     */
    @Override
    public String getPlayerColor(String player) {
        return gd.getPlayerToColor(player);
    }

    /**
     * @author Giuseppe Laguardia
     * @return message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private ServerMessage readMessage() throws IOException, ClassNotFoundException {
        ServerMessage reply;
        reply = (ServerMessage) input.readObject();
        return reply;
    }
    /**
     * Sends to server a message for creating a new Game, and wait for his reply
     *
     * @param numOfPlayers the int representing how many player can join the game
     * @param playerName   the username of the player creating the Game
     * @return the UUID that identifies the client on the server
     * @throws IOException                       when there's a problem during message (de)serialization
     * @throws ClassNotFoundException            when Class of a serialized object cannot be found
     * @throws UnacceptableNumOfPlayersException when numOfPlayers is not in range [2:4]
     * @throws IllegalOperationException         when in the current phase of the game bootGame is not admissible
     */
    @Override
    public synchronized GameKey bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, ClassNotFoundException, IllegalOperationException, IOException, PlayerNameNotUniqueException {
        BootGameMessage message = new BootGameMessage(numOfPlayers,playerName);
        output.writeObject(message);
        ServerMessage reply = readMessage(); // this should be a GameKeyMessage from the server
        try {
            reply.processMessage();
        } catch (IsNotYourTurnException | InvalidChoiceException | DeckEmptyException | RequirementsNotMetException |
                 IllegalPositionException | InvalidCardException | HandNotFullException | InvalidUserId |
                 InvalidGoalException | HandFullException | InvalidGameID ignore) {}
        // if processMessage didn't throw an exception then the reply of the server is a GameKeyMessage
       return  ((GameKeyMessage) reply).getGameKey();
    }

    /**
     * joins the game
     * @author Giuseppe Laguardia
     * @param gameId
     * @param playerName
     * @return
     * @throws PlayerNameNotUniqueException
     * @throws IllegalOperationException
     * @throws InvalidGameID
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public synchronized UUID joinGame(UUID gameId,String playerName) throws PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID, IOException, ClassNotFoundException {
        JoinGameMessage message = new JoinGameMessage(gameId,playerName);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException   |
                 IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException |
                 InvalidCardException | HandNotFullException | InvalidUserId | InvalidGoalException |
                 HandFullException | DeckEmptyException | InvalidChoiceException ignore) {}
        // if processMessage didn't throw an exception then the reply of the server is an UserIDMessage
        return ((UserIDMessage) reply).getYourID();

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
     * @throws RequirementsNotMetException when the player doesn't have the rights to play the card
     * @throws IllegalPositionException when the client plays the card in an inadmissible position
     * @throws InvalidCardException when the client plays a card that is
     */
    @Override
    public synchronized void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IOException, ClassNotFoundException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalOperationException, IllegalPositionException, InvalidCardException {
        PlayCardMessage message= new PlayCardMessage(selectedCard,position,userId,true);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | InvalidGameID | PlayerNameNotUniqueException | InvalidUserId |
                 InvalidGoalException | HandFullException | DeckEmptyException | InvalidChoiceException ignore) {}

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
     * @throws IllegalPositionException when the client plays the card in an inadmissible position
     * @throws InvalidCardException when the client plays a card that is
     */
    @Override
    public synchronized void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId, IOException, ClassNotFoundException {
        PlayCardMessage message= new PlayCardMessage(selectedCard,position,userId,false);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | InvalidGameID | PlayerNameNotUniqueException | InvalidUserId |
                 InvalidGoalException | HandFullException | DeckEmptyException | InvalidChoiceException |
                 RequirementsNotMetException ignore) {}
    }

    /**
     * Sends a message containing the side on which play the StarterCard, then wait for server's reply
     * @param isFront the side on which you play the StarterCard
     * @param userId the UUID that identifies the user
     * @throws IOException when there's a problem during message (de)serialization
     * @throws IllegalOperationException when in the current phase of the game bootGame is not admissible
     * @throws ClassNotFoundException when Class of a serialized object cannot be found
     * @throws InvalidUserId when the UserID is not in userId map on the Controller
     */
    @Override
    public synchronized void chooseStarterCardSide(boolean isFront, UUID userId) throws RemoteException, IllegalOperationException, InvalidUserId, IOException, ClassNotFoundException {
        ChooseStarterCardMessage message= new ChooseStarterCardMessage(isFront,userId);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | InvalidGameID | InvalidChoiceException |
                 PlayerNameNotUniqueException | IsNotYourTurnException | RequirementsNotMetException |
                 IllegalPositionException | InvalidCardException | HandNotFullException | InvalidGoalException |
                 HandFullException | DeckEmptyException ignore) {
        }
    }

    /**
     * Sends a message containing the Goal chosen by the user, then wait's for server's reply
     * @param userId the UUID that identifies the user
     * @param newGoal the Goal chosen
     * @throws InvalidGoalException if the goal chosen isn't in the goal's options of the user
     * @throws InvalidUserId if the userID is not one of the player's in game
     * @throws IllegalOperationException if this action isn't permitted in this phase
     * @throws IOException if a connection error occurs
     *
     */
  @Override
    public synchronized void chooseGoal(UUID userId, Goal newGoal) throws IOException, InvalidGoalException, IllegalOperationException, ClassNotFoundException, InvalidUserId {
      ChooseGoalMessage message= new ChooseGoalMessage(userId,newGoal);
        output.writeObject(message);
        ServerMessage reply = readMessage();
      try {
          reply.processMessage();
      } catch (UnacceptableNumOfPlayersException | PlayerNameNotUniqueException | IsNotYourTurnException |
               RequirementsNotMetException | IllegalPositionException | InvalidCardException | HandNotFullException |
               InvalidGameID | InvalidChoiceException | HandFullException | DeckEmptyException ignore) {
      }
  }

    /**
     * draw card form deck
     * @author Giuseppe Laguardia
     * @param userId
     * @param choice
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws HandFullException
     * @throws InvalidChoiceException
     * @throws IsNotYourTurnException
     * @throws IllegalOperationException
     * @throws DeckEmptyException
     */
    @Override
    public synchronized void drawFromDeck(UUID userId, int choice) throws IOException, ClassNotFoundException, HandFullException, InvalidChoiceException, IsNotYourTurnException, IllegalOperationException, DeckEmptyException {

        DrawFromDeckMessage message= new DrawFromDeckMessage(choice,userId);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | PlayerNameNotUniqueException | RequirementsNotMetException |
                 IllegalPositionException | InvalidCardException | HandNotFullException | InvalidUserId |
                 InvalidGoalException | InvalidGameID ignore) {}
    }

    /**
     * draw card form visible cards
     * @author Giuseppe Laguardia
     * @param userId
     * @param choice
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws HandFullException
     * @throws InvalidChoiceException
     * @throws IsNotYourTurnException
     * @throws IllegalOperationException
     * @throws DeckEmptyException
     */
    @Override
    public synchronized void drawVisibleCard(UUID userId, int choice) throws IOException, ClassNotFoundException, HandFullException, InvalidChoiceException, IsNotYourTurnException, IllegalOperationException, DeckEmptyException {
        DrawVisibleMessage message= new DrawVisibleMessage(choice,userId);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | PlayerNameNotUniqueException | RequirementsNotMetException |
                 IllegalPositionException | InvalidCardException | HandNotFullException | InvalidUserId |
                 InvalidGoalException | InvalidGameID ignore) {}
    }

    /**
     * close the game
     * @author Giuseppe Laguardia
     * @param userID
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Override
    public synchronized void closeGame(UUID userID) throws ClassNotFoundException,IOException{
        CloseGameMessage message= new CloseGameMessage(userID);
        output.writeObject(message);
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | InvalidGameID | InvalidChoiceException | DeckEmptyException |
                 IllegalOperationException | PlayerNameNotUniqueException | IsNotYourTurnException |
                 RequirementsNotMetException | IllegalPositionException | InvalidCardException | HandNotFullException |
                 InvalidUserId | InvalidGoalException | HandFullException ignore) {}
    }

    /**
     * checks if the game is ended
     * @author Giuseppe Laguardia
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean isGameEnded() throws RemoteException {
        return gd.isGameEnded();
    }

    /**
     * checks if the game has started
     * @author Giuseppe Laguardia
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean isGameStarted() throws RemoteException {
        return gd.isGameStarted();
    }

    /**
     * @author Giuseppe Laguardia
     * @param name
     * @return field building helper
     */
    @Override
    public List<Coordinates> getFieldBuildingHelper(String name) {
        return gd.getFieldBuilderHelper(name);
    }

    /**
     * initializes the field building helper
     * @author Giuseppe Laguardia
     * @param myName
     */
    @Override
    public void initializeFieldBuildingHelper(String myName) {
        gd.setFieldBuilderHelper(myName,new ArrayList<>());
    }

    /**
     * updates the field building helper
     * @author Giuseppe Laguardia
     * @param position
     * @param username
     */
    @Override
    public void updateFieldBuildingHelper(Coordinates position, String username) {
        gd.getFieldBuilderHelper(username).add(position);
    }

    /**
     * @author Giuseppe Laguardia
     * @return top resource card from resource deck
     */
    @Override
    public Reign getTopOfResourceCardDeck() {
        return gd.getTopOfResourcesDeck();
    }

    /**
     * @author Giuseppe Laguardia
     * @return top of gold card deck
     */
    @Override
    public Reign getTopOfGoldCardDeck() {
        return gd.getTopOfGoldsDeck();
    }

    /**
     * @author Giuseppe Laguardia
     * @param id
     * @return if pinged
     */
    @Override
    public boolean amIPinged(UUID id) {
        return false;
    }

    /**
     * @author Giuseppe Laguardia
     * @param myID
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public synchronized void pong(UUID myID) throws IOException, ClassNotFoundException {
        output.writeObject(new PongMessage(myID));
        ServerMessage reply = readMessage();
        try {
            reply.processMessage();
        } catch (UnacceptableNumOfPlayersException | IllegalOperationException | PlayerNameNotUniqueException |
                 IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException |
                 InvalidCardException | HandNotFullException | InvalidUserId | InvalidGoalException |
                 HandFullException | DeckEmptyException | InvalidChoiceException | InvalidGameID ignore) {}
    }

    /**
     * @author Maximilian Mangosi
     * @return chat list
     */
    @Override
    public List<String> getChatList() {
        return gd.getChatData();
    }

    /**
     * sends private message
     * @author Giuseppe Laguardia
     * @param receiver
     * @param message
     * @param sender
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public synchronized void sendPrivateMessage(String receiver, String message, UUID sender) throws IOException, ClassNotFoundException {
      output.writeObject(new PrivateChatMessage(message,receiver,sender));
      ServerMessage reply = readMessage();
      try {
            reply.processMessage();
      } catch (UnacceptableNumOfPlayersException | IllegalOperationException | PlayerNameNotUniqueException |
               IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException | InvalidCardException |
               HandNotFullException | InvalidUserId | InvalidGoalException | HandFullException | DeckEmptyException |
               InvalidChoiceException | InvalidGameID ignore) {}
    }

    @Override
    @Deprecated
    public List<String> getPrivateChat(String receiver, UUID uuid) {
        return null;
    }

    /**
     * @author Giuseppe Laguardia
     * @param user
     * @return private chat
     */
    public List<String> getPrivateChat(String user)  {
        return gd.getPrivateChat(user);
    }

    /**
     * sends chat message
     * @author Maximilian Mangosi
     * @param message
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalOperationException
     */
    @Override
    public synchronized void sendChatMessage(String message) throws IOException, ClassNotFoundException, IllegalOperationException {
        try {
            output.writeObject(new ChatMessage(message));
            ServerMessage reply = readMessage();
            reply.processMessage();

        }catch (InvalidGoalException | HandFullException | InvalidChoiceException | IsNotYourTurnException |
                UnacceptableNumOfPlayersException | PlayerNameNotUniqueException | InvalidCardException |
                DeckEmptyException | InvalidGameID | HandNotFullException | InvalidUserId |
                RequirementsNotMetException | IllegalPositionException ignore) {}
    }

    /**
     * @author Giuseppe Laguardia
     * @return game data
     */
    public GameData getGameData() {
      return gd;
    }

    /**
     * @author Giuseppe Laguardia
     * @return joinable games list
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Map<UUID, List<String>> getJoinableGames() throws IOException, ClassNotFoundException{
      output.writeObject(new RequestJoinableGamesMessage());
      readMessage().processMessage(this);
      return gd.getJoinableGames();
    }

}
