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
    public  UUID BootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException, IllegalOperationException {
        throw new IllegalOperationException();
    }

    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }

    public void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }

    protected abstract GameState nextState();

    public void chooseStarterCardSide(boolean isFront, UUID userId) throws IllegalOperationException,InvalidUserId {
        throw new IllegalOperationException();
    }

    public void chooseGoal(UUID userId, Goal newGoal) throws IllegalOperationException, InvalidGoalException, InvalidUserId {
        throw new IllegalOperationException();
    }

    public void drawFromDeck(UUID userId, int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, IllegalOperationException {
        throw new IllegalOperationException();
    }
    public void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }

}
