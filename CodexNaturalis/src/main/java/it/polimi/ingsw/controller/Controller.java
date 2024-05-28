package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewRMI;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

public class Controller {
    protected GameState currentState;
    private ConcurrentHashMap <UUID,Boolean> pingMap= new ConcurrentHashMap<>();
    private BlockingQueue<ServerMessage> messageQueue = new LinkedBlockingQueue<>();
    private ViewRMI view;
    /**
     * constructor of Controller, creates a new GameState
     * @author Giorgio Mattina
     * @param gameManager
     */
    public Controller( GameManager gameManager) throws RemoteException {
        currentState=new LobbyState(gameManager);
        view = new ViewRMI(this);
    }


    /**
     * Makes the transition to nextState
     * @author Giuseppe Laguardia
     */
    private synchronized void changeState()  {
        currentState=currentState.nextState();
        if(currentState.isGameStarted()){
            view.updateAll();
        }

    }
    /**
     * Checks if some users lost the connection to kick them from the game
     * @author Giusppe Laguardia
     * @throws RemoteException when a connection problem occurs
     */
    public void checkPong() throws RemoteException {
        List<UUID> kickedPlayers= new ArrayList<>();
        for (Map.Entry<UUID,Boolean> entry: pingMap.entrySet() ){
            UUID userID=entry.getKey();
            if(!entry.getValue()){
                closeGame(userID);
                kickedPlayers.add(userID);
            }
        }
        //removes kickedPlayer from pingMap
        for(UUID uID: kickedPlayers){
            pingMap.remove(uID);
        }
    }
    /**
     * Sets to true the value corresponding to the given userID, used to ensure that player didn't lost connection
     * @param userID the UUID of the user ensuring his connection
     * @author Giuseppe Laguardia
     */
    public void pong(UUID userID){
        pingMap.put(userID,true);
    }
    /**
     * Returns true if the user has received a ping from the server.
     * @return true the value corresponding to the given u
     */
    public boolean amIPinged(UUID userIDs){
        return pingMap.get(userIDs);
    }

    /**
     * Pings all the users, set to false all pingMap's value
     * @author Giuseppe Laguardia
     */
    public void ping() {
        for (Map.Entry<UUID,Boolean> entry: pingMap.entrySet() ){
            entry.setValue(false);
        }
    }

    /**
     * adds a ServerMessage to messagesQueue
     *@author Giuseppe Laguardia
     * @param msg the message needed to send to the server
     */
    public void addMessage(ServerMessage msg){
        try {
            messageQueue.put(msg);
        }catch (InterruptedException ignore){}
    }

    /**
     * gets and removes the first message of the queue
     * @author Giuseppe Laguardia
     * @return the first ServerMessage
     */
    public ServerMessage retrieveMessage(){
        try {
            return messageQueue.take();
        } catch (InterruptedException ignore) {}
        return null;
    }

    /**
     * call the BootGame method on currentState to create a new game if there aren't any pending otherwise joins the pending one.
     * After that updates the view
     * @author Giuseppe Laguardia
     * @param numOfPlayers the number of players wanted for the game, if there is another game pending thi parameter is ignored
     * @param playerName the name chosen by the user for the game
     * @return the user's identifier
     * @throws UnacceptableNumOfPlayersException if numOfPlayers is out of range [2,4]
     * @throws PlayerNameNotUniqueException if playerName is already taken by another user
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public  UUID bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException,  IllegalOperationException, OnlyOneGameException {
        UUID userID= currentState.BootGame(numOfPlayers,playerName);
        view.updatePlayersList();
        view.initializeFieldBuildingHelper(playerName);
        addMessage(new PlayersListMessage(getPlayersList()));
        pingMap.put(userID,true);
        changeState();
        return userID;
    }

    /**
     * calls joinGame on currentState with the player's name as a parameter, then updates the players list in view and
     * calls changeState()
     * @author Giorgio Mattina
     * @param playerName the player's username joining the game
     * @return the UUID returned by currentState
     * @throws NoGameExistsException
     * @throws IllegalOperationException
     * @throws PlayerNameNotUniqueException
     */
    public UUID joinGame(String playerName) throws NoGameExistsException, IllegalOperationException, PlayerNameNotUniqueException {
        UUID userID= currentState.joinGame(playerName);
        view.updatePlayersList();
        view.initializeFieldBuildingHelper(playerName);
        changeState();
        try {
            if(view.isGameStarted()){
                System.out.println("partita iniziata");
                GameStartMessage gameStartMessage = new GameStartMessage(getPublicGoals(),getVisibleCards(),getCurrentPlayer());
                addMessage(gameStartMessage);
            }
        } catch (RemoteException ignore){}
        pingMap.put(userID,true);
        return userID;
    }

