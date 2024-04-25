package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;

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
    private List<Goal> publicGoals;
    private Map<UUID,Goal[]> playersGoalOptions;
    private Map<UUID,Goal> privateGoals;
    private List<Card> visibleCards;



    public View (Controller controller){
        this.controller=controller;
    }



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

    /**
     * updates PlaeyrsPoints, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updatePlayersPoints(){
        playersPoints=controller.getPlayersPoints();
    }

    /**
     * @author Giorgio Mattina
     * @return map nickname-points for each player
     */
    public Map<String, Integer> getPlayersPoints(){
        return playersPoints;
    }
    /**
     * updates NumOfResourceCards, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updateNumOfResourceCards(){
        numOfResourceCards=controller.getNumOfResourceCards();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of resource cards in the deck
     */
    public int getNumOfResourceCards(){
        return numOfResourceCards;
    }
    /**
     * updates NumOfGoldCards, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updateNumOfGoldCards(){
        numOfGoldCards=controller.getNumOfGoldCards();
    }

    /**
     * @author Giorgio Mattina
     * @return number of gold cards in the GoldCardDeck
     */
    public int getNumOfGoldCards(){
        return numOfGoldCards;
    }
    /**
     * updates playersHands, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updatePlayersHands(){
        playersHands=controller.getPlayersHands();
    }

    /**
     * @author Giorgio Mattina,Maximilian Mangosi
     * @return Map of playeId-list of card in player's hand
     */
    public Map<UUID, List<Card>> getPlayersHands(){
        return  playersHands;
    }
    /**
     * updates PlayersField, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updatePlayersField(){
        playersField=controller.getPlayersField();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map playerId-playerField ( cards on the table with relative position)
     */
    public Map<UUID,Map<Coordinates,Card>> getPlayersField(){
        return playersField;
    }
    /**
     * updates PlayersList, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updatePlayersList(){
        playersList=controller.getPlayersList();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of players in the game
     */
    public List<String> getPlayersList(){
        return playersList;
    }
    /**
     * updates currentPlayer, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updateCurrentPlayer(){
        currentPlayer=controller.getCurrentPlayer();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return the current player's name
     */
    public String getCurrentPlayer(){
        return currentPlayer;
    }
    /**
     * updates PlayesLegalPositions, calling the controller
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updatePlayersLegalPosition(){
        playersLegalPositions=controller.getPlayersLegalPositions();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map of list of available positions with relative player ID
     */
    public Map<UUID,List<Coordinates>> getPlayersLegalPositions(){
        return playersLegalPositions;
    }

    /**
     * updates public goals
     * @author Giorgio Mattina
     */
    public void updatePublicGoals(){
        publicGoals=controller.getPublicGoals();
    }

    /**
     * @author Giorgio Mattina
     * @return list of public Goals
     */
    public List<Goal> getPublicGoals(){
        return publicGoals;
    }

    /**
     * updates all the player's initial goal options
     * @author Giorgio Mattina
     */
    public void updatePlayersGoalOptions(){
        playersGoalOptions=controller.getGoalOptions();
    }
    /**
     * @author Giorgio Mattina
     * @return all player's initial goal options
     */
    public Map<UUID, Goal[]> getPlayersGoalOptions(){
        return playersGoalOptions;
    }

    /**
     * updates the map player-goal
     * @author Giorgio Mattina
     */
    public void updatePrivateGoals(){
        privateGoals=controller.getPrivateGoals();
    }

    /**
     * @author Giorgio Mattina
     * @return map of player-private goal from view
     */
    public Map<UUID,Goal> getPrivateGoals(){
        return privateGoals;
    }

    /**
     * updates the list of visibleCards
     * @author Giorgio Mattina, Maximilian Mangosi
     */
    public void updateVisibleCards(){
        visibleCards=controller.getVisibleCards();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of visible cards from view
     */
    public List<Card> getVisibleCards(){
        return visibleCards;
    }
    public void updateAll(){
        updatePlayersPoints();
        updateNumOfResourceCards();
        updateNumOfGoldCards();
        updatePlayersHands();
        updatePlayersField();
        updatePlayersList();
        updateCurrentPlayer();
        updatePlayersLegalPosition();
        updatePublicGoals();
        updatePlayersGoalOptions();
        updatePrivateGoals();
        updateVisibleCards();
    }
    //lista di giocatori
    //tabellone
    //currentPlayer
    //mazzi
    //Mappa di mappe per la mano
    //Vincitore




}
