package it.polimi.ingsw.controller.exceptions;

public class HandNotFullException extends Exception {
    public HandNotFullException(){super("You've already played a card this turn, draw a card to conclude your turn");}
}