    /**
     * Calls playCardFront on currentState to play a Card on front side.
     * After that updates the view.
     * @author Giuseppe Laguardia
     * @param selectedCard the card to be played
     * @param position the position on the field where to play the card
     * @param userId the user's identifier, needed to check if is user's turn
     * @throws IsNotYourTurnException if currentPlayer's userID doesn't match with userID passed as parameter
     * @throws RequirementsNotMetException if card is a GoldCard and the player doesn't meet the requirements to play the Card
     * @throws IllegalPositionException if the user choose an invalid position according to the rule
     * @throws InvalidCardException if the card chosen is not in user's hand
     * @throws HandNotFullException if the user already played a card this turn
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public synchronized void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        if(currentState.playCardFront(selectedCard, position, userId)){
            String winnerName = currentState.game.getWinner().getName();
            view.setWinner(winnerName);
            view.setIsGameEnded();
            addMessage(new GameEndMessage(winnerName));
        }
        view.updatePlayersHands();
        view.updatePlayersField();
        view.updatePlayersPoints();
        view.updateCurrentPlayer();
        view.updatePlayersLegalPosition();
        
       handlePlayCardSocketUpdate(userId);

        currentState=currentState.nextState();
    }
    /**
     * Calls playCardBack on currentState to play a Card on back side.
     * After that updates the view.
     * If playCardBack from controller returns true, calls nextState and sets the game's winner
     * @author Giuseppe Laguardia
     * @param selectedCard the card to be played
     * @param position the position on the field where to play the card
     * @param userId the user's identifier, needed to check if is user's turn
     * @throws IsNotYourTurnException if currentPlayer's userID doesn't match with userID passed as parameter
     * @throws RequirementsNotMetException if card is a GoldCard and the player doesn't meet the requirements to play the Card
     * @throws IllegalPositionException if the user choose an invalid position according to the rule
     * @throws InvalidCardException if the card chosen is not in user's hand
     * @throws HandNotFullException if the user already played a card this turn
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public synchronized void playCardBack(Card selectedCard, Coordinates position,UUID userId) throws HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException {
        if(currentState.playCardBack(selectedCard,position,userId)){
            String winnerName = currentState.game.getWinner().getName();
            view.setWinner(winnerName);
            view.setIsGameEnded();
            addMessage(new GameEndMessage(winnerName));
        }
        view.updatePlayersHands();
        view.updatePlayersField();
        view.updateCurrentPlayer();
        view.updatePlayersLegalPosition();

        handlePlayCardSocketUpdate(userId);

        currentState=currentState.nextState();
    }

    /**
     * adds to message queue the view updates for socket players caused by playCard
     * @param userId the player who play the card
     */
    private void handlePlayCardSocketUpdate(UUID userId) {
        String playerName = getUserIDs().get(userId).getName();
        try {
            addMessage(new FieldMessage(getPlayersField().get(playerName),getView().getFieldBuildingHelper(playerName),playerName));
            addMessage(new PointsMessage(getPlayersPoints()));
            addMessage(new TurnMessage(getCurrentPlayer()));
        } catch (RemoteException ignore) {}
    }

    /**
     * Calls chooseStarterCardSide on currentState, to play StarterCard on the side chosen by the user.
     * After that updates the view.
     * @author Giuseppe Laguardia
     * @param isFront true if the user decides to play StarterCard on front side, false if the user decides to play StarterCard on front side
     * @param userId the user's identifier, needed to check if is user's turn
     * @throws InvalidUserId if the given userId isn't associate to any Player
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public synchronized void chooseStarterCardSide(boolean isFront, UUID userId) throws InvalidUserId, IllegalOperationException, RemoteException {
        currentState.chooseStarterCardSide(isFront,userId);
        view.updateStarterCardMap();
        view.updatePlayersField();
        view.updatePlayersLegalPosition();
        String player = getUserIDs().get(userId).getName();
        addMessage(new FieldMessage(getPlayersField().get(player),getView().getFieldBuildingHelper(player), player));

        currentState=currentState.nextState();
    }

    /**
     * Calls chooseGoal on currentState,so that the player can choose his private Goal.
     * After that updates the view.
     * @author Giuseppe Laguardia
     * @param userId the user's identifier, needed to check if is user's turn
     * @param newGoal the Goal chosen by the player as private Goal
     * @throws InvalidGoalException the Goal given is not in GoalOption of the player
     * @throws InvalidUserId if the given userId isn't associate to any Player
     * @throws IllegalOperationException if in this state this action cannot be performed
     */

