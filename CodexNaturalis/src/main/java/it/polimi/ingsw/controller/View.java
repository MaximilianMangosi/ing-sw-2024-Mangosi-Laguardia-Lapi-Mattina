package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;

import java.util.*;

public class View {
    private Controller controller;
    private Map<String, Integer > playersPoints;
    private String winner;
    private int numOfResourceCards;
    private int numOfGoldCards;
    private Map<UUID, List<Card>> playersHands;
    private Map<UUID,Map<Coordinates,Card>> playersField;
    private List<String> playersList;
    private String currentPlayer;
    private Map<UUID,List<Coordinates>> playersLegalPositions;

  /*  public void insertCommand (){
        Scanner scannerCommand = new Scanner(System.in);
        System.out.println("Insert an action:\n");
        command = scannerCommand.nextLine();


        switch (command){

            case "bootGame":


            case "playCardFront":
                System.out.println("insert a card ID:\n");
                String card = scannerCommand.nextLine();
                System.out.println("insert a :\n");

        }

        scannerCommand.close();
    }
    */
    public void updatePlayersPoints(){
        playersPoints=controller.getPlayersPoints();
    }
    public Map<String, Integer> getPlayersPoints(){
        return playersPoints;
    }
    public void updateNumOfResourceCards(){
        numOfResourceCards=controller.getNumOfResourceCards();
    }
    public int getNumOfResourceCards(){
        return numOfResourceCards;
    }
    public void updateNumOfGoldCards(){
        numOfGoldCards=controller.getNumOfGoldCards();
    }
    public int getNumOfGoldCards(){
        return numOfGoldCards;
    }
    public void updatePlayersHands(){
        playersHands=controller.getPlayersHands();
    }
    public Map<UUID, List<Card>> getPlayersHands(){
        return  playersHands;
    }
    public void updatePlayersField(){
        playersField=controller.getPlayersField();
    }
    public Map<UUID,Map<Coordinates,Card>> getPlayersField(){
        return playersField;
    }
    public void updatePlayersList(){
        playersList=controller.getPlayersList();
    }
    public List<String> getPlayersList(){
        return playersList;
    }
    public void updateCurrentPlayer(){
        currentPlayer=controller.getCurrentPlayer();
    }
    public String getCurrentPlayer(){
        return currentPlayer;
    }
    public void updatePlayersLegalPosition(){
        playersLegalPositions=controller.getPlayersLegalPositions();
    }
    public Map<UUID,List<Coordinates>> getPlayersLegalPositions(){
        return playersLegalPositions;
    }
    public void update (){

    }
    /*public void startGame (){

    }*/


    public View (Controller controller){
        this.controller=controller;
    }


    //lista di giocatori
    //tabellone
    //currentPlayer
    //mazzi
    //Mappa di mappe per la mano
    //Vincitore




}
