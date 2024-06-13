package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.servermessages.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class RMIToSocketDispatcher extends Thread{
    private final Controller controller;
    private final ViewUpdater viewUpdater;

    public RMIToSocketDispatcher(Controller controller, ViewUpdater viewUpdater) {
        this.controller = controller;
        this.viewUpdater = viewUpdater;
    }
    /**
     * Retrieves a message from Remote view and send to the Socket clients
     * @author Giuseppe Laguardia
     */
    @Override
    public void run() {
        while (true) {
            try {
                ServerMessage newMsg=controller.retrieveMessage();
                if(newMsg == null)// if newMsg is null than the player are using the same connection
                    break;
                if(newMsg instanceof GameStartMessage) {
                    for (UUID id : viewUpdater.getClients().keySet()) {
                        viewUpdater.sendTo( new HandMessage(controller.getPlayersHands().get(id)),id);
                        viewUpdater.sendTo( new GoalOptionsMessage(controller.getGoalOptions().get(id)),id);
                        viewUpdater.sendTo( new StarterCardMessage(controller.getPlayersStarterCards().get(id)),id);
                    }
                    viewUpdater.sendAll(newMsg);
                } else if (newMsg instanceof UpdateChatMessage) {
                    UUID receiver = ((UpdateChatMessage) newMsg).getReceiver();
                    if(viewUpdater.getClients().containsKey(receiver))
                        viewUpdater.sendTo(newMsg,receiver);
                } else
                    viewUpdater.sendAll(newMsg);

            } catch (IOException e) {
                System.out.println("RMIToSocketDispatcher stopped");
            }
        }
    }
}
