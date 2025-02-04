package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.controller.exceptions.DeckEmptyException;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.*;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;

import java.util.*;
import java.util.List;

import static java.util.Collections.shuffle;

/**
 * Game class
 * @author Giorgio Mattina
 * @author Maximilian Mangosi
 */
public class Game{
    private List<Player> listOfPlayers=new ArrayList<>();
    private int numOfPlayers;
    private boolean AreBothDeckEmpty = false;
    private List<ResourceCard> resourceCardDeck ;
    private List<GoldCard> goldCardDeck ;
    private List<Card> visibleCards=new ArrayList<>();
    private Goal[] publicGoals=new Goal[2];
    private final List<Goal> listOfGoal= new ArrayList<>();
    private Player currentPlayer;
    private List<StarterCard> starterCards=new ArrayList<>();

    private List<String> globalChat = new ArrayList<>();

    public List<String> getGlobalChat(){
        return globalChat;
    }

    public void addToGlobalChat(String message){
        globalChat.add(message);
    }


    /**
     * constructor of class Game
     * @author Giuseppe Laguardia
     * @param firstPlayer the player who creates the game
     * @param numOfPlayers the number of player that can join the game, must be bigger than 1
     */
    public Game(Player firstPlayer, int numOfPlayers,GameBox gamebox){

        listOfPlayers.add(firstPlayer);
        this.numOfPlayers = numOfPlayers;
        this.currentPlayer=firstPlayer;

        //create new lists for resource and Gold Cards
        this.resourceCardDeck = new ArrayList<>();
        this.goldCardDeck = new ArrayList<>();
        //adds from gamebox

        this.resourceCardDeck.addAll(gamebox.getResourceCardSet());
        this.goldCardDeck.addAll(gamebox.getGoldCardSet());
        this.listOfGoal.addAll(gamebox.getGoalSet());
        this.starterCards.addAll(gamebox.getStarterCardSet());


    }

    /**
     * getter of list of players
     * @author Giuseppe Laguardia
     * @return List <Player></>
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(listOfPlayers);
    }

    /**
     * Adds a player to the list of players.
     *
     * @param p The player to add.
     */
    public void addPlayer(Player p) {
        listOfPlayers.add(p);
    }

