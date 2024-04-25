package it.polimi.ingsw;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.controller.exceptions.DeckEmptyException;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    public Game game;

    @Before
    public void GameSetup() throws IOException {
        GameBox gb= new GameBox();
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

        fillList(resourceCardJsons,numOfResourceCard,resourceCardPath);
        fillList(goldCardJsons,numOfGoldCard,goldCardPath);
        fillList(goldCardAnglesJsons,numOfGoldCardAngles,goldCardAnglesPath);
        fillList(goldCardToolJsons,numOfGoldCardTool,goldCardToolPath);
        fillList(starterCardJsons,numOfStarterCard,starterCardPath);
        fillList(identicalGoalJsons,numOfIdenticalGoal,identicalGoalPath);
        fillList(LGoalJsons,numOfLGoal,LGoalPath);
        fillList(stairGoalJsons,numOfStairGoal,stairGoalPath);

        gb.addToResourceCardSet(resourceCardJsons);
        gb.addGoldCards(goldCardJsons);
        gb.addGoldCardAngles(goldCardAnglesJsons);
        gb.addGoldCardTools(goldCardToolJsons);
        gb.addToStarterCardSet(starterCardJsons);
        gb.addIdenticalGoals(identicalGoalJsons);
        gb.addLGoals(LGoalJsons);
        gb.addStairGoals(stairGoalJsons);
        gb.addDistinctGoals();

        Player p1= new Player("Pepo");
        Player p2= new Player("Iugal");
        Player p3= new Player("Maxi il processo che ha sconfitto la mafia");
        game= new Game(p1,3,gb);
        game.addPlayer(p2);
        game.addPlayer(p3);


    }
    @Test
    public void startGameTest(){
        game.startGame();
        for(Player p: game.getPlayers()){
            //check if the player has only 2 ResourceCard and only 1 ResourceCard in his hand
            assertEquals(2, p.getHand().stream().filter(card -> card instanceof ResourceCard).count());
            assertEquals(1, p.getHand().stream().filter(card -> card instanceof GoldCard).count());
            assertEquals(3,p.getHand().size());

            //check if the goalOptions are not null and not the same
            for(Goal goal: p.getGoalOptions()){
                assertNotNull(goal);
            }
            assertNotEquals(p.getGoalOptions()[0], p.getGoalOptions()[1]);
            //check if the goal is null
            assertNull(p.getGoal());
            for (int cnt: p.getResourceCounters().values()){
                assertNotEquals(0,cnt);
            }
            //check if the starterCard is not null
            assertNotNull(p.getStarterCard());
            //check if available position list contains only the (0,0) position
            assertTrue(p.getAvailablePositions().contains(new Coordinates(0,0)));
            assertEquals(1,p.getAvailablePositions().size());
            //check if unavailable position is empty
            assertTrue(p.getUnavailablePositions().isEmpty());
            //check if points are 0
            assertEquals(0,p.getPoints());
            assertEquals(0,p.getGoalPoints());

        }
        assertEquals(game.getCurrentPlayer(),game.getPlayers().getFirst());
    }
   @Test
    public void playCardFrontFromHandTest() throws DeckEmptyException, HandFullException, RequirementsNotMetException {
        game.startGame();
        int oldPoints=game.getCurrentPlayer().getPoints();
        Card cardPlayed=game.getCurrentPlayer().getHand().removeFirst();
        game.playCardFront(cardPlayed,new Coordinates(1,0));
        assertEquals(oldPoints+cardPlayed.getPoints(),game.getCurrentPlayer().getPoints());
        assertTrue(game.getCurrentPlayer().getField().containsValue(cardPlayed));
    }

    private void fillList(ArrayList<String> jsonsList,int numOfJson,String path) throws IOException {
        for (int i = 1; i <=numOfJson; i++) {
            jsonsList.add(Files.readString(Path.of(path+i+".json")));
        }
    }
}
