package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TextUserInterface {
    private ViewInterface view;

    private UUID myID;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    private String myName;
    public UUID getMyID() {
        return myID;
    }

    public void setMyID(UUID myID) {
        this.myID = myID;
    }
    public TextUserInterface(ViewInterface view) {
        this.view = view;
    }

    private StringBuilder idleUI;

    public synchronized void updateIdleUI() throws RemoteException, InvalidUserId {
        idleUI=new StringBuilder();
        String winner=view.getWinner();
        List<String> players=view.getPlayersList();
        idleUI.append("Players: ");
        for (String player : players) {
            idleUI.append(player);
            if (!player.equals(players.getLast()))
                idleUI.append(", ");
        }
        idleUI.append("\n");
        if (view.isGameEnded()){
            idleUI.append(winner);
            idleUI.append("WINS!!!\n\n");

        }else if(view.isGameStarted()){

            String currentPlayer= view.getCurrentPlayer();
            Map<String,Integer> scoreboard=view.getPlayersPoints();

            idleUI.append("\n\n");
            if (currentPlayer!=null) {
                idleUI.append(currentPlayer);
                idleUI.append(" is playing.\n");
            }
            for (Map.Entry<String,Integer> entry: sortedScoreboard(scoreboard)){
                idleUI.append(entry.getKey());
                idleUI.append(": ");
                idleUI.append(entry.getValue());
                idleUI.append("\n");
            }
            idleUI.append("\n\n");
            if(view.showPrivateGoal(myID)==null){
                idleUI.append("To start the game you have to choose your private goal from your goal options, try choose-private-goal\n");
            }
            if(!didIPlayStarterCard()){
                idleUI.append("To start the game you have to choose the side of your starter card, try choose-starter-card\n");
            }
        } else{
            idleUI.append("Waiting for players...");
        }

        }

    private boolean didIPlayStarterCard() throws RemoteException {
        return view.getPlayersField().get(myName).containsValue(view.getStarterCard(myID));
    }


    private List<Map.Entry<String, Integer>> sortedScoreboard(Map<String, Integer> scoreboard) {
        return scoreboard.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
    }

    public String getIdleUI() {
        return idleUI.toString();
    }
}
