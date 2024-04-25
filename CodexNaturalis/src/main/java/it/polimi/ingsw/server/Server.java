package it.polimi.ingsw.server;

import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamelogic.GameManager;

import javax.swing.text.View;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Server {
    GameManager gameManager=new GameManager();
    public static void main(String[] argv){
        //View = new View()
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
        try {
            fillList(resourceCardJsons, numOfResourceCard, resourceCardPath);
            fillList(goldCardJsons, numOfGoldCard, goldCardPath);
            fillList(goldCardAnglesJsons, numOfGoldCardAngles, goldCardAnglesPath);
            fillList(goldCardToolJsons, numOfGoldCardTool, goldCardToolPath);
            fillList(starterCardJsons, numOfStarterCard, starterCardPath);
            fillList(identicalGoalJsons, numOfIdenticalGoal, identicalGoalPath);
            fillList(LGoalJsons, numOfLGoal, LGoalPath);
            fillList(stairGoalJsons, numOfStairGoal, stairGoalPath);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        try {
            Registry registry= LocateRegistry.createRegistry(23);
        }catch (RemoteException e){
            System.out.println("Connection error unable to export object"+e.getMessage());
        }


    }
    static void fillList(ArrayList<String> jsonsList,int numOfJson,String path) throws IOException {
        for (int i = 1; i <=numOfJson; i++) {
            jsonsList.add(Files.readString(Path.of(path+i+".json")));
        }
    }

}
