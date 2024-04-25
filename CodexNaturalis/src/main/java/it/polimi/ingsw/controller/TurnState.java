package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

import java.util.UUID;

public class TurnState extends GameState{
    TurnState(Game game, GameManager gameManager) {
        super( gameManager);
        this.game=game;
    }
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
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException {
        
        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard, position, userId);

        game.playCardFront(selectedCard,position);
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
    public void playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException, HandNotFullException {
        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard,position,userId);

        game.playCardBack(selectedCard,position);
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





}
