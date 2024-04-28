package it.polimi.ingsw.model.gamelogic.exceptions;

public class UnacceptableNumOfPlayersException extends Exception{
    public UnacceptableNumOfPlayersException(){ super("You can't play with this number of players, choose a number in range [2:4]");}
}