    /**
     * Gets the number of players.
     *
     * @return The number of players.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Gets the deck of resource cards.
     *
     * @return A list of resource cards.
     */
    public List<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    /**
     * Gets the deck of gold cards.
     *
     * @return A list of gold cards.
     */
    public List<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    /**
     * Gets the list of visible cards.
     *
     * @return The list of visible cards.
     */
    public List<Card> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Gets the list of goals.
     *
     * @return The list of goals.
     */
    public List<Goal> getListOfGoal() {
        return listOfGoal;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer The current player.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the array of public goals.
     *
     * @return An array of public goals.
     */
    public Goal[] getPublicGoals() {
        return publicGoals;
    }


    /**
     * @author Giorgio  Mattina
     * getter of attribute AreBothDeckEmpty
     * @return attribute AreBothDeckEmpty
     */
    public boolean AreBothDeckEmpty() {
        return AreBothDeckEmpty;
    }
    /**
     * set the next player to play, according to the order of ListOfPlayers
     * @author Giuseppe Laguardia
     */
    public void nextTurn(){
        int currPlayerIdx= listOfPlayers.indexOf(currentPlayer);
        Player nextPlayer= listOfPlayers.get((currPlayerIdx+1) % numOfPlayers); //if currPlayerIdx+1==numOfPlayers next player is the first of the list
        setCurrentPlayer(nextPlayer);
    }

    /**
     * Finds the player with most points, when more than one player have the same amount of point the winner is who has more goalPoints
     * @author Giuseppe Laguardia
     * @return the winner of the game
     */
    public Player getWinner(){

        int maxPoints = listOfPlayers.stream().map(p->p.getPoints()+p.getGoalPoints()).
                                                max(Integer::compareTo).
                                                get();
        // don't know if a check for maxPoints >=20 is needed
        return listOfPlayers.stream().
                filter(p->p.getPoints()+p.getGoalPoints()== maxPoints).
                max(Comparator.comparing((Player::getGoalPoints))).get();

    }

    /**
     * @author Giuseppe Laguardia
     * @return true if a player has reached at least 20 points
     */
    public boolean someoneHas20Points(){
        return listOfPlayers.stream().anyMatch(p->p.getPoints()>=20);
    }

    /**
     * Builds the Players' hands and all the game decks
     * @author Giorgio Mattina
     *
     * Builds the Players' hands and all the game decks
     */
    public void startGame() {
        //building players' hands
        int i = 0;
        int j = 0;


        Player player;
        String[] colorArray={"Red","Blue","Yellow","Green"};
        List<String> colors= new ArrayList<String>(List.of(colorArray));


        shuffle(resourceCardDeck);
        shuffle(goldCardDeck);
        shuffle(listOfGoal);
        shuffle(starterCards);
        shuffle(colors);
        Resource[] resourceArray={Reign.ANIMAL,Reign.MUSHROOM,Reign.BUG,Reign.PLANTS, Tool.PHIAL,Tool.FEATHER,Tool.SCROLL};
        publicGoals[0] = listOfGoal.removeFirst();
        publicGoals[1]=listOfGoal.removeFirst();

        for (i = 0; i < numOfPlayers; i++) {

            player = listOfPlayers.get(i);
            player.setColor(colors.get(i));
            player.setPoints(0);
            //initialize the player's resource counter to 0
            for (Resource resource: resourceArray){
                player.setResourceCounter(resource,0);
            }
            //two resource cards
            for (j = 0; j < 2; j++) {
                player.addCardToHand(resourceCardDeck.removeFirst());

            }
            //sets the player's starter card
            player.setStarterCard(starterCards.removeFirst());
            //one gold card
            player.addCardToHand(goldCardDeck.removeFirst());



            Goal[] goalOptions=new Goal[2];
            for (int k = 0; k<2; k++){
                goalOptions[k]=listOfGoal.removeFirst();
            }
            player.setGoalOptions(goalOptions);
            //initialize private chats
            for (int p=i+1;p<numOfPlayers;p++){
                List<String>chat=new ArrayList<>();
                // both player share the same list
                player.setPrivateChat(listOfPlayers.get(p).getName(),chat);
                listOfPlayers.get(p).setPrivateChat(player.getName(),chat);

            }


        }
        //Builds a list of public goals

        //Builds the visible cards deck
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(goldCardDeck.removeFirst());
        }
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(resourceCardDeck.removeFirst());
        }

