package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.view.ViewSocket;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Giuseppe Laguardia
 * send messahe that the game has started
 */
public class GameStartMessage extends ServerMessage{
    private Goal[] publicGoals;
    private List<Card> visibleCards;
    private Reign topGolds;
    private Reign topResources;
    private String currentPlayer;
    private List<String> globalChat;
    private Map<String,String> playerToColor;


    public GameStartMessage(Controller c) {
        publicGoals = c.getPublicGoals();
        visibleCards=c.getVisibleCards();
        currentPlayer=c.getCurrentPlayer();
        globalChat=c.getGlobalChat();
        playerToColor = c.getPlayerToColor();
        try {
            topGolds=c.getTopOfGoldCardDeck();
            topResources=c.getTopOfResourceCardDeck();
        } catch (IllegalOperationException ignore) {}
    }

    @Override
    public void processMessage(ViewSocket view) {
        GameData gameData = view.getGameData();
        gameData.setGameStarted(true);
        gameData.setPublicGoals(publicGoals);
        gameData.setVisibleCards(visibleCards);
        gameData.setCurrentPlayer(currentPlayer);
        gameData.setChatData(globalChat);
        gameData.setPlayerToColor(playerToColor);
        gameData.setTopOfResourcesDeck(topResources);
        gameData.setTopOfGoldsDeck(topGolds);

    }
}
