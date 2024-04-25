package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.util.*;

public class Controller {
    GameState currentState;
    View view;
    /**
     * constructor of Controller, creates a new GameState
     * @author Giorgio Mattina
     * @param gameManager
     */
    public Controller( GameManager gameManager){
        currentState=new LobbyState(gameManager);
        view = new View(this);
    }


    /**
     * Makes the transition to nextState
     * @author Giuseppe Laguardia
     */
    private synchronized void changeState(){
        currentState=currentState.nextState();
    }

    /**
     * @author Giuseppe Laguardia
     * @param numOfPlayers the number of players wanted for the game, if there is another game pending thi parameter is ignored
     * @param playerName the name chosen by the user for the game
     * @return the user's identifier
     * @throws UnacceptableNumOfPlayersException if numOfPlayers is out of range [2,4]
     * @throws PlayerNameNotUniqueException if playerName is already taken by another user
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public  UUID BootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException, IllegalOperationException {
        UUID userID= currentState.BootGame(numOfPlayers,playerName);
        view.updatePlayersList();
        changeState();
        return userID;
    }
    public synchronized void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        currentState.playCardFront(selectedCard,position,userId);
        //TODO update view
        view.updatePlayersHands();
        view.updatePlayersField();
        view.updatePlayersPoints();
        view.updateCurrentPlayer();
        view.updatePlayersLegalPosition();
    }
    public synchronized void playCardBack(Card selectedCard, Coordinates position,UUID userId) throws HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, IllegalOperationException, InvalidCardException {
        currentState.playCardBack(selectedCard,position,userId);
        view.updatePlayersHands();
        view.updatePlayersField();
        view.updateCurrentPlayer();
        view.updatePlayersLegalPosition();
    }
    public synchronized void ChooseStarterCardSide(boolean isFront, UUID userId) throws InvalidUserId, IllegalOperationException {
        currentState.chooseStarterCardSide(isFront,userId);
        view.updatePlayersField();
        view.updatePlayersLegalPosition();
    }
    public synchronized void ChooseGoal(UUID userId, Goal newGoal) throws InvalidGoalException, InvalidUserId, IllegalOperationException {
        currentState.chooseGoal(userId,newGoal);
        //TODO update goals
    }
    public synchronized void drawFromDeck(UUID userId,int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException {
        currentState.drawFromDeck(userId, choice);
        view.updatePlayersHands();
    }
    public synchronized void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException, IllegalOperationException {
        currentState.drawVisibleCard(userId,choice);
        view.updatePlayersHands();
        //TODO update visible cards
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return Map of each player's score
     */
    public Map<String, Integer> getPlayersPoints(){
        Map<String, Integer> scoreboard = new HashMap<>();
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
    public Map<UUID,Map<Coordinates,Card>> getPlayersField(){
        Map<UUID,Map<Coordinates,Card>> fields = new HashMap<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id: set){
            fields.put(id,currentState.getPlayerFromUid(id).getField());
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
     * @return the list of public goals to the view
     */
    public List<Goal> getPublicGoals(){
        return currentState.game.getListOfGoal();
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
    //turn->final dopo drawCard check (areBothEmpty or hasSomeone20points) and  current player is first of the list
    //pepo,giorgio,max,ric
    //final->Terminal compute public goal and getWinner
    //currentState=new FinalTurn(game,gameManager);
}
