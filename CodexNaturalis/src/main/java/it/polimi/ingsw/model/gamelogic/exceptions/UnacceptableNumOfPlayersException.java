package it.polimi.ingsw.model.gamelogic.exceptions;
/**
 * the number of players is too high or too low
 * @author Giuseppe Laguardia
 */
public class UnacceptableNumOfPlayersException extends Exception{
    public UnacceptableNumOfPlayersException(){ super("You can't play with this number of players, choose a number in range [2:4]");}
}
