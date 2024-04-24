package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.HandFullException;
import it.polimi.ingsw.model.gamecards.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

public class TurnState extends GameState{
    TurnState(Game game, GameManager gameManager) {
        super(game, gameManager);
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
    public void playCardFront(Card selectedCard, Coordinates position, Integer userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException {
        
        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard, position, userId);

        game.playCardFront(selectedCard,position);
    }

    /**
     * checks if it's the player's turn, if the card is legal and if the position is legal
     * @author Giorgio Mattina
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws InvalidCardException
     * @throws IllegalPositionException
     */
    private void CheckTurnCardPosition(Card selectedCard, Coordinates position, Integer userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        //checks if selectedCard is in player's hand
        if(!game.getCurrentPlayer().getHand().contains(selectedCard)){
            throw new InvalidCardException();
        }
        //cheks if given position is in the availablePosition list
        if(!game.getCurrentPlayer().getAvailablePositions().contains(position)){
            throw new IllegalPositionException();
        }
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
    public void playCardBack(Card selectedCard, Coordinates position, Integer userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException {
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
    public void drawFromDeck(Integer userId,int choice) throws IsNotYourTurnException, HandFullException, DeckEmptyException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        if(choice == 0 && game.getResourceCardDeck().isEmpty()){
            throw new DeckEmptyException();
            //Da capire come fare per notificare che un solo mazzo è vuoto
        } 
        if (choice!=0 && game.getGoldCardDeck().isEmpty()) {
            throw new DeckEmptyException();
        }
        game.drawFromDeck(choice);
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
    public void drawVisibleCard (Integer userId,int choice) throws IsNotYourTurnException, HandFullException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        game.drawVisibleCard(choice);
    }





    //isFinalTurn() return
}
