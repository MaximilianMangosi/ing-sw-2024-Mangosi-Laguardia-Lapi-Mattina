package it.polimi.ingsw;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.controller.exceptions.DeckEmptyException;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.server.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    public Game game;
    public GameBox gb= new GameBox();
    @Before
    public void GameSetup() throws IOException {


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
        fillList(resourceCardJsons,numOfResourceCard,resourceCardPath);
        fillList(goldCardJsons,numOfGoldCard,goldCardPath);
        fillList(goldCardAnglesJsons,numOfGoldCardAngles,goldCardAnglesPath);
        fillList(goldCardToolJsons,numOfGoldCardTool,goldCardToolPath);
        fillList(starterCardJsons,numOfStarterCard,starterCardPath);
        fillList(identicalGoalJsons,numOfIdenticalGoal,identicalGoalPath);
        fillList(LGoalJsons,numOfLGoal,LGoalPath);
        fillList(stairGoalJsons,numOfStairGoal,stairGoalPath);}
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
                assertEquals(0,cnt);
            }
            //check if the starterCard is not null
            assertNotNull(p.getStarterCard());
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
    @Test
    public void playCardResourceCounterTest() throws RequirementsNotMetException {
        Player p = new Player("pepo");
        game.setCurrentPlayer(p);
        p.setAvailablePositions(new ArrayList<>());


        p.addCardToHand(new ResourceCard(Reign.MUSHROOM,Reign.EMPTY,Reign.MUSHROOM,null,0,Reign.MUSHROOM, 1));
        p.addCardToHand(new ResourceCard(Reign.MUSHROOM,Reign.MUSHROOM,null,Reign.EMPTY,0,Reign.MUSHROOM, 1));
        p.addCardToHand(new ResourceCard(null,Reign.BUG,Tool.PHIAL,Reign.ANIMAL,0,Reign.ANIMAL, 1));
        Reign[] arr= {Reign.BUG};
        p.setStarterCard(new StarterCard(Reign.EMPTY,Reign.PLANTS,Reign.BUG,Reign.EMPTY,Reign.MUSHROOM,Reign.PLANTS,Reign.BUG,Reign.ANIMAL,new ArrayList<>(List.of(arr)), 1));
        initializeResourceCounter(p);
        game.playStarterCard(p,true);
        assertEquals(1,p.getResourceCounter(Reign.PLANTS));
        assertEquals(2,p.getResourceCounter(Reign.BUG));
        assertEquals(0,p.getResourceCounter(Reign.ANIMAL));
        assertEquals(0,p.getResourceCounter(Reign.MUSHROOM));
        assertEquals(0,p.getResourceCounter(Tool.FEATHER));
        assertEquals(0,p.getResourceCounter(Tool.PHIAL));
        assertEquals(0,p.getResourceCounter(Tool.SCROLL));
        game.playCardFront(p.getHand().getFirst(),new Coordinates(1,1));
        assertEquals(0,p.getResourceCounter(Reign.PLANTS));
        assertEquals(2,p.getResourceCounter(Reign.BUG));
        assertEquals(0,p.getResourceCounter(Reign.ANIMAL));
        assertEquals(2,p.getResourceCounter(Reign.MUSHROOM));
        assertEquals(0,p.getResourceCounter(Tool.FEATHER));
        assertEquals(0,p.getResourceCounter(Tool.PHIAL));
        assertEquals(0,p.getResourceCounter(Tool.SCROLL));
        game.playCardFront(p.getHand().getFirst(),new Coordinates(-1,1));
        assertEquals(0,p.getResourceCounter(Reign.PLANTS));
        assertEquals(2,p.getResourceCounter(Reign.BUG));
        assertEquals(0,p.getResourceCounter(Reign.ANIMAL));
        assertEquals(4,p.getResourceCounter(Reign.MUSHROOM));
        assertEquals(0,p.getResourceCounter(Tool.FEATHER));
        assertEquals(0,p.getResourceCounter(Tool.PHIAL));
        assertEquals(0,p.getResourceCounter(Tool.SCROLL));
        game.playCardFront(p.getHand().getFirst(),new Coordinates(-1,-1));
        assertEquals(0,p.getResourceCounter(Reign.PLANTS));
        assertEquals(2,p.getResourceCounter(Reign.BUG));
        assertEquals(1,p.getResourceCounter(Reign.ANIMAL));
        assertEquals(4,p.getResourceCounter(Reign.MUSHROOM));
        assertEquals(0,p.getResourceCounter(Tool.FEATHER));
        assertEquals(1,p.getResourceCounter(Tool.PHIAL));
        assertEquals(0,p.getResourceCounter(Tool.SCROLL));
    }
    @Test
    public void checkRequirementsTest_Passed() throws RequirementsNotMetException {
        Player p= new Player("Gianni");
        game.addPlayer(p);
        game.setCurrentPlayer(p);
        initializeResourceCounter(p);
        StarterCard starterCard= gb.getStarterCardSet().stream().filter(c->c.getId()==82).findAny().get();
        p.setStarterCard(starterCard);
        game.playStarterCard(p,true);
        p.addCardToHand(gb.getGoldCardSet().stream().filter(c->c.getId()==41).findAny().get());
        p.addCardToHand(gb.getResourceCardSet().stream().filter(c->c.getId()==15).findAny().get());
        p.addCardToHand(gb.getResourceCardSet().iterator().next());

        game.playCardFront(p.getHand().get(1),new Coordinates(-1,-1));
        game.playCardFront(p.getHand().getFirst(),new Coordinates(1,1));


    }

    private void initializeResourceCounter(Player p) {
        Resource[] resourceArray={Reign.ANIMAL,Reign.MUSHROOM,Reign.BUG,Reign.PLANTS, Tool.PHIAL,Tool.FEATHER,Tool.SCROLL};

        for (Resource resource: resourceArray){
            p.setResourceCounter(resource,0);
        }
    }

    public static void fillList(ArrayList<String> jsonsList,int numOfJson,String path) throws IOException, URISyntaxException {
        for (int i = 1; i <=numOfJson; i++) {
                jsonsList.add(Files.readString(Path.of(Server.class.getResource(path+i+".json").toURI())));
        }
    }

}
