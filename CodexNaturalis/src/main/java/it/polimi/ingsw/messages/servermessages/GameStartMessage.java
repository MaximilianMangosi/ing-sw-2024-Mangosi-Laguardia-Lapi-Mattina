package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;

import java.rmi.RemoteException;
import java.util.List;

public class GameStartMessage extends ServerMessage{
    private Goal[] publicGoals;
    private List<Card> visibleCards;


    public GameStartMessage(Goal[] publicGoals,List<Card> visibleCards ) {
        this.publicGoals = publicGoals;
        this.visibleCards=visibleCards;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setGameStarted(true);
        view.getGameData().setPublicGoals(publicGoals);

        view.getGameData().setPrivateGoal(privateGoal);
        view.getGameData().setStarterCard(starterCard);
    }
}
