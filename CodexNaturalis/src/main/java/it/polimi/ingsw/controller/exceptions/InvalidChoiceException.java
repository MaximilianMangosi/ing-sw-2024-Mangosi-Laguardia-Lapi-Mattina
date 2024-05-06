package it.polimi.ingsw.controller.exceptions;

public class InvalidChoiceException extends Exception {
    public InvalidChoiceException(){
        super("Invalid choice: choose between ");
    }
}
