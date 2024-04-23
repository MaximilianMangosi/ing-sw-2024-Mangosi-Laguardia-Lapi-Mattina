package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
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
    private final int numOfPlayers;
    private boolean AreBothDeckEmpty;
    private List<ResourceCard> resourceCardDeck ;
    private List<GoldCard> goldCardDeck ;
    private List<Card> visibleCards;
    private final List<Goal> listOfGoal= new ArrayList<>();
    private Player currentPlayer;

    private List<StarterCard> starterCards=new ArrayList<>();


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

        //create a new List for visible cards
        this.visibleCards = new ArrayList<Card>();
        //adds them from this class
        for (int i = 0; i<2; i++){
            this.visibleCards.add(resourceCardDeck.removeFirst());
        }
        for (int i = 0; i<2; i++){
            this.visibleCards.add(goldCardDeck.removeFirst());
        }
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(listOfPlayers);
    }

    public void addPlayer(Player p) {
        listOfPlayers.add(p);
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public List<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    public List<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    public List<Card> getVisibleCards() {
        return visibleCards;
    }

    public List<Goal> getListOfGoal() {
        return listOfGoal;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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

        int maxPoints = listOfPlayers.stream().max(Comparator.comparing((Player::getPoints))).
                        get().getPoints();
        // don't know if a check for maxPoints >=20 is needed
        return listOfPlayers.stream().
                filter(p->p.getPoints()== maxPoints).
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
    *   Builds the Players' hands and all the game decks
     * @author Giorgio Mattina
     * Builds the Players' hands and all the game decks
     */
    public void startGame() {
        //building players' hands
        int i = 0;
        int j = 0;
        Coordinates origin = new Coordinates(0,0);
        List<Coordinates> start_position = new ArrayList<>();
        start_position.add(origin);
        Player player;
        //Mi manca un attimo come funziona APP ,cioè devo fare una copia dei mazzi da APP e poi fare lo shuffle
        String[] colorArray={"Red","Blue","Yellow","Green"};
        List<String> Colors= new ArrayList<String>(List.of(colorArray));


        shuffle(resourceCardDeck);
        shuffle(goldCardDeck);
        shuffle(listOfGoal);
        shuffle(starterCards);

        for (i = 0; i < numOfPlayers; i++) {

            player = listOfPlayers.get(i);
            player.setColor(Colors.get(i));
            player.setPoints(0);
            player.setAvailablePositions(start_position);

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


            //aggiungere posizione della starter card



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
     * @throws AllDeckEmptyExeption catches the exception when the decks are all empty
     */
    public void drawVisibleCard(int choice) throws HandFullException, AllDeckEmptyExeption {
        List<Card> hand = currentPlayer.getHand();
        if (hand.size() > 2) {
            throw new HandFullException();
        }

        var drawCard = visibleCards.remove(choice);
        currentPlayer.addCardToHand(drawCard);
        if (!(goldCardDeck.isEmpty() && resourceCardDeck.isEmpty())) {
            if (drawCard instanceof GoldCard) {
                try {
                    visibleCards.add(goldCardDeck.removeFirst());
                } catch (NoSuchElementException e) {
                    visibleCards.add(resourceCardDeck.removeFirst());
                }
            } else {
                try {
                    visibleCards.add(resourceCardDeck.removeFirst());
                } catch (NoSuchElementException e) {
                    visibleCards.add(goldCardDeck.removeFirst());
                }
            }
        }
        AreBothDeckEmpty=goldCardDeck.isEmpty() && resourceCardDeck.isEmpty();

    }
    /**
     * @author Maximilian Mangosi
     * play card front for starter card
     * @param selectedCard it's the starter card
     * @param position position 0,0
     */
    public void playCardFront(StarterCard selectedCard, Coordinates position){
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());
        currentPlayer.updateResourceCounter(selectedCard.getCentralResource());

        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
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
        selectedCard.setIsFront(true);
        if(!elementCounter(selectedCard)){
            throw new RequirementsNotMetException();
        }
        int selectedCardPoints = selectedCard.getPoints();

        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
        currentPlayer.addPoints(selectedCard);
        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
    }

    /**
     * @author Maximilian Mangosi
     * playing the card in the front position on the field for the nortmal card
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardFront(ResourceCard selectedCard, Coordinates position){
        selectedCard.setIsFront(true);

        currentPlayer.addCardToMap(selectedCard, position);

        //add counter of resources
        currentPlayer.updateResourceCounter(selectedCard.getCardResources());

        //covering all the angles the new card is covering
        coverAngle(position);
        currentPlayer.addPoints(selectedCard);
        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
    }


    /**
     * @author Maximilan Mangosi
     * playing the Resource card in the back position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardBack(ResourceCard selectedCard, Coordinates position){ //TODO overload for StarterCard
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        List<Resource> newResource = new ArrayList<>();
        newResource.add(selectedCard.getReign());
        //add counter resources
        currentPlayer.updateResourceCounter(newResource);

        //covering all the angles the new card is covering
        coverAngle(position);

        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
    }
    /**
     * @author Maximilan Mangosi
     * playing the Gold card in the back position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardBack(GoldCard selectedCard, Coordinates position){ //TODO overload for StarterCard
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        List<Resource> newResource = new ArrayList<>();
        newResource.add(selectedCard.getReign());
        //add counter resources
        currentPlayer.updateResourceCounter(newResource);

        //covering all the angles the new card is covering
        coverAngle(position);

        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
    }
    /**
     * @author Maximilan Mangosi
     * playing the Starter card in the back position on the field
     * @param selectedCard the card selected by the user
     * @param position the coordinates in witch th user wants the card to be positioned
     */
    public void playCardBack(StarterCard selectedCard, Coordinates position){
        selectedCard.setIsFront(false);
        currentPlayer.addCardToMap(selectedCard, position);

        //add counter resources
        currentPlayer.updateResourceCounter(selectedCard.getBackResources());

        //update availablePosition list
        currentPlayer.checkAvailablePositions(position, selectedCard);
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
                currentPlayer.decrementResourceCounter(cardToBeCovered.getResource(angleToBeCovered));
            }catch (NoSuchElementException ignore){}
        }
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal "tris of resources"
     * @param goal goal of player
     * @param player player
     * @return the points for the goal
     */
    public int calculateGoal(IdenticalGoal goal, Player player){
        int pointPerNum = 2;
        int totNum = player.getResourceCounter(goal.getResource()) / goal.getNumOfResource();
        return  totNum * pointPerNum;
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal "3 different resources"
     * @param goal goal of player
     * @param player player
     * @return the points for the goal
     */
    public int calculateGoal(DistinctGoal goal, Player player){
        List<Resource> validResources = new ArrayList<Resource>();
        validResources.add(Tool.FEATHER);
        validResources.add(Tool.SCROLL);
        validResources.add(Tool.PHIAL);

        int pointPerNum = 3;
        int totNum = player.getResourceCounters().entrySet()
                .stream()
                .filter(entry -> validResources.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .min(Integer::compareTo)
                .orElse(0);

        return  totNum * pointPerNum;
    }

    /**
     * @author Riccardo Lapi
     * calculate the points for the goal of 3 cards in a "stair" position
     * @param goal the player goal
     * @param player player
     * @return the points for the given goal
     */
    public int calculateGoal(StairGoal goal, Player player){

        int pointPerNum = 2;

        Map<Coordinates,Card> field = player.getField();
        List<Coordinates> usedCards = new ArrayList<>();
        int numOfStairs = 0;

        int modY = goal.isToLowerRight() ? -1 : 1;

        Reign goalReign = goal.getReign();

        for(Map.Entry<Coordinates, Card> card : field.entrySet()){
            Reign currentReign = (card.getValue() instanceof GoldCard) ? ((GoldCard) card.getValue()).getReign() : ((ResourceCard)card.getValue()).getReign();
            if(!currentReign.equals(goalReign)) continue;

            Coordinates current = card.getKey();

            if(usedCards.contains(current)) continue;

            Coordinates right = new Coordinates(current.x + 1, current.y + modY);
            Coordinates left = new Coordinates(current.x - 1, current.y - modY);

            boolean isStairs = field.containsKey(right) && field.containsKey(left);
            if(isStairs){
                usedCards.add(current);
                usedCards.add(right);
                usedCards.add(left);
                numOfStairs++;
            }
        }

        return numOfStairs * pointPerNum;
    }
    /**
     * @author Riccardo Lapi
     * calculate the points for the goal of 3 cards in a "L" position
     * @param goal the player goal
     * @param player player
     * @return the points for the given goal
     */
    public int calculateGoal(LGoal goal, Player player){
        int pointPerNum = 3;

        Map<Coordinates,Card> field = player.getField();
        List<Coordinates> usedCards = new ArrayList<>();
        int numOfLs = 0;

        int modY, modX;
        if(goal.getPrimaryPosition() == LGoal.PrimaryPosition.BOTTOMRIGHT){
            modX = -1;
            modY = 1;
        }else if(goal.getPrimaryPosition() == LGoal.PrimaryPosition.BOTTOMLEFT){
            modX = 1;
            modY = 1;
        }else if(goal.getPrimaryPosition() == LGoal.PrimaryPosition.TOPRIGHT){
            modX = -1;
            modY = -1;
        }else{
            modX = 1;
            modY = -1;
        }

        Reign goalPrimaryReign = goal.getPrimaryReign();
        Reign goalSecondaryReign = goal.getSecondaryReign();
        for(Map.Entry<Coordinates, Card> card : field.entrySet()){
            Reign currentReign = (card.getValue() instanceof GoldCard) ? ((GoldCard) card.getValue()).getReign() : ((ResourceCard)card.getValue()).getReign();
            if(!currentReign.equals(goalPrimaryReign)) continue;

            Coordinates current = card.getKey();

            if(usedCards.contains(current)) continue;

            Coordinates secondaryA = new Coordinates(current.x + modX, current.y + modY);
            Coordinates secondaryB = new Coordinates(current.x + modX, current.y + modY*2);

            Card cardA = field.get(secondaryA);
            Reign AReign = (cardA instanceof GoldCard) ? ((GoldCard) cardA).getReign() : ((ResourceCard)cardA).getReign();

            Card cardB = field.get(secondaryB);
            Reign BReign = (cardB instanceof GoldCard) ? ((GoldCard) cardB).getReign() : ((ResourceCard)cardB).getReign();

            boolean isL = field.containsKey(secondaryA)
                        && AReign.equals(goalSecondaryReign)
                        && field.containsKey(secondaryB)
                        && BReign.equals(goalSecondaryReign);

            if(isL){
                usedCards.add(current);
                usedCards.add(secondaryA);
                usedCards.add(secondaryB);
                numOfLs++;
            }
        }

        return numOfLs * pointPerNum;
    }

}


