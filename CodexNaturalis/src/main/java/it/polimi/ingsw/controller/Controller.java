package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.*;

public class Controller {
    GameState currentState;
    View view;
    /**
     * constructor of Controller, creates a new GameState
     * @author Giorgio Mattina
     * @param gameManager
     */
    public Controller( GameManager gameManager){
        currentState=new LobbyState(gameManager);
        view = new View(this);
    }


    /**
     * Makes the transition to nextState
     * @author Giuseppe Laguardia
     */
    private synchronized void changeState(){
        currentState=currentState.nextState();
    }
    public synchronized void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        currentState.playCardFront(selectedCard,position,userId);
        //TODO update view
        view.updatePlayersHands();
        view.updatePlayersPoints();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return Map of each player's score
     */
    public Map<String, Integer> getPlayersPoints(){
        Map<String, Integer> scoreboard = new HashMap<>();
        List<Player> playersList = currentState.game.getPlayers();
        for (int i = 0; i< currentState.game.getNumOfPlayers();i++){
           scoreboard.put(playersList.get(i).getName(),playersList.get(i).getPoints());
        }
        return scoreboard;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of resource cards in the ResourceCardsDeck
     */
    public int getNumOfResourceCards(){
        return currentState.game.getResourceCardDeck().size();
    }
    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return number of gold cards in the ResourceCardsDeck
     */
    public int getNumOfGoldCards(){
        return currentState.game.getGoldCardDeck().size();
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return map of UUID,List of player's hand
     */
    public Map<UUID,List<Card>> getPlayersHands(){
        //the method will return hands
        Map<UUID,List<Card>> hands = new HashMap<>();
        //save the keyset of all the player's UUIDs
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id : set){
            hands.put(id,currentState.getPlayerFromUid(id).getHand());
        }
        return hands;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return a map of all player's fields (cards on the table)
     */
    public Map<UUID,Map<Coordinates,Card>> getPlayersField(){
        Map<UUID,Map<Coordinates,Card>> fields = new HashMap<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id: set){
            fields.put(id,currentState.getPlayerFromUid(id).getField());
        }
        return fields;
    }

    /**
     * @author Giorgio Mattina, Maximilian Mangosi
     * @return list of the player's names
     */
    public List<String> getPlayersList(){
        List<String> names = new ArrayList<>();
        Set<UUID> set=currentState.userIDs.keySet();
        for (UUID id :set){
            names.add(currentState.getPlayerFromUid(id).getName());
        }
        return names;
    }

    /**
     * @author Giorgio Mattina
     * @return the name of the current player
     */
    public String getCurrentPlayer(){
        return currentState.game.getCurrentPlayer().getName();
    }

    /**
     * @author Giorgio Mattina, Maximilan Mangosi
     * @return all player's legal positions
     */
    public Map<UUID,List<Coordinates>> getPlayersLegalPositions(){
        Map<UUID,List<Coordinates>> legal = new HashMap<>();
        Set <UUID> set = currentState.userIDs.keySet();
        for (UUID id :set){
            legal.put(id,currentState.getPlayerFromUid(id).getAvailablePositions());
        }
        return legal;
    }


    //turn->final dopo drawCard check (areBothEmpty or hasSomeone20points) and  current player is first of the list
    //pepo,giorgio,max,ric
    //final->Terminal compute public goal and getWinner
    //currentState=new FinalTurn(game,gameManager);
}