        //the current player is the first player in the players list
        shuffle(listOfPlayers);
        currentPlayer = listOfPlayers.getFirst();


    }


    /**
     * @author Maximilian Mangosi
     * draw a card from one of the two decks
     * @param choice it's the input given from the player to decide witch deck to draw from
     *               if 0 draws from resourceCardDeck, otherwise from goldCardDeck
     * @throws HandFullException catches the exception when the hand is already full
     */
    public void drawFromDeck(int choice) throws HandFullException {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }
        if (choice == 0) {
            //normal cards
            ResourceCard drawCard = resourceCardDeck.removeFirst();
            currentPlayer.addCardToHand(drawCard);
        } else {
            //gold cards
            GoldCard drawCard = goldCardDeck.removeFirst();
            currentPlayer.addCardToHand(drawCard);
        }
        AreBothDeckEmpty=goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();
    }

    /**
     * @author Maximilian Mangosi
     * drawing from the visible cards on the field
     * @param choice it's the input given from the player to decide witch card to draw
     * @throws HandFullException catches the exception when the hand is already full
     * @throws DeckEmptyException catches the exception when the decks are all empty
     */
    public void drawVisibleCard(int choice) throws HandFullException {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }

        var drawCard = visibleCards.remove(choice);
        currentPlayer.addCardToHand(drawCard);
        if (!(goldCardDeck.isEmpty() && resourceCardDeck.isEmpty())) {
            if (!drawCard.getRequirements().isEmpty()) {
                try {
                    visibleCards.add(choice,goldCardDeck.removeFirst());
                } catch (NoSuchElementException e) {
                    visibleCards.add(choice,resourceCardDeck.removeFirst());
                }
            } else {
                try {
                    visibleCards.add(choice,resourceCardDeck.removeFirst());
                } catch (NoSuchElementException e) {
                    visibleCards.add(choice,goldCardDeck.removeFirst());
                }
            }
        }
        AreBothDeckEmpty=goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();

    }
    /**
     * @author Maximilian Mangosi
     * play card for starter card
     * @param isFront determines the side on which the card is played
     */
    public void playStarterCard(Player player,boolean isFront){

        //add counter of resources
        StarterCard starterCard = player.getStarterCard();
        starterCard.setIsFront(isFront);
        player.addCardToMap(starterCard, new Coordinates(0,0));
        player.updateResourceCounter(starterCard.getCardResources());
        player.checkAvailablePositions(new Coordinates(0,0), starterCard);

    }

    /**
     * @author Maximilian Mangosi,Giorgio Mattina
     * playing the card in the front position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     * @throws RequirementsNotMetException catches the exception when the requirements are not met(if there are any)
     */
    public void playCardFront(Card selectedCard, Coordinates position) throws RequirementsNotMetException {
        //check the requirements for the gold card
        selectedCard.setIsFront(true);
        if(!selectedCard.checkRequirements(currentPlayer.getResourceCounters())){
            throw new RequirementsNotMetException();
        }
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
        selectedCard.addPoints(currentPlayer);
        //update availablePosition list
        currentPlayer.checkAvailablePositions(position,selectedCard);
        currentPlayer.removeFromHand(selectedCard);
    }




    /**
     * @author Maximilan Mangosi, Giorgio Mattina
     * playing the Resource card in the back position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardBack(Card selectedCard, Coordinates position){
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        List<Resource> newResource = new ArrayList<>();
        newResource.add(selectedCard.getReign());
        //add counter resources
        currentPlayer.updateResourceCounter(newResource);

        //covering all the angles the new card is covering
        coverAngle(position);

        //update availablePosition list
        currentPlayer.checkAvailablePositions(position,selectedCard);
        currentPlayer.removeFromHand(selectedCard);
    }

    /**
     * @author Maximilian Mangosi
     * when a card is positioned on the field, this card coveres the angles of other cards
     * @param position coordinates in witch the card has been positioned
     */
    private void coverAngle(Coordinates position) {
        //check all angles of the newly positioned card and set the angles covered by the new card as covered
        int x, y;
        x = position.x;
        y = position.y;
        cover(x - 1, y + 1, "SE");
        cover(x + 1, y + 1, "SW");
        cover(x - 1, y - 1, "NE");
        cover(x + 1, y - 1, "NW");
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
            try {
                Resource decrementedResource=cardToBeCovered.getResource(angleToBeCovered);
                if( decrementedResource!=null && !decrementedResource.isEmpty()) {
                    currentPlayer.decrementResourceCounter(decrementedResource);
                    cardToBeCovered.setAngle(Reign.EMPTY, angleToBeCovered);
                }
            }catch (NoSuchElementException ignore){}
        }
    }

    /**
     * Removes the player from the listOfPlayers and updates NumOfPlayers.
     * If listOfPlayers doesn't contain player nothing changes.
     * If player equals currentPlayer the turn passes to next player in the list
     * @author Giuseppe Laguardia
     * @param player the player to remove
     */

    public void removePlayer(Player player) {
        if(currentPlayer.equals(player))
            nextTurn();
        listOfPlayers.remove(player);
        numOfPlayers--;
    }

}


