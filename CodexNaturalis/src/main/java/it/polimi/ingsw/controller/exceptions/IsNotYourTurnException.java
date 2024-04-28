package it.polimi.ingsw.controller.exceptions;

public class IsNotYourTurnException extends Exception{
    public IsNotYourTurnException() {
        super("It's not your turn, please wait");
    }
}
