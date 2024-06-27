package it.polimi.ingsw.model.gamelogic.exceptions;

/**
 * the player name is already taken
 * @author Giuseppe Laguardia
 */
public class PlayerNameNotUniqueException extends Exception{
    public PlayerNameNotUniqueException() {
        super("This username is already taken, please choose another one");
    }
}
