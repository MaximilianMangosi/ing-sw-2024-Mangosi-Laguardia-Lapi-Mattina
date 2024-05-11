package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.view.ViewSocket;

import java.util.HashMap;
import java.util.List;

public class FieldMessage extends ServerMessage{
    HashMap <Coordinates, Card> newField;
    String player;
    List<Coordinates> newFieldBuilderHelper;
    List<Coordinates> newAvailablePositions;

    public FieldMessage(HashMap<Coordinates, Card> newField, List<Coordinates> newAvailablePositions, List<Coordinates> newFieldBuilderHelper) {
        this.newField = newField;
        this.newAvailablePositions = newAvailablePositions;
        this.newFieldBuilderHelper = newFieldBuilderHelper;
    }

    public FieldMessage(HashMap<Coordinates, Card> newField, List<Coordinates> newFieldBuilderHelper) {
        this.newField = newField;
        this.newFieldBuilderHelper = newFieldBuilderHelper;
    }

    @Override
    public void processMessage(ViewSocket view){
        GameData gd = view.getGameData();
        gd.setPlayerField(player,newField);
        gd.setFieldBuilderHelper(player,newFieldBuilderHelper);
        if(newAvailablePositions!=null)
            gd.setLegalPositions(newAvailablePositions);

    }
}
