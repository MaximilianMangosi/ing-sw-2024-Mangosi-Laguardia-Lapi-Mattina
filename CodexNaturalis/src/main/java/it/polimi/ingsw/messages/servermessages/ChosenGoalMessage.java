package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;

public class ChosenGoalMessage extends ServerMessage{
    Goal privateGoal;

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setPrivateGoal(privateGoal);
    }
}
