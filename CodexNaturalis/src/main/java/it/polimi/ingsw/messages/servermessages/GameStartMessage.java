package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStartMessage extends ServerMessage{
    private Goal[] publicGoals;
    private List<Card> visibleCards;
    private String currentPlayer;
    private List<String> globalChat;
    private Map<String,String> playerToColor;


    public GameStartMessage(Goal[] publicGoals,List<Card> visibleCards ,String currentPlayer,List<String> chat,Map<String,String> playerToColor) {
        this.publicGoals = publicGoals;
        this.visibleCards=visibleCards;
        this.currentPlayer=currentPlayer;
        this.globalChat=chat;
        this.playerToColor = playerToColor;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setGameStarted(true);
        view.getGameData().setPublicGoals(publicGoals);
        view.getGameData().setVisibleCards(visibleCards);
        view.getGameData().setCurrentPlayer(currentPlayer);
        view.getGameData().setChatData(globalChat);
        view.getGameData().setPlayerToColor(playerToColor);
    }
}
