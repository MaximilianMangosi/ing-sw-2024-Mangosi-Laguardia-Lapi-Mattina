package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.*;

import java.util.*;

public class Player {
    private List<Card> hand;
    private Goal goal;
    private Map<Coordinates,Card> field;

    private StarterCard starterCard;

    private String name;
    private String check;
    private int points;

    private Map<Resource,Integer> resourceCounters = new HashMap<>();

    public HashMap<Resource, Integer> getResourceCounters() {
        return resourceCounters;
    }
    public int getResourceCounter(Resource resource){
        return resourceCounters[resource];
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

