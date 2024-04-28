package it.polimi.ingsw.controller.exceptions;

public class IllegalPositionException extends Exception{
    public IllegalPositionException(){
        super("You can't play a card here, please try show-available-position");
    }
}
