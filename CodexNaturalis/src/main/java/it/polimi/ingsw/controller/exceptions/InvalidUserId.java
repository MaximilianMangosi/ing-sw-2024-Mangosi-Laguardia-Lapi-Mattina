package it.polimi.ingsw.controller.exceptions;

public class InvalidUserId extends Exception{
    public InvalidUserId(){ super("The server kicked you from the game, due to disconnection or to game over");}
}
