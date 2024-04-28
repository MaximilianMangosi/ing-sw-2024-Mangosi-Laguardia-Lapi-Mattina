package it.polimi.ingsw.controller.exceptions;

public class IllegalOperationException extends Exception {
    public IllegalOperationException(String operation){ super(operation+" isn't allowed in this phase\n");}
}
