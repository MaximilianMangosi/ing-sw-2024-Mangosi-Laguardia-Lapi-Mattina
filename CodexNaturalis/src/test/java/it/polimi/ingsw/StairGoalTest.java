package it.polimi.ingsw;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.StairGoal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class StairGoalTest {
    GameBox gb=new GameBox();
    StairGoal goal = new StairGoal(2, Reign.PLANTS,true, 1);
    Player p=new Player("pepo");
    @Before
    public void GameBoxSetup() throws IOException {

        ArrayList<String> resourceCardJsons=new ArrayList<>();
        String resourceCardPath="src/jsons/ResourceCard/ResourceCard_";
        int numOfResourceCard=40;

        ArrayList<String> goldCardJsons=new ArrayList<>();
        String goldCardPath="src/jsons/GoldCard/GoldCard_";
        int numOfGoldCard=16;

        ArrayList<String> goldCardAnglesJsons=new ArrayList<>();
        String goldCardAnglesPath="src/jsons/GoldCard/GoldCardAngles/GoldCardAngles_";
        int numOfGoldCardAngles=12;

        ArrayList<String> goldCardToolJsons=new ArrayList<>();
        String goldCardToolPath="src/jsons/GoldCard/GoldCardTool/GoldCardTool_";
        int numOfGoldCardTool=12;

        ArrayList<String> starterCardJsons=new ArrayList<>();
        String starterCardPath="src/jsons/StarterCard/StarterCard_";
        int numOfStarterCard=6;

        ArrayList<String> identicalGoalJsons=new ArrayList<>();
        String identicalGoalPath="src/jsons/Goal/IdenticalGoal/IdenticalGoal_";
        int numOfIdenticalGoal=7;

        ArrayList<String> LGoalJsons=new ArrayList<>();
        String LGoalPath="src/jsons/Goal/LGoal/LGoal_";
        int numOfLGoal=4;

        ArrayList<String> stairGoalJsons=new ArrayList<>();
        String stairGoalPath="src/jsons/Goal/StairGoal/StairGoal_";
        int numOfStairGoal=4;
        try{
            GameTest.fillList(resourceCardJsons,numOfResourceCard,resourceCardPath);
            GameTest.fillList(goldCardJsons,numOfGoldCard,goldCardPath);
            GameTest.fillList(goldCardAnglesJsons,numOfGoldCardAngles,goldCardAnglesPath);
            GameTest.fillList(goldCardToolJsons,numOfGoldCardTool,goldCardToolPath);
            GameTest.fillList(starterCardJsons,numOfStarterCard,starterCardPath);
            GameTest.fillList(identicalGoalJsons,numOfIdenticalGoal,identicalGoalPath);
            GameTest.fillList(LGoalJsons,numOfLGoal,LGoalPath);
            GameTest.fillList(stairGoalJsons,numOfStairGoal,stairGoalPath);}
        catch (IOException | URISyntaxException e){
            System.out.println("Error during json file reading:\n"+e.getMessage());
        }

        gb.addToResourceCardSet(resourceCardJsons);
        gb.addGoldCards(goldCardJsons);
        gb.addGoldCardAngles(goldCardAnglesJsons);
        gb.addGoldCardTools(goldCardToolJsons);
        gb.addToStarterCardSet(starterCardJsons);
        gb.addIdenticalGoals(identicalGoalJsons);
        gb.addLGoals(LGoalJsons);
        gb.addStairGoals(stairGoalJsons);
        gb.addDistinctGoals();


    }
    @Test
    public void calculateGoal_OneStair_Test(){
        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();
        ArrayList<Card> plantsCard= new ArrayList<>( gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        plantsCard.addAll(gb.getResourceCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(plantsCard.get(5),new Coordinates(1,1));
        p.addCardToMap(plantsCard.get(6),new Coordinates(0,2));
        p.addCardToMap(plantsCard.get(7),new Coordinates(-1,3));

        assertEquals(2,goal.calculateGoal(p));
    }
    @Test
    public void calculateGoal_ZeroStair_Test(){

        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();
        ArrayList<Card> plantsCard= new ArrayList<>( gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        plantsCard.addAll(gb.getResourceCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        GoldCard stairInterruption=  gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.BUG).findAny().get();

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(plantsCard.get(5),new Coordinates(1,1));
        p.addCardToMap(stairInterruption,new Coordinates(0,2));
        p.addCardToMap(plantsCard.get(7),new Coordinates(-1,3));

        assertEquals(0,goal.calculateGoal(p));
    }
    @Test
    public void calculateGoal_NoReuse_Test(){

        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();
        ArrayList<Card> plantsCard= new ArrayList<>( gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        plantsCard.addAll(gb.getResourceCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(plantsCard.get(5),new Coordinates(1,1));
        p.addCardToMap(plantsCard.get(6), new Coordinates(0,2));
        p.addCardToMap(plantsCard.get(7),new Coordinates(-1,3));
        p.addCardToMap(plantsCard.get(8),new Coordinates(-2,4));
        p.addCardToMap(plantsCard.get(9),new Coordinates(-3,5));

        assertEquals(2,goal.calculateGoal(p));
    }
    @Test
    public void calculateGoal_TwoConsecutiveStair_Test(){

        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();
        ArrayList<Card> plantsCard= new ArrayList<>( gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        plantsCard.addAll(gb.getResourceCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(plantsCard.get(5),new Coordinates(1,1));
        p.addCardToMap(plantsCard.get(6), new Coordinates(0,2));
        p.addCardToMap(plantsCard.get(7),new Coordinates(-1,3));
        p.addCardToMap(plantsCard.get(8),new Coordinates(-2,4));
        p.addCardToMap(plantsCard.get(9),new Coordinates(-3,5));
        p.addCardToMap(plantsCard.get(10),new Coordinates(-4,6));

        assertEquals(4,goal.calculateGoal(p));
    }
    public void calculateGoal_WrongDirection_Test(){
        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();
        ArrayList<Card> plantsCard= new ArrayList<>( gb.getGoldCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());
        plantsCard.addAll(gb.getResourceCardSet().stream().filter(c->c.getReign()==Reign.PLANTS).toList());

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(plantsCard.get(5),new Coordinates(1,1));
        p.addCardToMap(plantsCard.get(6),new Coordinates(2,2));
        p.addCardToMap(plantsCard.get(7),new Coordinates(3,3));

        assertEquals(0,goal.calculateGoal(p));
    }
}
