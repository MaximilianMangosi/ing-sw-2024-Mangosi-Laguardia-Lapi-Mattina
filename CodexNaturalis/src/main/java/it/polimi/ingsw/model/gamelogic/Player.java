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
        if (!availablePositions.contains(coordinates))
            availablePositions.add(coordinates);
    }

    /**
     * @author Maximilian Mangosi
     * eliminates poition that is no longer available
     * @param x coordinate x
     * @param y coordinate y
     */
    private void removeFromAvailablePositions(int x, int y){
        Coordinates coordinates = new Coordinates(x, y);
        if (!availablePositions.contains(coordinates))
            availablePositions.remove(coordinates);
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
            iteratorForAvailablePositions(x + 1, y - 1);
        }else{
            removeFromAvailablePositions(x+1, y-1);
        }
        if (selectedCard.getResource("SW") != null) { //SW
            iteratorForAvailablePositions(x + 1, y + 1);
        }else{
            removeFromAvailablePositions(x+1, y+1);
        }
        if (selectedCard.getResource("NE") != null) { //NE
            iteratorForAvailablePositions(x - 1, y - 1);
        }else{
            removeFromAvailablePositions(x-1, y-1);
        }
        if (selectedCard.getResource("SE") != null) { //SE
            iteratorForAvailablePositions(x - 1, y + 1);
        }else{
            removeFromAvailablePositions(x-1, y+1);
        }
    }

    /**
     * @author Maximilian Mangosi
     * if a card already exists at that position then don't update the available position list
     * otherwise check if the angle opposite is not nonexistent
     * if not then all the update function
     * @param x coordinate x
     * @param y coordinate y
     */
    private void iteratorForAvailablePositions(int x, int y){
        if (getCardAtPosition(x,y) != null){
            if(isThatCardOk(x+1, y-1, "SE")
                    && isThatCardOk(x+1, y+1, "NE")
                    && isThatCardOk(x-1, y-1, "SW")
                    && isThatCardOk(x-1, y+1, "NW")){
                updateAvailablePositions(x, y);
            }else{
                //removeFromAvailablePositions(x, y);
            }
        }else{
            updateAvailablePositions(x, y);
        }
    }

    /**
     * @author Maximilian Mangosi
     * @param x coordinate x
     * @param y coordinate y
     * @param cardinal cardinal position
     * @return returns true if the cardinal position is not nonexistent and if there is no card
     */
    private boolean isThatCardOk(int x, int y, String cardinal){
        if (getCardAtPosition(x, y) != null) {
            if (getCardAtPosition(x, y).getResource(cardinal) != null) {
                //the position could be valid
                return true;
            }
            return false;
        }else {
            return true;
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
     * @author Maximilian Mangosi
     * adds points to the player
     */
    public void addPoints(int selectedCardPoints) {
        points = points + selectedCardPoints;
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
}

