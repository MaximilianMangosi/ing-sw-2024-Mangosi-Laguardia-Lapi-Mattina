package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamelogic.GameManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Server {
    public static void main(String[] argv) {
        GameBox gb = new GameBox();
        //GAMEBOX SETUP
        try {
            gameBoxSetup(gb);
        } catch (IOException e) {
            System.out.println("Error occurred during json file's reading:\n "+e.getMessage());
            return;
        }

        // MAIN SERVER LOGIC
        try {
            // GameManager setup
            GameManager gameManager = new GameManager();
            gameManager.setGameBox(gb);
            System.out.println("GameBox ready");
            //Controller and View setup
            Controller controller = new Controller(gameManager);
            View view = controller.getView();

            // export View
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ViewRMI", view);
            System.out.println("Remote View has been correctly exported");

            CloseGame t1 = new CloseGame(controller);
            t1.start();
            //TODO DisconnectionHandler
        } catch (RemoteException ex) {
            System.out.println("Connection error unable to export object:\n" + ex.getMessage());}
    }

    private static void gameBoxSetup(GameBox gb) throws IOException {
        ArrayList<String> resourceCardJsons = new ArrayList<>();
        String resourceCardPath = "src/jsons/ResourceCard/ResourceCard_";
        int numOfResourceCard = 40;

        ArrayList<String> goldCardJsons = new ArrayList<>();
        String goldCardPath = "src/jsons/GoldCard/GoldCard_";
        int numOfGoldCard = 16;

        ArrayList<String> goldCardAnglesJsons = new ArrayList<>();
        String goldCardAnglesPath = "src/jsons/GoldCard/GoldCardAngles/GoldCardAngles_";
        int numOfGoldCardAngles = 12;

        ArrayList<String> goldCardToolJsons = new ArrayList<>();
        String goldCardToolPath = "src/jsons/GoldCard/GoldCardTool/GoldCardTool_";
        int numOfGoldCardTool = 12;

        ArrayList<String> starterCardJsons = new ArrayList<>();
        String starterCardPath = "src/jsons/StarterCard/StarterCard_";
        int numOfStarterCard = 6;

        ArrayList<String> identicalGoalJsons = new ArrayList<>();
        String identicalGoalPath = "src/jsons/Goal/IdenticalGoal/IdenticalGoal_";
        int numOfIdenticalGoal = 7;

        ArrayList<String> LGoalJsons = new ArrayList<>();
        String LGoalPath = "src/jsons/Goal/LGoal/LGoal_";
        int numOfLGoal = 4;

        ArrayList<String> stairGoalJsons = new ArrayList<>();
        String stairGoalPath = "src/jsons/Goal/StairGoal/StairGoal_";
        int numOfStairGoal = 4;

        fillList(resourceCardJsons, numOfResourceCard, resourceCardPath);
        fillList(goldCardJsons, numOfGoldCard, goldCardPath);
        fillList(goldCardAnglesJsons, numOfGoldCardAngles, goldCardAnglesPath);
        fillList(goldCardToolJsons, numOfGoldCardTool, goldCardToolPath);
        fillList(starterCardJsons, numOfStarterCard, starterCardPath);
        fillList(identicalGoalJsons, numOfIdenticalGoal, identicalGoalPath);
        fillList(LGoalJsons, numOfLGoal, LGoalPath);
        fillList(stairGoalJsons, numOfStairGoal, stairGoalPath);

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

    private static void fillList(ArrayList<String> jsonsList,int numOfJson,String path) throws IOException {
        for (int i = 1; i <=numOfJson; i++) {
            jsonsList.add(Files.readString(Path.of(path+i+".json")));
        }
    }


}
