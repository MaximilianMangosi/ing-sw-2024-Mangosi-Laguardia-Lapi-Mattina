package it.polimi.ingsw.model.gamelogic.exceptions;
/**
 * the game id is invalid
 * @author Giuseppe Laguardia
 */
public class InvalidGameID extends Exception{
    public InvalidGameID(){
        super("This game doesn't exist");
    }
}
