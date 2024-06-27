package it.polimi.ingsw.model.gamelogic.exceptions;
/**
 * there are no available games
 * @author Giorgio Mattina
 */
public class NoGameExistsException extends Exception{
    public NoGameExistsException(){
        super("There are currently 0 games hosted, let's make one!");
    }
}
