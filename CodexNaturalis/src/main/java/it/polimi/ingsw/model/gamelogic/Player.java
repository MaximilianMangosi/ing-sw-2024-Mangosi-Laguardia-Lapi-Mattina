package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.*;

import java.util.*;

public class Player {
    private List<Card> hand;
    private Goal goal;
    private Map<Coordinates,Card> field;
    private Map<Resource,Integer> resourceCounters = new HashMap<>();
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

    public HashMap<Resource, Integer> getResourceCounters() {
        return resourceCounters;
    }
    public int getResourceCounter(Resource resource){
        return resourceCounters.get(resource);
    }

    public void addPoints(int selectedCardPoints) {
    }


    public void addCardToHand(Card drawCard) {

    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public void addCardToMap(Card selectedCard, Coordinates position) {
    }

    public Card getCardAtPosition(int x, int y) {
        //it has to return an object Card at that position on the field
    }

    public void decrementResourceCounter(Resource resource) {
    }

    public void updateResourceCounter(List <Resource> resourceList){

    }
}

