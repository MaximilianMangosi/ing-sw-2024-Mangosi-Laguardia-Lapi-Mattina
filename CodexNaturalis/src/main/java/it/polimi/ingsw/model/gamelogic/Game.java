package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import static java.util.Collections.shuffle;

/**
 * Game class
 * @author Giorgio Mattina
 * @author Maximilian Mangosi
 */
public class Game{
     private List<Player> ListOfPlayers;
    private int numOfPlayers;
    private List<ResourceCard> resourceCardDeck;
    private List<GoldCard> goldCardDeck;
    private List<Card> visibleCards;
    private List<Goal> listOfGoal;
    private Player currentPlayer;

    /**
     * @author Giorgio Mattina
     * Builds the Players' hands and all the game decks
     */
    private void startGame() {
        //building players' hands
        int i = 0;
        int j = 0;
        Coordinates origin = new Coordinates(0,0);
        Player player;
        //Mi manca un attimo come funziona APP ,cioè devo fare una copia dei mazzi da APP e poi fare lo shuffle

        List<String> Colors= new ArrayList<String>();
        Colors.add("Red");
        Colors.add("Blue");
        Colors.add("Yellow");
        Colors.add("Green");


        shuffle(resourceCardDeck);
        shuffle(goldCardDeck);
        shuffle(listOfGoal);


        for (i = 0; i < numOfPlayers; i++) {

            player = ListOfPlayers.get(i);
            player.setname("Player"+i);
            player.setColor(Colors.get(i));
            player.setPoints(0);


            //two resource cards
            for (j = 0; j < 2; j++) {
                player.addCardToHand(resourceCardDeck.removeFirst());

            }

            //one gold card
            player.addCardToHand(goldCardDeck.removeFirst());


            //one starter card
            player.setStarterCard();
            player.setGoal(listOfGoal.removeFirst());



            //aggiungere posizione della starter card



        }
        //Builds a list of public goals

        //Builds the visiblecardsdeck
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(goldCardDeck.removeFirst());
        }
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(resourceCardDeck.removeFirst());
        }



    }


    /**
     * @author Maximilian Mangosi
     * draw a card from one of the two decks
     * @param choice it's the input given from the player to decide witch deck to draw from
     * @throws HandFullException catches the exception when the hand is already full
     */
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

    /**
     * @author Maximilian Mangosi
     * drawing from the visible cards on the field
     * @param choice it's the input given from the player to decide witch card to draw
     * @throws HandFullException catches the exception when the hand is already full
     * @throws AllDeckEmptyExeption catches the exception when the decks are all empty
     */
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

    /**
     * @author Maximilian Mangosi
     * playing the card in the front position on the field for the gold card
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     * @throws RequirementsNotMetException catches the exception when the requirements for the gold card are not met
     */
    public void playCardFront(GoldCard selectedCard, Coordinates position) throws RequirementsNotMetException {
        //check the requirements for the gold card
        if(!elementCounter(selectedCard)){
            throw new RequirementsNotMetException();
        }

        int selectedCardPoints = selectedCard.getPoints();
        currentPlayer.addPoints(selectedCardPoints);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    /**
     * @author Maximilian Mangosi
     * playing the card in the front position on the field for the nortmal card
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardFront(ResourceCard selectedCard, Coordinates position){
        int selectedCardPoints = selectedCard.getPoints();
        currentPlayer.addPoints(selectedCardPoints);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    /**
     * @author Maximilan Mangosi
     * playing the card in the back position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardBack(Card selectedCard, Coordinates position){
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
    }

    /**
     * @author Maximilian Mangosi
     * counts the elements needed for the gold card requirements
     * @param selectedCard selected gold card
     * @return returns the value true or false to give an answer for the requirements
     */
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

    /**
     * @author Maximilian Mangosi
     * when a card is positioned on the field, this card coveres the angles of other cards
     * @param position coordinates in witch the card has been positioned
     */
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

    /**
     * @author Maximilian Mangosi
     * checkes all angles and sees witch ones are covered
     * @param x coordinate x
     * @param y coordinate y
     * @param angleToBeCovered angle to be covered by the card
     */
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

    public int calculateGoal(IdenticalGoal goal, Player player){
        return player.getResourceCounter(goal.getResource()) / 3;
    }


    public int calculateGoal(DistinctGoal goal, Player player){
        List<Resource> validResources = new ArrayList<Resource>();
        validResources.add(Tool.FEATHER);
        validResources.add(Tool.SCROLL);
        validResources.add(Tool.PHIAL);

        return player.getResourceCounters().entrySet()
                .stream()
                .filter(entry -> validResources.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .min(Integer::compareTo)
                .orElse(0);
    }

}


