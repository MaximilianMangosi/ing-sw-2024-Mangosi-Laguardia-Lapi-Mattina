package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final Set<ResourceCard> RESOURCE_CARDS= new HashSet<>();
    public static final Set<GoldCard>  GOLD_CARDS= new HashSet<>();
    public static final Set<StarterCard> STARTER_CARDS = new HashSet<>();
    public static final  Set<Goal> GOALS= new HashSet<>();
    public static void main( String[] args )
    {
    }
}
