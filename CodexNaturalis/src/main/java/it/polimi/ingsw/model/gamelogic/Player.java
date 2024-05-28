package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.util.*;

public class Player {
    private List<Card> hand=new ArrayList<>();
    private Goal[] goalOptions=new Goal[2];
    private Goal goal;
    private Map<Coordinates,Card> field=new HashMap<>();
    private HashMap<Resource,Integer> resourceCounters = new HashMap<>();
    private StarterCard starterCard;
    private List<Coordinates> availablePositions=new ArrayList<>();
    private List<Coordinates> unavailablePositions=new ArrayList<>();
    private String name;
    private String check;
    private int points;
    private int goalPoints;

    private Map<String, List<String>> privateChat = new HashMap<>();

    public List<String> getPrivateChat(String name){
        return privateChat.get(name);
    }

    public Map<String, List<String>> getPrivateChats (){
        return privateChat;
    }

    public void addMessage(String name, String message){
        privateChat.get(name).add(message);
    }

    /**
     * constructor of class Player
     * @author Giuseppe Laguardia
     * @param name the player's nickname
     */
    public Player(String name){
        this.name = name;
    }

    /**
     * getter of number of points obtained through goalCards
     * @author Giuseppe Laguardia
     * @return
     */
    public int getGoalPoints() {
        return goalPoints;
    }

    /**
     * setter of  number of points obtained through goalCards
     * @author Giuseppe Laguardia
     * @param goalPoints
     */
    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    /**
     * setter of the player's hand
     * @author Giuseppe Laguardia
     * @param hand
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * getter of the player's private goal
     * @author Giuseppe Laguardia
     * @return
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * setter of player's private goal
     * @author Giuseppe Laguardia
     * @param goal
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * getter of the player's field (Map Coordinates to Card)
     * @author Giuseppe Laguardia
     * @return Map of Coordinates-Card
     */
    public Map<Coordinates, Card> getField() {
        return field;
    }

    /**
     * setter of player's personal field
     * @author Giuseppe Laguardia
     * @param field
     */
    public void setField(Map<Coordinates, Card> field) {
        this.field = field;
    }

    /**
     * getter of player's Starter Card
     * @author Giuseppe Laguardia
     * @return player's starterCard
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * setter of player's Starter Card
     * @author Giuseppe Laguardia
     * @param starterCard
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * getter of List of available positions
     * @author Giuseppe Laguardia
     * @return list of coordinates, the player's available positions
     */

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * getter of player's list of unavailable positions
     * @author Giuseppe Laguardia
     * @return List of coordinates
     */
    public List<Coordinates> getUnavailablePositions(){return unavailablePositions;}

    /**
     * setter of player's list of available positions
     * @author Giuseppe Laguardia
     * @param availablePositions, a list of coordinates
     */
    public void setAvailablePositions(List<Coordinates> availablePositions) {
        this.availablePositions = availablePositions;
    }

    /**
     * getter of the two player's goal options
     * @author Giuseppe Laguardia
     * @return an array of size 2, containing two Goal objects
     */
    public Goal[] getGoalOptions() {
        return goalOptions;
    }

    /**
     * setter  of the two player's goal options
     * @author Giorgio Mattina
     * @param goalOptions, an array of goals of size 2
     */
    public void setGoalOptions(Goal[] goalOptions) {
        this.goalOptions = goalOptions;
    }
    /**
     * @author Maximilian Mangosi
     * updates available positions for the player
     * @param x coordinate x
     * @param y coordinate y
     */
    public void updateAvailablePositions(int x, int y){
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
    public void updateUnavailablePositions(int x, int y){
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
        availablePositions.remove(newCardPositioned);
        unavailablePositions.add(newCardPositioned);
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


    /**
     * getter of player's nickname
     * @author Giuseppe Laguardia
     * @return player's nickname
     */
    public String getName() {
        return name;
    }

    /**
     * setter of player's name
     * @author Giuseppe Laguardia
     * @param name, the player's nickname
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of player's pawn's color
     * @author Giuseppe Laguardia
     * @return the color of the player's pawn
     */
    public String getCheck() {
        return check;
    }

    /**
     * setter of player's pawn's color
     * @author Giuseppe Laguardia
     * @param check, a color
     */
    public void setCheck(String check) {
        this.check = check;
    }

    /**
     *  getter of player's points
     * @author Giuseppe Laguardia
     * @return player's points
     */
    public int getPoints() {
        return points;
    }

    /**
     * setter of player's points
     * @author Giuseppe Laguardia
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * removes the Card parameter from player's hand
     * @author Giorgio Mattina
     * @param selectedCard
     */
    public void removeFromHand(Card  selectedCard){
        hand.remove(selectedCard);
    }
    /**
     * @author Maximilian Mangosi
     * @return returns a map of player's Resources with relative quantity on the field
     */
    public HashMap<Resource, Integer> getResourceCounters() {
        return resourceCounters;
    }

    /**
     * @author Maximilian Mangosi
     * @return returns the number of all the selected resource on the field
     */
    public int getResourceCounter(Resource resource){
        return resourceCounters.getOrDefault(resource,0);
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
    public boolean look(int x, int y){
        if (getCardAtPosition(x,y)!=null){
            return true;
        }else{
            return false;
        }
    }

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
            int temp = resourceCounters.getOrDefault(r,0);
            resourceCounters.put(r, temp+1);
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

    /**
     * sets the player's resource counter to value "i"
     * @author Giorgio Mattina
     * @param resource
     * @param i
     */
    public void setResourceCounter(Resource resource,Integer i){
        resourceCounters.put(resource,i);
    }
}

