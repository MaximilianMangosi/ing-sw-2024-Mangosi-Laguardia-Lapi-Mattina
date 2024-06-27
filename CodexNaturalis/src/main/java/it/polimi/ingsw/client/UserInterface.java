package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewRMIContainer;
import it.polimi.ingsw.view.ViewRMIContainerInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

/**
 * The abstract class of TUI and GUI
 */
public abstract class UserInterface{
    protected  View view;
    protected ViewRMIContainerInterface viewContainer;
    protected UUID myID;
    protected UUID gameID;
    protected String myName;
    protected boolean isPlaying=false;
    protected String serverAddress;

    public void setViewContainer(ViewRMIContainerInterface viewContainer) {
        this.viewContainer = viewContainer;
    }
    /**
     * function to get the user hand
     * @author Giuseppe Laguardia
     * @return the list of Card in player's hand
     * @throws RemoteException when a connection error occurs
     * @throws InvalidUserId when the user's ID is not associated to a player who joined a game
     */
    public List<Card> getHand() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayerHand(myID);
        return view.showPlayerHand();

    }


    /**
     * returns the starter card
     * @author Giuseppe Laguardia
     * @return the user's starterCard
     * @throws RemoteException when a connection error occurs
     */
    public StarterCard getStarterCard() throws RemoteException {
        if(view.isRMI())
            return view.getStarterCard(myID);
        return view.getStarterCard();
    }
    /**
     * returns the goal options
     * @author Giuseppe Laguardia
     * @return the goal options
     * @throws RemoteException when a connection error occurs
     * @throws InvalidUserId when a connection error occurs
     */
    public Goal[] getGoalOptions() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayerGoalOptions(myID);
        return view.showPlayerGoalOptions();
    }
    /**
     * returns the private goals
     * @author Giuseppe Laguardia
     * @return the private goal
     * @throws RemoteException when a connection errors occurs
     * @throws InvalidUserId when the user's ID is not associated to a player who joined a game
     */

    public Goal getPrivateGoal() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPrivateGoal(myID);
        return view.showPrivateGoal();
    }

    /**
     * returns private chat with another user
     * @author Giuseppe Laguardia
     * @param user the other user who shares the chat
     * @return the private chat with the given user
     * @throws RemoteException when a connection errors occurs
     */
    public  List<String> getPrivateChat(String user) throws RemoteException {
        if(view.isRMI())
            return view.getPrivateChat(user,myID);
        return view.getPrivateChat(user);
    }
    /**
     * returns the positions on witch the player can play the card
     * @author Giuseppe Laguardia
     * @return the list of positions where is possible to place a card on the field
     * @throws RemoteException when a connection errors occurs
     * @throws InvalidUserId when the player's ID is not associ
     */
    public List<Coordinates> getPlayersLegalPositions() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayersLegalPositions(myID);
        return view.showPlayersLegalPositions();
    }

    /**
     * sets the view
     * @author Giuseppe Laguardia
     * @param view the view to set as attribute
     */
    public void setView(View view) {
        this.view=view;
    }

    /**
     * sets the server's ip address
     * @author Giuseppe Laguardia
     * @param serverAddress the serverIP address to be set as attribute
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress=serverAddress;
    }

}
