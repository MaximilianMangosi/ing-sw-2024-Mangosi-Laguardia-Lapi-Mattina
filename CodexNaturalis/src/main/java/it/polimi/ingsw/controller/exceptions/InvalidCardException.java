package it.polimi.ingsw.controller.exceptions;

public class InvalidCardException extends Exception{
    public InvalidCardException(){
        super("The Card selected isn't in your hand, you can't play it");
    }
}
