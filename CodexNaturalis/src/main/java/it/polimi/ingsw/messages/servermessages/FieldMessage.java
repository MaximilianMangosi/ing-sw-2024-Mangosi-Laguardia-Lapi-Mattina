package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Giuseppe Laguardia
 * sends the field
 */
public class FieldMessage extends ServerMessage{
    Map<Coordinates, Card> newField;
    String player;
    List<Coordinates> newFieldBuilderHelper;


    public FieldMessage(Map<Coordinates, Card> newField, List<Coordinates> newFieldBuilderHelper, String player) {
        this.newField = new HashMap<>(newField);
        this.newFieldBuilderHelper = new ArrayList<>(newFieldBuilderHelper);
        this.player=player;
    }


    @Override
    public void processMessage(ViewSocket view){
        GameData gd = view.getGameData();
        gd.setPlayerField(player,newField);
        gd.setFieldBuilderHelper(player,newFieldBuilderHelper);

    }
}
