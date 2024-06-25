package it.polimi.ingsw;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.goals.DistinctGoal;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static org.junit.Assert.assertEquals;

public class DistinctGoalTest {
    public DistinctGoal distinctGoal=new DistinctGoal(3, 1);
    @Before
    public void distinctGoalSetup (){
        Player p = new Player("Pepo");
    }
    @Test
    public void calculateGoalTest(){
        Player p = new Player("Pepo");
        Map <Resource,Integer> resources = p.getResourceCounters();
        resources.put(Tool.FEATHER,2);
        resources.put(Tool.PHIAL,2);
        resources.put(Tool.SCROLL,3);
        int r =distinctGoal.calculateGoal(p);
        assertEquals(6,r);
    }
}
