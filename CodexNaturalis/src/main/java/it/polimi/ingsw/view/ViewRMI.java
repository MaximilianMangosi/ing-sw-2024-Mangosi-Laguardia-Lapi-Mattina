package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ViewRMI extends UnicastRemoteObject implements ViewRMIInterface {
    private final Controller controller;
    private boolean isGameStarted;
    private boolean isGameEnded;
    private Map<String, Integer> playersPoints;
    private String winner;
    private int numOfResourceCards;
    private int numOfGoldCards;
    private Map<UUID, List<Card>> playersHands;
    private Map<String, Map<Coordinates, Card>> playersField;
    private List<String> playersList;
    private String currentPlayer;
    private Map<UUID,List<Coordinates>> playersLegalPositions;
    private Goal[] publicGoals;
    private Map<UUID,Goal[]> playersGoalOptions;
    private Map<UUID,Goal> privateGoals;
    private List<Card> visibleCards;
    private HashMap<UUID,StarterCard> starterCardMap;
    private HashMap<String, List<Coordinates>> fieldBuildingHelper=new HashMap<>();
    private List<String> globalChat = new ArrayList<>(250);
    private Map<UUID, Map<String, List<String>>> privateChat = new HashMap<>();

    public void sendChatMessage(String message) throws IllegalOperationException, RemoteException {
        controller.addToGlobalChat(message);
    }

    public void sendPrivateMessage(String receiver, String message, UUID sender) throws IllegalOperationException ,RemoteException{
        controller.addMessage(receiver, message, sender);
    }

    public List<String> getPrivateChat(String receiver, UUID uuid) throws RemoteException{
        return privateChat.get(uuid).get(receiver);
    }

    @Override
    @Deprecated
    public List<String> getPrivateChat(String user) {
        return null;
    }

    public ViewRMI(Controller controller) throws RemoteException {
        this.controller=controller;
    }

    /**
     * updates PlayersPoints, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updatePlayersPoints(){
        playersPoints=controller.getPlayersPoints();
    }

    @Override
    public boolean isRMI() throws RemoteException {
        return true;
    }

    /**
     * @author Giorgio Mattina
     * @return map nickname-points for each player
     */
    public Map<String, Integer> getPlayersPoints(){
        return playersPoints;
    }
    /**
     * updates NumOfResourceCardsMessage, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updateNumOfResourceCards(){
        numOfResourceCards=controller.getNumOfResourceCards();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of resource cards in the deck
     */
    public int getNumOfResourceCards(){
        return numOfResourceCards;
    }
    /**
     * updates NumOfGoldCardsMessage, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updateNumOfGoldCards(){
        numOfGoldCards=controller.getNumOfGoldCards();
    }

    /**
     * @author Giorgio Mattina
     * @return number of gold cards in the GoldCardDeck
     */
    public int getNumOfGoldCards(){
        return numOfGoldCards;
    }
    /**
     * updates playersHands, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public  synchronized void updatePlayersHands(){
        playersHands=controller.getPlayersHands();
    }

    /**
     * @author Giorgio Mattina,Maximilian Mangosi
     * @return Map of playerId-list of card in player's hand
     */
    public Map<UUID, List<Card>> getPlayersHands(){
        return  playersHands;
    }
    /**
     * updates PlayersField, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updatePlayersField(){
        playersField=controller.getPlayersField();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map playerId-playerField ( cards on the table with relative position)
     */
    public Map<Coordinates,Card> getPlayersField(String name){

        return playersField.get(name);
    }
    /**
     * updates PlayersList, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updatePlayersList(){
        playersList=controller.getPlayersList();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of players in the game
     */
    public List<String> getPlayersList(){
        return playersList;
    }
    /**
     * updates currentPlayer, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updateCurrentPlayer(){
        currentPlayer=controller.getCurrentPlayer();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return the current player's name
     */
    public String getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * @author Giuseppe Laguardia and Riccardo Lapi
     * @param uid
     * @return the legal position of the player with uid as the passed one
     * @throws RemoteException
     * @throws InvalidUserId
     */
    @Override
    public List<Coordinates> showPlayersLegalPositions(UUID uid) throws RemoteException, InvalidUserId {
        return playersLegalPositions.get(uid);
    }

    @Override
    @Deprecated
    public List<Coordinates> showPlayersLegalPositions() throws RemoteException, InvalidUserId {
        return null;
    }

    /**
     * updates PlayersLegalPositions, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updatePlayersLegalPosition(){
        playersLegalPositions=controller.getPlayersLegalPositions();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map of list of available positions with relative player ID
     */
    public Map<UUID,List<Coordinates>> getPlayersLegalPositions(){
        return playersLegalPositions;
    }

    /**
     * updates public goals
     * @author Giorgio Mattina
     */
    public synchronized void updatePublicGoals(){
        publicGoals=controller.getPublicGoals();
    }

    /**
     * @author Giorgio Mattina
     * @return array of public Goals
     */
    public Goal[] getPublicGoals(){
        return publicGoals;
    }

    /**
     * updates all the player's initial goal options
     * @author Giorgio Mattina
     */
    public synchronized void updatePlayersGoalOptions(){
        playersGoalOptions=controller.getGoalOptions();
    }
    /**
     * @author Giorgio Mattina
     * @return all player's initial goal options
     */
    public Map<UUID, Goal[]> getPlayersGoalOptions(){
        return playersGoalOptions;
    }

    /**
     * updates the map player-goal
     * @author Giorgio Mattina
     */
    public synchronized void updatePrivateGoals(){
        privateGoals=controller.getPrivateGoals();
    }

    /**
     * @author Giorgio Mattina
     * @return map of player-private goal from view
     */
    public Map<UUID,Goal> getPrivateGoals(){
        return privateGoals;
    }

    /**
     * updates the list of visibleCards
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public synchronized void updateVisibleCards(){
        visibleCards=controller.getVisibleCards();
    }

    /**
     * @author Giuseppe Laguardia
     * set isGameEnded to true
     */
    public void setIsGameEnded(){
        isGameEnded=true;
    }
    /**
     * @author Giuseppe Laguardia
     * set isGameStarted to true
     */
    public void setIsGameStarted(){
        isGameStarted=true;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of visible cards from view
     */
    public List<Card> getVisibleCards(){
        return visibleCards;
    }

    /**
     * @author Riccardo Lapi
     * @return the winner
     * @throws RemoteException
     */
    @Override
    public String getWinner() throws RemoteException {
        return winner;
    }

    /**
     * @author Riccardo Lapi
     * @param numOfPlayers the numbers of player for the new created game
     * @param playerName the username of the caller user
     * @return the uid of the caller user
     * @throws RemoteException
     * @throws UnacceptableNumOfPlayersException
     * @throws PlayerNameNotUniqueException
     * @throws IllegalOperationException
     */
    @Override
    public synchronized UUID bootGame(int numOfPlayers, String playerName) throws RemoteException, UnacceptableNumOfPlayersException, IllegalOperationException, OnlyOneGameException {
        return controller.bootGame(numOfPlayers, playerName);
    }
    public synchronized UUID joinGame(String playerName) throws NoGameExistsException, PlayerNameNotUniqueException, IllegalOperationException {
        return controller.joinGame(playerName);
    }
    /**
     * @author Riccardo Lapi
     * Place the selectedCard facing up and adds it to the fieldBuildingHelper
     * @param selectedCard the card to play
     * @param position the position where to place the card
     * @param userId the caller user id (given by BootGame)
     * @throws RemoteException
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     * @throws IllegalPositionException
     * @throws InvalidCardException
     * @throws HandNotFullException
     * @throws IllegalOperationException
     * @throws InvalidUserId
     */
    @Override
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException, InvalidUserId {
        controller.playCardFront(selectedCard, position, userId);
        updateFieldBuildingHelper(position,controller.getUserIDs().get(userId).getName());
    }

    /**
     * @author Riccardo Lapi
     * Place the selectedCard facing down and adds it to the fieldBuildingHelper
     * @param selectedCard the card to play
     * @param position the position where to place the card
     * @param userId the caller user id (given by BootGame)
     * @throws RemoteException
     * @throws HandNotFullException
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     * @throws IllegalPositionException
     * @throws IllegalOperationException
     * @throws InvalidCardException
     * @throws InvalidUserId
     */
    @Override
    public void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws RemoteException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException, InvalidUserId {
        controller.playCardBack(selectedCard, position, userId);
        updateFieldBuildingHelper(position, controller.getUserIDs().get(userId).getName());
    }

    public void updateFieldBuildingHelper(Coordinates position, String username)  {
        fieldBuildingHelper.get(username).add(position);
    }

    /**
     * @author Riccardo Lapi
     * @param isFront boolean that indicate if the starter card should be facing up (true) or down (false)
     * @param userId the caller user id (given by BootGame)
     * @throws RemoteException
     * @throws InvalidUserId
     * @throws IllegalOperationException
     * @throws InvalidUserId
     */
    @Override
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws RemoteException, InvalidUserId, IllegalOperationException, InvalidUserId {
        controller.chooseStarterCardSide(isFront, userId);
        updateFieldBuildingHelper(new Coordinates(0,0),controller.getUserIDs().get(userId).getName());
    }

    /**
     * @author Riccardo Lapi
     * @param userId the caller user id (given by BootGame)
     * @param newGoal the chosen goal
     * @throws RemoteException
     * @throws InvalidGoalException
     * @throws InvalidUserId
     * @throws IllegalOperationException
     * @throws InvalidUserId
     */
    @Override
    public void chooseGoal(UUID userId, Goal newGoal) throws RemoteException, InvalidGoalException, InvalidUserId, IllegalOperationException, InvalidUserId {
        controller.ChooseGoal(userId, newGoal);
    }

    /**
     * @author Riccardo Lapi
     * @param userId the caller user id (given by BootGame)
     * @param choice the chosen deck 0 or 1
     * @throws RemoteException
     * @throws IsNotYourTurnException
     * @throws HandFullException
     * @throws DeckEmptyException
     * @throws IllegalOperationException
     * @throws InvalidChoiceException
     * @throws InvalidUserId
     */
    @Override
    public void drawFromDeck(UUID userId, int choice) throws RemoteException, IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException, InvalidUserId {
        controller.drawFromDeck(userId, choice);
    }

    /**
     * @author Riccardo Lapi
     * @param userId the caller user id (given by BootGame)
     * @param choice the chosen card to draw int 0,1,2 or 3
     * @throws RemoteException
     * @throws IsNotYourTurnException
     * @throws HandFullException
     * @throws IllegalOperationException
     * @throws InvalidChoiceException
     * @throws InvalidUserId
     */
    @Override
    public void drawVisibleCard(UUID userId, int choice) throws RemoteException, IsNotYourTurnException, HandFullException, IllegalOperationException, InvalidChoiceException, InvalidUserId {
        controller.drawVisibleCard(userId, choice);
    }

    /**
     * @author Riccardo Lapi
     * @param userID the caller user id (given by BootGame)
     * @throws RemoteException
     * @throws InvalidUserId
     */
    @Override
    public void closeGame(UUID userID) throws RemoteException, InvalidUserId {
        controller.closeGame(userID);
    }

    /**
     * @author Giuseppe Lagurdia
     * @return if the game is ended
     * @throws RemoteException
     */

    @Override
    public boolean isGameEnded() throws RemoteException {
        return isGameEnded;
    }

    /**
     * @author Giuseppe Lagurdia
     * @return if the game is started
     * @throws RemoteException
     */
    @Override
    public boolean isGameStarted() throws RemoteException {
        return isGameStarted;
    }

    /**
     * @author Maximilian Mangosi
     * @param name the username of the player
     * @return the fieldBuildingHelper for that player
     * @throws RemoteException
     */
    @Override
    public List<Coordinates> getFieldBuildingHelper(String name) throws RemoteException {
        return fieldBuildingHelper.get(name);
    }

    /**
     * @author Giuseppe Lagurdia
     * @param myName
     * @throws RemoteException
     */
    @Override
    public void initializeFieldBuildingHelper(String myName) {
        fieldBuildingHelper.put(myName,new ArrayList<>());
    }

    /**
     * @author Giuseppe Lagurdia
     * @return the getTopOfResourceCardDeck
     * @throws RemoteException
     */
    @Override
    public Reign getTopOfResourceCardDeck() throws RemoteException {
        return controller.getTopOfResourceCardDeck();
    }
    /**
     * @author Giuseppe Lagurdia
     * @return the getTopOfGoldCardDeck
     * @throws RemoteException
     */
    @Override
    public Reign getTopOfGoldCardDeck() throws RemoteException {
        return controller.getTopOfGoldCardDeck();
    }
    /**
     * Returns the return value of controller's amIPinged method
     * @param id the UUID of the user
     * @return the boolean returned by controller
     * @throws RemoteException when a connection error occurs
     */
    @Override
    public boolean amIPinged(UUID id) throws RemoteException {
        return controller.amIPinged(id);
    }

    /**
     * Calls controller's pong method
     * @param myID the UUID of the user
     * @throws RemoteException when a connection error occurs
     */
    @Override
    public void pong(UUID myID) throws RemoteException {
        controller.pong(myID);
    }

    @Override
    public List<String> getChatList() {
        return globalChat;
    }

    /**
     * @author Giorgio Mattina
     * update all the all the info
     */
    public synchronized void updateAll() {
        updatePlayersPoints();
        updateNumOfResourceCards();
        updateNumOfGoldCards();
        updatePlayersHands();
        updatePlayersField();
        updatePlayersList();
        updateCurrentPlayer();
        updatePlayersLegalPosition();
        updatePublicGoals();
        updatePlayersGoalOptions();
        updatePrivateGoals();
        updateVisibleCards();
        updateStarterCardMap();
        setIsGameStarted();
        updateGlobalChat();
        updatePrivateChat();
    }



    public void updatePrivateChat() {
        privateChat=controller.getAllPrivateChat();
    }

    public void updateGlobalChat() {
        globalChat = controller.getGlobalChat();
    }

    /**
     * @author Giorgio Mattina
     * @param id
     * @return
     */
    public List<Card> showPlayerHand(UUID id){
        //updatePlayersHands();
        return playersHands.get(id);

    }

    @Override
    @Deprecated
    public List<Card> showPlayerHand() throws RemoteException, InvalidUserId {
        return null;
    }

    /**
     * @author Giorgio Mattina
     * @param id the player uid
     * @return the legal positions for the player uid
     */
    public List<Coordinates> showPlayersLegalPosition(UUID id){
        //updatePlayersLegalPosition();
        return playersLegalPositions.get(id);
    }

    /**
     * @author Giorgio Mattina
     * @param uid the player uid
     * @return the Goals of the player with uid
     */
    public Goal[] showPlayerGoalOptions(UUID uid){
        updatePlayersGoalOptions();
        return playersGoalOptions.get(uid);
    }

    @Override
    @Deprecated
    public Goal[] showPlayerGoalOptions() throws RemoteException, InvalidUserId {
        return null;
    }

    /**
     * @author Giorgio Mattina
     * @param uid the player uid
     * @return the private goal for the player uid
     */
    public Goal showPrivateGoal(UUID uid){
        return privateGoals.get(uid);
    }

    @Override
    @Deprecated
    public Goal showPrivateGoal() throws RemoteException, InvalidUserId {
        return null;
    }

    /**
     * @author Giorgio Mattina
     * @param w the username of the winner player
     */
    public void setWinner(String w){
        this.winner=w;
    }

    /**
     * @author Giuseppe Laguardia
     * @param userId
     * @return the started card for the player uid
     * @throws RemoteException
     */
    public StarterCard getStarterCard(UUID userId) throws RemoteException{
        return starterCardMap.get(userId);
    }

    @Override
    @Deprecated
    public StarterCard getStarterCard() throws RemoteException {
        return null;
    }

    /**
     * @author Giuseppe Laguardia
     * update the starter card map
     */
    public void updateStarterCardMap() {
        starterCardMap=controller.getPlayersStarterCards();
    }
   

}