    public synchronized void ChooseGoal(UUID userId, Goal newGoal) throws InvalidGoalException, InvalidUserId, IllegalOperationException {
        currentState.chooseGoal(userId,newGoal);
        view.updatePrivateGoals();

        currentState=currentState.nextState();
    }

    /**
     * Calls drawFromDeck on the currentState to draw a card from the deck.
     * After that updates the view.
     * @param userId the user's identifier, needed to check if is user's turn
     * @param choice the int representing the two decks. O for ResourceDeck, 1 for GoldDeck
     * @throws IsNotYourTurnException if currentPlayer's userID doesn't match with userID passed as parameter
     * @throws HandFullException if the user's hand is full, meaning that he haven't played a card this turn yet
     * @throws DeckEmptyException if the chosen Deck is empty
     * @throws InvalidChoiceException if choice it's neither 0 nor 1
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public synchronized void drawFromDeck(UUID userId,int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException {
        currentState.drawFromDeck(userId, choice);

        view.updatePlayersHands();
        view.updateCurrentPlayer();
        addMessage(new TurnMessage(getCurrentPlayer()));
        if(choice==0){
            view.updateNumOfResourceCards();
            addMessage(new NumOfResourceCardsMessage(getNumOfResourceCards()));
        }
        else {
            view.updateNumOfGoldCards();
            addMessage(new NumOfGoldCardsMessage(getNumOfGoldCards()));
        }

        currentState=currentState.nextState();
    }
    /**
     * Calls drawVisibleCards on the currentState to draw one of the Card visible on the table.
     * After that updates the view.
     * @param userId the user's identifier, needed to check if is user's turn
     * @param choice the int representing the two decks. O for ResourceDeck, 1 for GoldDeck
     * @throws IsNotYourTurnException if currentPlayer's userID doesn't match with userID passed as parameter
     * @throws HandFullException if the user's hand is full, meaning that he haven't played a card this turn yet
     * @throws InvalidChoiceException if choice it's not in range [0:3]
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public synchronized void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException, IllegalOperationException, InvalidChoiceException {
        currentState.drawVisibleCard(userId,choice);

        view.updateCurrentPlayer();
        view.updatePlayersHands();
        view.updateVisibleCards();
        view.updateNumOfGoldCards();
        view.updateNumOfResourceCards();

        addMessage(new TurnMessage(getCurrentPlayer()));
        addMessage(new VisibleCardMessage(getVisibleCards()));
        addMessage(new NumOfGoldCardsMessage(getNumOfGoldCards()));
        addMessage(new NumOfResourceCardsMessage(getNumOfResourceCards()));


        currentState=currentState.nextState();
    }

    /**
     * Calls CloseGame on currentState to remove the user from the game
     * @author Giuseppe Laguardia
     * @param userID the users' identifier who's closing the game
     */
    public synchronized void closeGame(UUID userID) throws RemoteException {
        currentState.closeGame(userID);
        if(getUserIDs().size()<2){
            List<UUID> id = new ArrayList<>(currentState.userIDs.keySet());
            view.setWinner(currentState.getPlayerFromUid(id.getFirst()).getName());
            deleteGameFromGameManager();
        }
        view.updatePlayersList();
        view.updatePlayersHands();
        view.updatePrivateGoals();
        view.updatePlayersField();
        view.updateCurrentPlayer();
        view.updatePlayersPoints();

        String username= getUserIDs().get(userID).getName();
        addMessage(new PlayersListMessage(getPlayersList()));
        addMessage(new RemoveFieldMessage(username));
        addMessage(new TurnMessage(getCurrentPlayer()));
        addMessage(new PointsMessage(getPlayersPoints()));
    }

