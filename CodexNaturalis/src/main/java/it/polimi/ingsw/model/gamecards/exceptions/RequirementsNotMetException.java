package it.polimi.ingsw.model.gamecards.exceptions;

public class RequirementsNotMetException extends Exception{
    public RequirementsNotMetException(){super("You don't meet the requirements needed to play this card, play another one or play this on back side");}
}