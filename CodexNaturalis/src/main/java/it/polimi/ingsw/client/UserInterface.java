package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public abstract class UserInterface{
    protected  View view;
    protected UUID myID;
    protected String myName;

    public List<Card> getHand() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPlayerHand(myID);
        return view.showPlayerHand();

    }
    public StarterCard getStarterCard() throws RemoteException {
        if(view.isRMI())
            return view.getStarterCard(myID);
        return view.getStarterCard();
    }
    public Goal[] getGoalOptions() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPlayerGoalOptions(myID);
        return view.showPlayerGoalOptions();
    }

    public Goal getPrivateGoal() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPrivateGoal(myID);
        return view.showPrivateGoal();
    }
    public  List<String> getPrivateChat(String user) throws RemoteException {
        if(view.isRMI())
            return view.getPrivateChat(user,myID);
        return view.getPrivateChat(user);
    }


    public void setView(View view) {
        this.view=view;
    }

}
