package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.server.ClientHandler;

import java.rmi.RemoteException;
import java.util.UUID;

public class ChooseStarterCardMessage extends ClientMessage{
    private boolean isFront;
    private UUID userId;

    @Override
    public void processMessage(Controller controller, ClientHandler clientHandler) {
        try {
            controller.chooseStarterCardSide(isFront,userId);
        } catch (InvalidUserId e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
