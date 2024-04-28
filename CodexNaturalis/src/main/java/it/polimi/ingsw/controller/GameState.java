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
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.util.HashMap;
import java.util.UUID;

public abstract class GameState {
    protected Game game;
    protected HashMap<UUID, Player> userIDs = new HashMap<>();
    protected GameManager gameManager;

    /**
     * @param gameManager
     * @author Giuseppe Laguardia
     * constructor of GameState
     */
    GameState(GameManager gameManager) {
        this.gameManager = gameManager;

    }

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
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws InvalidCardException
     * @throws IllegalPositionException
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
        //cheks if given position is in the availablePosition list
        if (!game.getCurrentPlayer().getAvailablePositions().contains(position)) {
            throw new IllegalPositionException();
        }
    }

    /**
     * Default implementation for bootGame throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public  UUID BootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException, IllegalOperationException {
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for playCardFront throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public boolean playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for playCardBack throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public boolean playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException();
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
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for chooseGoal throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void chooseGoal(UUID userId, Goal newGoal) throws IllegalOperationException, InvalidGoalException, InvalidUserId {
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for drawFromDeck throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void drawFromDeck(UUID userId, int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException, InvalidChoiceException {
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for drawVisibleCard throws always IllegalOperationException, must be overridden by the subclasses if this method is legal in that state
     * @author Giuseppe Laguardia
     * @throws IllegalOperationException if in this state this action cannot be performed
     */
    public void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }
    /**
     * Default implementation for closeGame that delete the Player from listOfPlayers and UserIds.From now on the user cannot play this game, can be overridden by the subclasses
     * @author Giuseppe Laguardia
     */
    public void closeGame(UUID userID) {
       game.removePlayer(userIDs.get(userID));
       userIDs.remove(userID);

    }
    /**
     * @author Riccardo Lapi
     * remove the current game from the GameManager gameInProcess Map,
     * and remove ecah player in the current game from the GameManager playerToGame Map
     */
    public void deleteGameFromGameManager(){

        String gameHash = String.valueOf(game.hashCode());
        gameManager.deleteGame(gameHash);

        for(Player player : game.getPlayers()){
            gameManager.deletePlayerFromPlayersToGame(player.getName());
        }
        game=null;
    }

    /**
     *  Default implementation for isGameEnded, returns always false
     * @author Giuseppe Laguardia
     * @return false
     */
    public boolean isGameEnded() {
        return false;
    }


}
