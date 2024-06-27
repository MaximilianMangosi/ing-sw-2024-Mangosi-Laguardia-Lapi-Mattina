package it.polimi.ingsw;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.IdenticalGoal;
import it.polimi.ingsw.model.gamecards.goals.LGoal;
import it.polimi.ingsw.model.gamecards.goals.StairGoal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IdenticalGoalTest {

    GameBox gb=new GameBox();
    IdenticalGoal goal = new IdenticalGoal(2, Reign.MUSHROOM, 3,95);
    Player p=new Player("p");
    @Before
    public void GameBoxSetup() throws IOException {

        ArrayList<String> resourceCardJsons=new ArrayList<>();
        String resourceCardPath="/jsons/ResourceCard/ResourceCard_";
        int numOfResourceCard=40;

        ArrayList<String> goldCardJsons=new ArrayList<>();
        String goldCardPath="/jsons/GoldCard/GoldCard_";
        int numOfGoldCard=16;

        ArrayList<String> goldCardAnglesJsons=new ArrayList<>();
        String goldCardAnglesPath="/jsons/GoldCard/GoldCardAngles/GoldCardAngles_";
        int numOfGoldCardAngles=12;

        ArrayList<String> goldCardToolJsons=new ArrayList<>();
        String goldCardToolPath="/jsons/GoldCard/GoldCardTool/GoldCardTool_";
        int numOfGoldCardTool=12;

        ArrayList<String> starterCardJsons=new ArrayList<>();
        String starterCardPath="/jsons/StarterCard/StarterCard_";
        int numOfStarterCard=6;

        ArrayList<String> identicalGoalJsons=new ArrayList<>();
        String identicalGoalPath="/jsons/Goal/IdenticalGoal/IdenticalGoal_";
        int numOfIdenticalGoal=7;

        ArrayList<String> LGoalJsons=new ArrayList<>();
        String LGoalPath="/jsons/Goal/LGoal/LGoal_";
        int numOfLGoal=4;

        ArrayList<String> stairGoalJsons=new ArrayList<>();
        String stairGoalPath="/jsons/Goal/StairGoal/StairGoal_";
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
    public void calculate_test(){
        StarterCard starterCard= gb.getStarterCardSet().stream().toList().getFirst();

        List<Integer> mashCardsIds = new ArrayList<>();
        mashCardsIds.add(5);

        mashCardsIds.add(3);
        mashCardsIds.add(4);
        mashCardsIds.add(6);
        mashCardsIds.add(2);

        ArrayList<Card> mashCard= new ArrayList<>( gb.getResourceCardSet().stream().filter(c->mashCardsIds.contains(c.getId())).toList());

        mashCard.get(0).setIsFront(true);
        mashCard.get(1).setIsFront(true);
        mashCard.get(2).setIsFront(true);
        mashCard.get(3).setIsFront(true);
        mashCard.get(4).setIsFront(true);

        p.addCardToMap(starterCard,new Coordinates(0,0));
        p.addCardToMap(mashCard.get(0),new Coordinates(-2,-2));
        p.addCardToMap(mashCard.get(1),new Coordinates(-5,5));
        p.addCardToMap(mashCard.get(2),new Coordinates(0,-8));
        p.addCardToMap(mashCard.get(3),new Coordinates(0,10));
        p.addCardToMap(mashCard.get(4),new Coordinates(4,-10));

        assertEquals(6,goal.calculateGoal(p));
    }

}
