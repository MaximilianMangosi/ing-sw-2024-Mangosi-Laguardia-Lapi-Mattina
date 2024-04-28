package it.polimi.ingsw.model.gamelogic.exceptions;

public class PlayerNameNotUniqueException extends Exception{
    public PlayerNameNotUniqueException() {
        super("This username is already taken, please choose another one");
    }
}
