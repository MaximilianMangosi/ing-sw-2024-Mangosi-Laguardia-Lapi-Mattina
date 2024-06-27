package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends the chosen goal
 */
public class ChosenGoalMessage extends ServerMessage{
    private Goal chosenGoal;

    public ChosenGoalMessage(Goal g){
        this.chosenGoal=g;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setPrivateGoal(chosenGoal);
    }
}
