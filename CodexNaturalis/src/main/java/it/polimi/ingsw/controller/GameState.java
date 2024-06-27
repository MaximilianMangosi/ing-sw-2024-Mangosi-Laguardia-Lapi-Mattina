package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class GameState {
    protected Game game;
    protected HashMap<UUID, Player> userIDs=new HashMap<>() ;
    protected GameManager gameManager;

    /**
     * adds message to the global chat
     * @author Maximilian Mangosi
     * @param message the message to be sent
     * @throws IllegalOperationException when sending a message is not allowed
     */
    public void addToGlobalChat(String message) throws IllegalOperationException {
        game.addToGlobalChat(message);
    }

    /**
     * adds message to private chat
     * @author Maximilian Mangosi
     * @param receiver the receiver's username
     * @param message the message to be sent
     * @param userID the id of the sender
     * @throws IllegalOperationException when sending a message is not allowed
     */
    public void addMessage(String receiver, String message, UUID userID) throws IllegalOperationException {
       userIDs.get(userID).addMessage(receiver,message);

    }

    /**constructor of GameState
     * @param gameManager the Game manager
     * @author Giuseppe Laguardia
     *
     */
    GameState(GameManager gameManager) {
        this.gameManager = gameManager;

    }

    /**
     * @author Giorgio Mattina
     * @return the user ids
     */
    public HashMap<UUID, Player> getUserIDs() {
        return userIDs;
    }

    /**
     * @param userId the user unique id
     * @return the Player associated to the userId
     * @author Riccardo Lapi
     */
    public Player getPlayerFromUid(UUID userId) {
        return getUserIDs().get(userId);
    }

    /**
     * checks if it's the player's turn, if the card is legal and if the position is legal
     *
     * @param selectedCard the card to be checked
     * @param position the position to be checked
     * @param userId the user to be checked
     * @throws IsNotYourTurnException when isn't user turn
     * @throws InvalidCardException when the card is not in user's hand
     * @throws IllegalPositionException when the position is not valid
     * @author Giorgio Mattina
     */
    protected void CheckTurnCardPosition(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException, HandNotFullException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())) {
            throw new IsNotYourTurnException();
        }
        //checks if selectedCard is in player's hand
        if (!game.getCurrentPlayer().getHand().contains(selectedCard)) {
            throw new InvalidCardException();
        }
        if (game.getCurrentPlayer().getHand().size() < 3) {
            throw new HandNotFullException();
        }
        //checks if given position is in the availablePosition list
        if (!game.getCurrentPlayer().getAvailablePositions().contains(position)) {
            throw new IllegalPositionException();
        }
    }

    /**
     * Default implementation for bootGame throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     *
     * @throws IllegalOperationException if in this state this action cannot be performed
     * @author Giuseppe Laguardia
     */
    public GameKey bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException {
        throw new IllegalOperationException("boot-game");
    }
    /**
     * Default implementation for playCardFront throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public boolean playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException("play-card");
    }
    /**
     * Default implementation for playCardBack throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public boolean playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException("play-card");
    }
    /**
     * Abstract implementation for nextState, must be overridden by the subclasses
     * @author Giuseppe Laguardia
     */
    protected abstract GameState nextState();
    /**
     * Default implementation for chooseStarterCardSide throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void chooseStarterCardSide(boolean isFront, UUID userId) throws IllegalOperationException,InvalidUserId {
        throw new IllegalOperationException("choose-starter-card-side");
    }
    /**
     * Default implementation for chooseGoal throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void chooseGoal(UUID userId, Goal newGoal) throws IllegalOperationException, InvalidGoalException, InvalidUserId {
        throw new IllegalOperationException("choose-goal");
    }
    /**
     * Default implementation for drawFromDeck throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void drawFromDeck(UUID userId, int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException {
        throw new IllegalOperationException("draw");
    }
    /**
     * Default implementation for drawVisibleCard throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException, IllegalOperationException {
        throw new IllegalOperationException("draw");
    }
    /**
     * Default implementation for closeGame that delete the Player from listOfPlayers and UserIds.From now on the user cannot play this game, can be overridden by the subclasses
     * @author Giuseppe Laguardia
     */
    public void closeGame(UUID userID) {
        Player player = userIDs.get(userID);
        if (game!=null) {
            game.removePlayer(player);
        }
        gameManager.deletePlayerFromPlayersToGame(player.getName());
        userIDs.remove(userID);

    }
    /**
     * @author Riccardo Lapi
     * remove the current game from the GameManager gameInProcess Map,
     * and remove ecah player in the current game from the GameManager playerToGame Map
     */
    public void deleteGameFromGameManager(){
        gameManager.deleteGame(game);
        for(Player player : game.getPlayers()){
            gameManager.deletePlayerFromPlayersToGame(player.getName());
        }
    }

    /**
     *  Default implementation for isGameEnded, returns always false
     * @author Giuseppe Laguardia
     * @return false
     */
    public boolean isGameEnded() {
        return false;
    }
    /**
     *  Default implementation for isGameStarted, returns always false
     * @author Giuseppe Laguardia
     * @return false
     */
    public boolean isGameStarted(){return false;}

    /**
     * @author Giorgio Mattina
     * abstract for the joinGame function
     * @param playerName the username used in game
     * @return the UUID created
     * @throws IllegalOperationException
     * @throws PlayerNameNotUniqueException
     */
    public UUID joinGame(UUID gameId,String playerName) throws IllegalOperationException,PlayerNameNotUniqueException, InvalidGameID {
        throw new IllegalOperationException("joinGame");
    }

    /**
     * @author Giuseppe Laguardia
     * @param name
     * @param userID
     * @return private chat
     */
    public List<String> getPrivateChat(String name, UUID userID) {
        return userIDs.get(userID).getPrivateChat(name);
    }
}
