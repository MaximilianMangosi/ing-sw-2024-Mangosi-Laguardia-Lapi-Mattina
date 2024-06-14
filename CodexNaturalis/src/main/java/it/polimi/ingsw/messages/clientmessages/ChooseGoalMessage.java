package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
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

    /**
     * constructor of ChooseGoalMessage
     * @author Giorgio Mattina
     * @param userId of the player who is choosing the goal
     * @param goal the chosen goal
     */
    public ChooseGoalMessage(UUID userId, Goal goal) {
        this.userId = userId;
        this.goal = goal;
    }

    /**
     * calls the controller and answers the client with SuccessMessage and ChosenGoalMessage
     * @author Giorgio Mattina
     * @param clientHandler the father thread that can send a response to the client
     * @throws IOException
     */
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        try{
            Controller c = clientHandler.getController();
            c.chooseGoal(userId,goal);
            clientHandler.answerClient(new SuccessMessage());
            clientHandler.sendTo(userId,new ChosenGoalMessage(goal));
        } catch (InvalidGoalException e) {
            clientHandler.answerClient(new InvalidGoalMessage());
        } catch (InvalidUserId e) {
            clientHandler.answerClient(new InvalidUserIdMessage());
        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        }
    }
}
