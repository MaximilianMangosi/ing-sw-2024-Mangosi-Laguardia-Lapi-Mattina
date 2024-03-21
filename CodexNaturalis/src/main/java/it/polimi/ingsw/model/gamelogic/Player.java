package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;
import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Map;

public class Player {
    private List<Card> hand;
    private Goal goal;
    private Map<Coordinates,Card> field;

    private StarterCard starterCard;

    private String name;
    private String check;
    private int points;

    private Map<Resource,Integer> resourceCounters;

}
