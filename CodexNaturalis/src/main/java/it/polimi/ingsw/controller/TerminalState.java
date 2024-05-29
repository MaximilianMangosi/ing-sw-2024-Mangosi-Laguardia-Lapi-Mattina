package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.UUID;

public class TerminalState extends GameState{
    TerminalState(Game game, GameManager gameManager, HashMap<UUID,Player> userIds) {
        super( gameManager);
        this.game=game;
        this.userIDs=userIds;
    }

    /**
     * @author Giuseppe Laguardia
     * @return null, there is no nextState after TerminalState
     */
    @Override
    protected GameState nextState() {
        return null;
    }

    /**
     * In TerminalState the game is ended (i.e. there is a winner ) so return true
     * @return true
     */
    @Override
    public boolean isGameEnded() {
        return true;
    }

    /**
     * @author Giuseppe Laguardia
     * @return if the game is started
     */
    @Override
    public boolean isGameStarted() {
        return true;
    }

    @Override
    public void addToGlobalChat(String message) throws IllegalOperationException {
        throw new IllegalOperationException("write message");
    }

    @Override
    public void addMessage(String name, String message, UUID userID) throws IllegalOperationException {
        throw new IllegalOperationException("write message");
    }
}
