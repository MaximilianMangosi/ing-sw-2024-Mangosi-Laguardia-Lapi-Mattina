package it.polimi.ingsw.client;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.util.List;

public class TUIAsciiArtist implements CardDisplay {
    private StringBuilder strbuilder = new StringBuilder();
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    @Override
    public void show(Card card) {

        strbuilder.append(card.getReign().getColor()); //

        strbuilder.append(card.getCardResources().removeFirst().toString());
        strbuilder.append("##" + card.getPoints() + "##\n");
        //strbuilder.append("##" + card.get);

    }
}
