package it.polimi.ingsw.server;

import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.view.ViewRMIContainer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static void main(String[] argv) {
        //obtain IP address
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
        } catch (IOException e) {
            System.out.println("Connection error");
            return;
        }
        String serverAddress=socket.getLocalAddress().getHostAddress();
        System.out.println("The server IP: "+serverAddress);

        //GAME BOX SETUP
        GameBox gb = new GameBox();
        try {
            gameBoxSetup(gb);
        } catch (IOException | URISyntaxException | NullPointerException e) {
            System.out.println("Error occurred during json file's reading:\n " + e.getMessage());
            return;
        }

        // MAIN SERVER LOGIC
        try {
            // GameManager setup
            GameManager gameManager = new GameManager();
            gameManager.setGameBox(gb);
            System.out.println("GameBox ready");

            //View setup
            ViewRMIContainer viewRMIContainer = new ViewRMIContainer(gameManager);

            // export View
            System.setProperty("java.rmi.server.hostname", serverAddress);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ViewRMI", viewRMIContainer);
            System.out.println("Remote View has been correctly exported");

            ServerSocket commandSocket;

            try {
                commandSocket = new ServerSocket(2323);
            } catch (IOException e) {
                System.out.println("cannot open server commandSocket");
                return;
            }
            ConcurrentHashMap<UUID,ViewUpdater> viewUpdaterMap=new ConcurrentHashMap<>();
            new UpdateViewSocket(viewUpdaterMap).start();
            while (true){
                try {
                    Socket client = commandSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(client,viewRMIContainer,viewUpdaterMap);
                    Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                    thread.start();

                } catch (IOException e) {
                    System.out.println("connection dropped");
                }
            }
       } catch (RemoteException ex) {
            System.out.println("Connection error unable to export object:\n" + ex.getMessage());}
    }

    /**
     * sets up the game box
     * @author Giuseppe Laguardia
     * @param gb
     * @throws IOException
     * @throws URISyntaxException
     */
    private static void gameBoxSetup(GameBox gb) throws IOException ,URISyntaxException{
        ArrayList<String> resourceCardJsons = new ArrayList<>();
        String resourceCardPath = "/jsons/ResourceCard/ResourceCard_";
        int numOfResourceCard = 40;

        ArrayList<String> goldCardJsons = new ArrayList<>();
        String goldCardPath = "/jsons/GoldCard/GoldCard_";
        int numOfGoldCard = 16;

        ArrayList<String> goldCardAnglesJsons = new ArrayList<>();
        String goldCardAnglesPath = "/jsons/GoldCard/GoldCardAngles/GoldCardAngles_";
        int numOfGoldCardAngles = 12;

        ArrayList<String> goldCardToolJsons = new ArrayList<>();
        String goldCardToolPath = "/jsons/GoldCard/GoldCardTool/GoldCardTool_";
        int numOfGoldCardTool = 12;

        ArrayList<String> starterCardJsons = new ArrayList<>();
        String starterCardPath = "/jsons/StarterCard/StarterCard_";
        int numOfStarterCard = 6;

        ArrayList<String> identicalGoalJsons = new ArrayList<>();
        String identicalGoalPath = "/jsons/Goal/IdenticalGoal/IdenticalGoal_";
        int numOfIdenticalGoal = 7;

        ArrayList<String> LGoalJsons = new ArrayList<>();
        String LGoalPath = "/jsons/Goal/LGoal/LGoal_";
        int numOfLGoal = 4;

        ArrayList<String> stairGoalJsons = new ArrayList<>();
        String stairGoalPath = "/jsons/Goal/StairGoal/StairGoal_";
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

    /**
     * fills the json list
     * @author Giuseppe Laguardia
     * @param jsonsList
     * @param numOfJson
     * @param path
     * @throws IOException
     * @throws NullPointerException
     */
    private static void fillList(ArrayList<String> jsonsList, int numOfJson, String path) throws IOException, NullPointerException {
        for (int i = 1; i <=numOfJson; i++) {
            jsonsList.add(new String(Server.class.getResourceAsStream(path+i+".json").readAllBytes()));
        }
    }


}
