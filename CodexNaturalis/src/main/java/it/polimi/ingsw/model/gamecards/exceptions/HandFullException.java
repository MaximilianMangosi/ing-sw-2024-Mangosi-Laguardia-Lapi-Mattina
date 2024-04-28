package it.polimi.ingsw.model.gamecards.exceptions;

public class HandFullException extends Exception{
    public HandFullException(){super("Your hand is full, you cannot draw unless you play a card first");}
}
