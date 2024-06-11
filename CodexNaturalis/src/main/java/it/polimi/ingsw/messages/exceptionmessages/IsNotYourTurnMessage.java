package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
*   ExceptionMessage sent by the server when PlayerNameNotUniqueException occurs
 */
public class IsNotYourTurnMessage extends ExceptionMessage{

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException, IsNotYourTurnException {
        throw  new IsNotYourTurnException();
    }
}
