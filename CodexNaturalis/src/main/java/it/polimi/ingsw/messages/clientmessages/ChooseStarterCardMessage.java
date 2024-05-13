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

    /**
     * constructor of ChooseStarterCardMessage
     * @param isFront
     * @param userId
     */
    public ChooseStarterCardMessage(boolean isFront, UUID userId){
        this.isFront=isFront;
        this.userId=userId;
    }

    /**
     * Overriding, gets the controller from the ClientHandler and calls
     * chooseStarterCardSide on the controller
     * @param clientHandler, the object that called processMessage
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().chooseStarterCardSide(isFront,userId);
        } catch (InvalidUserId e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
