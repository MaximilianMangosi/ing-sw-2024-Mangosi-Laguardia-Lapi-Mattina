package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.UnacceptableNumberOfPlayersException;

public class InitState extends GameState{

    InitState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }
    public void BootGame(int numOfPlayers, String playerName) throws UnacceptableNumberOfPlayersException, PlayerNameNotUniqueException {

    }

    //TODO BootGame Giorgio
    //TODO StartGame
    //todo ChooseGoal(UserId,Goal) Ric
    //todo PlaySarterCardSide(boolean,UserId) Ric

}
