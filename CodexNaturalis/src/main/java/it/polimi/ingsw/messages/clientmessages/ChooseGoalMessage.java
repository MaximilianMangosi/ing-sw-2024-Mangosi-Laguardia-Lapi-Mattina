package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidGoalException;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.InvalidGoalMessage;
import it.polimi.ingsw.messages.exceptionmessages.InvalidUserIdMessage;
import it.polimi.ingsw.messages.servermessages.ChosenGoalMessage;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.UUID;

public class ChooseGoalMessage extends ClientMessage{
    private UUID userId;
    private Goal goal;

    public ChooseGoalMessage(UUID userId, Goal goal) {
        this.userId = userId;
        this.goal = goal;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        try{
            clientHandler.getController().ChooseGoal(userId,goal);
            clientHandler.answerClient(new SuccessMessage());
            clientHandler.answerClient(new ChosenGoalMessage(goal));
        } catch (InvalidGoalException e) {
            clientHandler.answerClient(new InvalidGoalMessage());
        } catch (InvalidUserId e) {
            clientHandler.answerClient(new InvalidUserIdMessage());
        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        }
    }
}
