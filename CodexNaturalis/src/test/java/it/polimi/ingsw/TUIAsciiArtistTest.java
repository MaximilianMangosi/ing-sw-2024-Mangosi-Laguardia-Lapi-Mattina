package it.polimi.ingsw;

import it.polimi.ingsw.client.OutStreamWriter;
import it.polimi.ingsw.client.TUIAsciiArtist;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.GoldCardAngles;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;


public class TUIAsciiArtistTest {
    public TUIAsciiArtist artist=new TUIAsciiArtist(new OutStreamWriter());
    public Game game;
    public GameBox gb = new GameBox();
    private void fillList(ArrayList<String> jsonsList,int numOfJson,String path) throws IOException {
        for (int i = 1; i <=numOfJson; i++) {
            jsonsList.add(Files.readString(Path.of(path+i+".json")));
        }
    }
    @Before
    public void GameSetup() throws IOException {


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
            fillList(resourceCardJsons,numOfResourceCard,resourceCardPath);
            fillList(goldCardJsons,numOfGoldCard,goldCardPath);
            fillList(goldCardAnglesJsons,numOfGoldCardAngles,goldCardAnglesPath);
            fillList(goldCardToolJsons,numOfGoldCardTool,goldCardToolPath);
            fillList(starterCardJsons,numOfStarterCard,starterCardPath);
            fillList(identicalGoalJsons,numOfIdenticalGoal,identicalGoalPath);
            fillList(LGoalJsons,numOfLGoal,LGoalPath);
            fillList(stairGoalJsons,numOfStairGoal,stairGoalPath);}
        catch (IOException e){
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
        Player p3= new Player("Maxi");
        Player p4 = new Player("Mario");
        game= new Game(p1,4,gb);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

    }

    @Test
    public void showCardTest (){
        List<Card> deck =new ArrayList<>(gb.getResourceCardSet());
        shuffle(deck);
        deck= deck.stream().limit(4).toList();
        artist.show(deck);
        artist.show(deck.getLast(),true);

    }
    @Test
    public void showGoalTest (){
        List<Goal> deck =new ArrayList<>(gb.getGoalSet());
        shuffle(deck);
        Goal[] options = new Goal[2];
        options[0] = deck.removeFirst();
        options[1] = deck.removeFirst();

        artist.show(options);

    }
    @Test
    public void showStarterCardTest(){
        List<StarterCard> deck = new ArrayList<>(gb.getStarterCardSet());
        //shuffle(deck);
        for (StarterCard s : deck) {
            artist.show(s,true);
        }

    }

}
