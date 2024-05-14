package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

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
