package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ViewInterface extends Remote {
    public Map<String, Integer> getPlayersPoints() throws RemoteException;
    public int getNumOfResourceCards() throws RemoteException;
    public int getNumOfGoldCards()  throws RemoteException;

    public List<Card> showPlayerHand(UUID uid) throws RemoteException, InvalidUserId;

    public Map<String,Map<Coordinates,Card>> getPlayersField() throws RemoteException;

    public List<String> getPlayersList() throws RemoteException;

    public String getCurrentPlayer() throws RemoteException;

    public List<Coordinates> showPlayersLegalPositions(UUID uid) throws RemoteException, InvalidUserId;

    public List<Goal> getPublicGoals() throws RemoteException;

    public Goal[] showPlayerGoalOptions(UUID uid) throws RemoteException, InvalidUserId;

    public Goal showPrivateGoal(UUID uid) throws RemoteException, InvalidUserId;

    public List<Card> getVisibleCards() throws RemoteException;

}
