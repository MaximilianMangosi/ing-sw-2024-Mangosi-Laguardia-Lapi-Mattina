package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Game{
     private List<Player> ListOfPlayers;
    private int numOfPlayers;
    private List<ResourceCard> resourceCardDeck;
    private List<GoldCard> goldCardDeck;
    private List<Card> visibleCards;
    private List<Goal> listOfGoal;
    private Player currentPlayer;

    public void drawFromDeck(int choice) throws HandFullException {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }
        if (choice == 0) {
            //normal cards
            Card drawCard = resourceCardDeck.removeFirst();
            currentPlayer.addCardToHand(drawCard);
        } else {
            //gold cards
            Card drawCard = goldCardDeck.removeFirst();
            currentPlayer.addCardToHand(drawCard);
        }
    }

    public void drawVisibleCard(int choice) throws HandFullException {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }

        Card drawCard = visibleCards.remove(choice);
        if (!(goldCardDeck.isEmpty() && resourceCardDeck.isEmpty())) {
            if (drawCard instanceof GoldCard) {
                try {
                    visibleCards.add(goldCardDeck.removeFirst());
                } catch (NoSuchElementException e) {
                    visibleCards.add(resourceCardDeck.removeFirst());
                }
            } else {
                try{
                    visibleCards.add(resourceCardDeck.removeFirst());
                }catch (NoSuchElementException e){
                    visibleCards.add(goldCardDeck.removeFirst());
                }
            }
        }
        currentPlayer.addCardToHand(drawCard);
    }

    public void playCardFront(Card selectedCard, Coordinates position){
        //add points if there are any
        if (selectedCard instanceof GoldCard) {
            //check the requirements for the gold card
            if(selectedCard.getRequirements == true){
                int selectedCardPoints = selectedCard.getPoints();
                Player.addPoints(selectedCardPoints);
                currentPlayer.addCardToMap(selectedCard, position);
            }
        }else{
            int selectedCardPoints = selectedCard.getPoints();
            Player.addPoints(selectedCardPoints);
            currentPlayer.addCardToMap(selectedCard, position);
        }
    }

    public void playCardBack(Card selectedCard, Coordinates position){
        currentPlayer.addCardToMap(selectedCard, position);
    }

}
