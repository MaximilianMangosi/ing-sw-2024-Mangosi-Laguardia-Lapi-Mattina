package it.polimi.ingsw.model.gamelogic.exceptions;

public class NoGameExistsException extends Exception{
    public NoGameExistsException(){
        super("There are currently 0 games hosted, let's make one!");
    }
}
