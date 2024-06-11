package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import it.polimi.ingsw.view.ViewSocket;

/**
 * Abstract class for messages sent by the server when an exception occurs
 */
public abstract class ExceptionMessage extends ServerMessage {
}