    /**
     * @author Giuseppe Laguardia
     * remove the game from the game manager
     * @throws RemoteException
     */
    public synchronized void deleteGameFromGameManager() throws RemoteException {
        currentState.deleteGameFromGameManager();
        view= new ViewRMI(this);
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return Map of each player's score
     */
    public HashMap<String, Integer> getPlayersPoints(){
        HashMap<String, Integer> scoreboard = new HashMap<>();
        List<Player> playersList = currentState.game.getPlayers();
        for (int i = 0; i< currentState.game.getNumOfPlayers();i++){
           scoreboard.put(playersList.get(i).getName(),playersList.get(i).getPoints());
        }
        return scoreboard;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of resource cards in the ResourceCardsDeck
     */
    public int getNumOfResourceCards(){
        return currentState.game.getResourceCardDeck().size();
    }
    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of gold cards in the ResourceCardsDeck
     */
    public int getNumOfGoldCards(){
        return currentState.game.getGoldCardDeck().size();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map of UUID,List of player's hand
     */
    public Map<UUID,List<Card>> getPlayersHands(){
        //the method will return hands
        Map<UUID,List<Card>> hands = new HashMap<>();
        //save the keyset of all the player's UUIDs
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id : set){
            hands.put(id,currentState.getPlayerFromUid(id).getHand());
        }
        return hands;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return a map of all player's fields (cards on the table)
     */
    public Map<String,Map<Coordinates,Card>> getPlayersField(){
        Map<String,Map<Coordinates,Card>> fields = new HashMap<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id: set){
            fields.put(currentState.getPlayerFromUid(id).getName(),currentState.getPlayerFromUid(id).getField());
        }
        return fields;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of the player's names
     */
    public List<String> getPlayersList(){
        List<String> names = new ArrayList<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id :set){
            names.add(currentState.getPlayerFromUid(id).getName());
        }
        return names;
    }

    /**
     * @author Giorgio Mattina
     * @return the name of the current player
     */
    public String getCurrentPlayer(){
        return currentState.game.getCurrentPlayer().getName();
    }

    /**
     * @author Giorgio Mattina, Maximilan Mangosi
     * @return all player's legal positions
     */
    public Map<UUID,List<Coordinates>> getPlayersLegalPositions(){
        Map<UUID,List<Coordinates>> legal = new HashMap<>();
        Set <UUID> set = currentState.userIDs.keySet();
        for (UUID id :set){
            legal.put(id,currentState.getPlayerFromUid(id).getAvailablePositions());
        }
        return legal;
    }

    /**
     *
     * @author Giorgio Mattina
     * @return the array of public goals to the view
     */
    public Goal[] getPublicGoals(){
        return currentState.game.getPublicGoals();
    }

    /**
     *
     * @author Giorgio Mattina
     * @return the map plauerId-ArrayOfGoalOptions to the view
     */
    public Map<UUID,Goal[]> getGoalOptions(){
        Map<UUID,Goal[]> options = new HashMap<>();
        Set<UUID> set = currentState.userIDs.keySet();
        for (UUID id:set){
            options.put(id,currentState.getPlayerFromUid(id).getGoalOptions());
        }
        return options;
    }

    /**
     * @author Giorgio Mattina
     * @return the map playerId-privateGoal
     */
    public Map<UUID,Goal> getPrivateGoals(){
        Map<UUID,Goal> privateGoals = new HashMap<>();
        Set<UUID> set = currentState.userIDs.keySet();
        for(UUID id:set){
            privateGoals.put(id,currentState.getPlayerFromUid(id).getGoal());
        }
        return privateGoals;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of visible cards
     */
    public List<Card> getVisibleCards(){
        return currentState.game.getVisibleCards();
    }

    /**
     * @author Giuseppe Laguardia
     * @return the user ids
     */
    public  Map<UUID,Player> getUserIDs(){
        return currentState.userIDs;
    }

    /**
     * @author Giuseppe Laguardia
     * @return the current state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * @author Giuseppe Laguardia
     * @return if the game id ended
     */
    public boolean isGameEnded() {
        return currentState.isGameEnded();
    }

    /**
     * @author Giuseppe Laguardia
     * @return the current game
     */
    public Game getGame() {
        return currentState.game;
    }

    /**
     * @author Giorgio Mattina
     * @return view attribute
     */
    public ViewRMI getView(){
        return  view;
    }

    /**
     * @author Giuseppe Laguardia
     * @return the player starter cards
     */
    public HashMap<UUID, StarterCard> getPlayersStarterCards(){
        HashMap<UUID, StarterCard> starterCardMap = new HashMap<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id: set){
            starterCardMap.put(id,currentState.getPlayerFromUid(id).getStarterCard());
        }
        return starterCardMap;
    }

    /**
     * @author Giuseppe Laguardia
     * @return the top (first) card of the Resource cards deck
     */
    public Reign getTopOfResourceCardDeck() {
        return getGame().getResourceCardDeck().getFirst().getReign();
    }
    /**
     * @author Giuseppe Laguardia
     * @return the top (first) card of the Gold cards deck
     */
    public Reign getTopOfGoldCardDeck() { return getGame().getGoldCardDeck().getFirst().getReign();
    }


}
