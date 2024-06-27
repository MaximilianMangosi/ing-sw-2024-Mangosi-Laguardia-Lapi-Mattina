package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends the goal options
 */
public class GoalOptionsMessage extends ServerMessage{
    private Goal[] goalOptions;

    public GoalOptionsMessage(Goal[] options){
        this.goalOptions=options;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setGoalOptions(goalOptions);
    }
}
