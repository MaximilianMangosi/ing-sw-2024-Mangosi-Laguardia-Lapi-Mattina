package it.polimi.ingsw.model.gamelogic.exceptions;

public class OnlyOneGameException extends Exception{

    public OnlyOneGameException(){
        super("Actually there's a game");
    }

}
