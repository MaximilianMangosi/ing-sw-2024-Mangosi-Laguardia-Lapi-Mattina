package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.*;

import java.util.*;

public class Player {
    private List<Card> hand;
    private Goal goal;
    private Map<Coordinates,Card> field;
    private HashMap<Resource,Integer> resourceCounters = new HashMap<>();
    private StarterCard starterCard;
    private List<Coordinates> availablePositions;
    private List<Coordinates> unavailablePositions;
    private String name;
    private String check;
    private int points;
    private int goalPoints;

    public Player(String name){
        this.name = name;
    }

    public int getGoalPoints() {
        return goalPoints;
    }

    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Map<Coordinates, Card> getField() {
        return field;
    }

    public void setField(Map<Coordinates, Card> field) {
        this.field = field;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }
    public List<Coordinates> getUnavailablePositions(){return unavailablePositions;}

    public void setAvailablePositions(List<Coordinates> availablePositions) {
        this.availablePositions = availablePositions;
    }

    /**
     * @author Maximilian Mangosi
     * updates available positions for the player
     * @param x coordinate x
     * @param y coordinate y
     */
    private void updateAvailablePositions(int x, int y){
        Coordinates coordinates = new Coordinates(x, y);
        if (!unavailablePositions.contains(coordinates))
            if (!availablePositions.contains(coordinates))
                availablePositions.add(coordinates);
    }

    /**
     * @author Maximilian Mangosi
     * updates available positions for the player
     * @param x coordinate x
     * @param y coordinate y
     */
    private void updateUnavailablePositions(int x, int y){
        Coordinates coordinates = new Coordinates(x, y);
        if (!unavailablePositions.contains(coordinates))
            unavailablePositions.add(coordinates);
    }

    /**
     * @author Maximilian Mangosi
     * the function updates the available positions list after placing a new card
     * @param newCardPositioned position of the newly placed card
     */
    public void checkAvailablePositions(Coordinates newCardPositioned, Card selectedCard) {
        int x = newCardPositioned.x;
        int y = newCardPositioned.y;
        //Verify that the angles are not nonexistent
        if (selectedCard.getResource("NW") != null){ //NW
            updateAvailablePositions(x - 1, y + 1);
        }else{
            updateUnavailablePositions(x-1, y+1);
        }
        if (selectedCard.getResource("SW") != null) { //SW
            updateAvailablePositions(x - 1, y - 1);
        }else{
            updateUnavailablePositions(x-1, y-1);
        }
        if (selectedCard.getResource("NE") != null) { //NE
            updateAvailablePositions(x + 1, y + 1);
        }else{
            updateUnavailablePositions(x+1, y+1);
        }
        if (selectedCard.getResource("SE") != null) { //SE
            updateAvailablePositions(x + 1, y - 1);
        }else{
            updateUnavailablePositions(x+1, y-1);
        }
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @author Maximilian Mangosi
     * @return returns the number of all the resources the player has on the field
     */
    public HashMap<Resource, Integer> getResourceCounters() {
        return resourceCounters;
    }

    /**
     * @author Maximilian Mangosi
     * @return returns the number of all the selected resource on the field
     */
    public int getResourceCounter(Resource resource){
        return resourceCounters.get(resource);
    }

    /**
     * @author Maximilian Mangosi, Giorgio Mattina
     * @param playedCard
     * adds points to the player,
     */
    public void addPoints(GoldCardTool playedCard) {
        points += getResourceCounter(playedCard.getTool());
    }

    /**
     * @author Giorgio Mattina
     * @param playedCard
     * adds point to the player, has to check for covered angles around the played card
     */
    public void addPoints(GoldCardAngles playedCard){
        Coordinates position = new Coordinates(0,0);

        //for loop that finds the coordinates of a given card in the field Map
        for (Map.Entry<Coordinates, Card> instance : field.entrySet()) {

            if (instance.getValue().equals(playedCard)) {
                position = instance.getKey();
                break;
            }
        }

        int x = position.x;
        int y = position.y;
        if(look(x-1, y+1)){
            points+=playedCard.getPoints();
        }
        if(look(x+1, y+1)){
            points+=playedCard.getPoints();
        }
        if(look(x-1, y-1)){
            points+=playedCard.getPoints();
        }
        if(look(x+1, y-1)){
            points+=playedCard.getPoints();
        }
    }

    /**
     * @author Giorgio Mattina
     * @param x
     * @param y
     * @return true if there is a card at (x,y), otherwise returns false
     */
    private boolean look (int x, int y){
        if (getCardAtPosition(x,y)!=null){
            return true;
        }else{
            return false;
        }
    }
    /**
     * @author Giorgio Mattina
     * @param playedCard
     * adds points to the player, it takes a normal gold card that isn't of type Angle or Tool
     */
    public void addPoints(GoldCard playedCard){
        points += playedCard.getPoints();
    }

    /**
     * @author Giorgio Mattina
     * @param playedCard
     * adds points to player, takes a normal resource card
     */
    public void addPoints(ResourceCard playedCard){
        points += playedCard.getPoints();
    }

    /**
     * @author Maximilian Mangosi
     * adds a drawn card to the hand of the player
     */
    public void addCardToHand(Card drawCard) {
        hand.add(drawCard);
    }

    /**
     * @author Maximilian Mangosi
     * @return returns a list with all the cards
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * @author Maximilian Mangosi
     * positions the card on the field at the desired coordinates
     */
    public void addCardToMap(Card selectedCard, Coordinates position) {
        field.put(position, selectedCard);
    }

    /**
     * @author Maximilian Mangosi
     * @return returns the card at a certain coordinate
     */
    public Card getCardAtPosition(int x, int y) {
        //it has to return an object Card at that position on the field
        Coordinates curr = new Coordinates(x ,y);
        Card selectedCard = field.get(curr);
        return selectedCard;
    }

    /**
     * @author Maximilian Mangosi
     * decrements the resource counter of a selected resource
     */
    public void decrementResourceCounter(Resource resource) {
        int temp = resourceCounters.get(resource);
        resourceCounters.replace(resource, temp-1);
    }

    /**
     * @author Maximilian Mangosi
     * updates the resource counter
     */
    public void updateResourceCounter(List <Resource> resourceList){
        for (Resource r : resourceList){
            int temp = resourceCounters.get(r);
            resourceCounters.replace(r, temp+1);
        }
    }

    /**
     * set the check's color to given value if is accepted {"Red","Blue","Yellow","Green"}, otherwise nothing happens
     * @author Giuseppe Laguardia
     * @param s a String representing the color of the check,
     */
    public void setColor(String s) {
        String[] colorArray={"Red","Blue","Yellow","Green"};
        if(Arrays.asList(colorArray).contains(s)){
            check=s;
        }
    }
}

