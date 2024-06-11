package it.polimi.ingsw.model.gamelogic.exceptions;

public class InvalidGameID extends Exception{
    public InvalidGameID(){
        super("This game doesn't exist");
    }
}
