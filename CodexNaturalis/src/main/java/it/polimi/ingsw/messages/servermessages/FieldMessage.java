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


    public FieldMessage(HashMap<Coordinates, Card> newField, List<Coordinates> newFieldBuilderHelper, String player) {
        this.newField = newField;
        this.newFieldBuilderHelper = newFieldBuilderHelper;
        this.player=player;
    }


    @Override
    public void processMessage(ViewSocket view){
        GameData gd = view.getGameData();
        gd.setPlayerField(player,newField);
        gd.setFieldBuilderHelper(player,newFieldBuilderHelper);


    }
}
