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
