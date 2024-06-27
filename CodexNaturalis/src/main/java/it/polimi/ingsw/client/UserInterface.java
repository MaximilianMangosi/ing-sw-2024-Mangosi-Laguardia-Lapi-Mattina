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

    public List<Card> getHand() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayerHand(myID);
        return view.showPlayerHand();

    }
    public StarterCard getStarterCard() throws RemoteException {
        if(view.isRMI())
            return view.getStarterCard(myID);
        return view.getStarterCard();
    }
    public Goal[] getGoalOptions() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayerGoalOptions(myID);
        return view.showPlayerGoalOptions();
    }

    public Goal getPrivateGoal() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPrivateGoal(myID);
        return view.showPrivateGoal();
    }
    public  List<String> getPrivateChat(String user) throws RemoteException {
        if(view.isRMI())
            return view.getPrivateChat(user,myID);
        return view.getPrivateChat(user);
    }
    public List<Coordinates> getPlayersLegalPositions() throws RemoteException, InvalidUserId, IllegalOperationException {
        if(view.isRMI())
            return view.showPlayersLegalPositions(myID);
        return view.showPlayersLegalPositions();
    }


    public void setView(View view) {
        this.view=view;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress=serverAddress;
    }

}
