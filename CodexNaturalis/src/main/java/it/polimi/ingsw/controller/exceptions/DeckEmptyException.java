package it.polimi.ingsw.controller.exceptions;

public class DeckEmptyException extends Exception{

    public DeckEmptyException(){super("This deck is empty please select another one or draw one of the card visible on the table\n");}
}
