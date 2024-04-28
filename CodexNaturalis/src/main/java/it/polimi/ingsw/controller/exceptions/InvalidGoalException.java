package it.polimi.ingsw.controller.exceptions;

public class InvalidGoalException extends Exception {
    public InvalidGoalException(){ super("The Goal selected isn't in your Goal options, try show-goal-options");}

}
