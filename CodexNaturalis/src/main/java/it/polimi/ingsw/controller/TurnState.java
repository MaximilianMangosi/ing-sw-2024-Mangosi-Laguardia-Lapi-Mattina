package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TurnState extends GameState{
    /**
     * constructor of object TurnState
     * @author Giuseppe Laguardia
     * @param game
     * @param gameManager
     */
    TurnState(Game game, GameManager gameManager, HashMap<UUID, Player> userIds) {
        super( gameManager);
        this.game=game;
        this.userIDs=userIds;
    }

    /**
     * checks the right circumstances to change the state and then returns a new FinalState object
     * @author Giuseppe Laguardia
     * @return the next state
     */
    public GameState nextState(){
        if((game.someoneHas20Points() || game.AreBothDeckEmpty()) && game.getCurrentPlayer().equals(game.getPlayers().getFirst()))
            return new FinalTurnState(game,gameManager);
        return this;
    }

    /**
     * checks for Turn rights, and calls playCardFront
     * @author Giorgio Mattina
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     */
    public boolean playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException {
        
        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard, position, userId);

        game.playCardFront(selectedCard,position);
        return false;
    }

    /**
     * checks for Turn Rights and calls PlayCardBack
     * @author Giorgio Mattina
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws InvalidCardException
     * @throws IllegalPositionException
     */
    public boolean playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException, HandNotFullException {
        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard,position,userId);

        game.playCardBack(selectedCard,position);
        return false;
    }   
    
    /**
     * checks for Turn rights and calls drawFromDeck
     * @author Giorgio Mattina
     * @param userId
     * @param choice
     * @throws IsNotYourTurnException
     * @throws HandFullException
     */
    public void drawFromDeck(UUID userId,int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException, InvalidChoiceException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        if(choice!=0 && choice!=1)
            throw new InvalidChoiceException();
        if(choice == 0 && game.getResourceCardDeck().isEmpty()){
            throw new DeckEmptyException();
        } 
        if (choice!=0 && game.getGoldCardDeck().isEmpty()) {
            throw new DeckEmptyException();
        }
        game.drawFromDeck(choice);
        game.nextTurn();
    }

    /**
     * @author Giorgio Mattina
     * checks if the decks are empty
     * @param userId
     * @param choice
     * @throws IsNotYourTurnException
     * @throws DeckEmptyException
     * @throws HandFullException
     */
    public void drawVisibleCard (UUID userId,int choice) throws IsNotYourTurnException, HandFullException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        game.drawVisibleCard(choice);
        game.nextTurn();
    }
    @Override
    public boolean isGameStarted() {
        return true;
    }




}
