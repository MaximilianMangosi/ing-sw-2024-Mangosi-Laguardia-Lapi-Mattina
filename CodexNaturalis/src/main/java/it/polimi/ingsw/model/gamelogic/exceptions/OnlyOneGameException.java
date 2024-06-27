package it.polimi.ingsw.model.gamelogic.exceptions;
/**
 * there is only one game
 * @author Giorgio Mattina
 */
public class OnlyOneGameException extends Exception{

    public OnlyOneGameException(){
        super("Actually there's a game");
    }

}
