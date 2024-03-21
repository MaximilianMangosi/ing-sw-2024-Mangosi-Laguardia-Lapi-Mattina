package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Player {
    private List<Card> hand;
    private Goal goal;
    private Map<Coordinates,Card> field;

    private StarterCard starterCard;

    private String name;
    private String check;
    private int points;

    private Map<Resource,Integer> resourceCounters;

    public static void addPoints(int selectedCardPoints) {
    }


    public void addCardToHand(Card drawCard) {

    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public void addCardToMap(ResourceCard selectedCard, Coordinates position) {
    }
}

