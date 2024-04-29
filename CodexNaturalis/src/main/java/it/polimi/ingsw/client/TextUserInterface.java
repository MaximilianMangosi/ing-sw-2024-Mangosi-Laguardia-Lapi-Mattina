package it.polimi.ingsw.client;

import it.polimi.ingsw.view.ViewInterface;

import java.rmi.RemoteException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

public class TextUserInterface {
    private ViewInterface view;

    /**
     * constructor of TextUserInterface class
     * @author Giuseppe Laguardia
     * @param view
     */
    public TextUserInterface(ViewInterface view) {
        this.view = view;
    }

    private StringBuilder idleUI=new StringBuilder();

    public void updateIdleUI() throws RemoteException {
        Map<String,Integer> scoreboard=view.getPlayersPoints();
        List<String> players=view.getPlayersList();
        String currentPlayer= view.getCurrentPlayer();
        String winner=view.getWinner();
        if (winner==null){
            if (players!=null){
                idleUI.append("Players :");
                for (String player: players){
                    idleUI.append(player);
                    if(!player.equals(players.getLast()))
                        idleUI.append(", ");
                }
                idleUI.append("\n\n");
            }
            if(currentPlayer!=null){
                idleUI.append(currentPlayer);
                idleUI.append(" is playing.\n");
            }

        }else{
            idleUI.append(winner);
            idleUI.append("WINS!!!\n\n");
        }
        if(scoreboard!=null){
            for (Map.Entry<String,Integer> entry: sortedScoreboard(scoreboard)){
                idleUI.append(entry.getKey());
                idleUI.append(" :");
                idleUI.append(entry.getValue());
                idleUI.append("\n");
            }
            idleUI.append("\n\n");
        }


    }
    public String getIdleUI(){
        return idleUI.toString();
    }

    private List<Map.Entry<String, Integer>> sortedScoreboard(Map<String, Integer> scoreboard) {
        return scoreboard.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
    }
}
