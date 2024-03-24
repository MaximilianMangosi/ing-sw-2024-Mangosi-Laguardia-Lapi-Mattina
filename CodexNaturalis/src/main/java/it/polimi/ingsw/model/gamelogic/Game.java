package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;

import java.lang.reflect.Field;
import java.util.HashMap;
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

    public void drawVisibleCard(int choice) throws HandFullException, AllDeckEmptyExeption {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }

        Card drawCard = visibleCards.remove(choice);
        currentPlayer.addCardToHand(drawCard);
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
        } else{
            throw new AllDeckEmptyExeption();
        }

    }

    public void playCardFront(GoldCard selectedCard, Coordinates position) throws RequirementsNotMetException {
        //check the requirements for the gold card
        if(!elementCounter(selectedCard)){
            throw new RequirementsNotMetException();
        }

        int selectedCardPoints = selectedCard.getPoints();
        currentPlayer.addPoints(selectedCardPoints);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    public void playCardFront(ResourceCard selectedCard, Coordinates position){
        int selectedCardPoints = selectedCard.getPoints();
        currentPlayer.addPoints(selectedCardPoints);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    public void playCardBack(Card selectedCard, Coordinates position){
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter resources

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    private boolean elementCounter(GoldCard selectedCard){
        //receive map with all resources in the field of the current player
        HashMap <Resource, Integer> allResourcesOnField = currentPlayer.getResourceCounters();
        //compare the hashmap with the requirements of the card
        HashMap <Reign, Integer> selectedCardRequirements = selectedCard.getRequirements();
        for (Reign reign : selectedCardRequirements.keySet()) {
            if(selectedCardRequirements.get(reign) >= allResourcesOnField.get(reign)){
                return false;
            }
        }
        return true;
    }

    private void coverAngle(Coordinates position){
        //check all angles of the newly positioned card and set the angles covered by the new card as covered
        int x, y;
        x = position.x;
        y= position.y;
        cover(x-1, y+1, "SE");
        cover(x+1, y+1, "SW");
        cover(x-1, y-1, "NE");
        cover(x+1, y-1, "NW");
    }

    private void cover(int x, int y, String angleToBeCovered) {
        Card cardToBeCovered = currentPlayer.getCardAtPosition(x, y);
        if (cardToBeCovered != null){
            cardToBeCovered.setAngleCovered(angleToBeCovered);

            try {
                currentPlayer.decrementResourceCounter(cardToBeCovered.getResource(angleToBeCovered));
            }catch (NoSuchElementException e){

            }
        }
    }



}


