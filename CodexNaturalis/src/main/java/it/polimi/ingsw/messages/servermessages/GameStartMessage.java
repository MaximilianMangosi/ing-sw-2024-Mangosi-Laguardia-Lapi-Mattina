package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.ViewSocket;

public class GameStartMessage extends ServerMessage{
    private Goal[] publicGoals;
    private Goal[] goalOptions;
    private Goal privateGoal;
    private StarterCard starterCard;

    public GameStartMessage(Goal[] publicGoals, Goal[] goalOptions, Goal privateGoal, StarterCard starterCard) {
        this.publicGoals = publicGoals;
        this.goalOptions = goalOptions;
        this.privateGoal = privateGoal;
        this.starterCard = starterCard;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setGameStarted(true);
        view.getGameData().setPublicGoals(publicGoals);
        view.getGameData().setGoalOptions(goalOptions);
        view.getGameData().setPrivateGoal(privateGoal);
        view.getGameData().setStarterCard(starterCard);
    }
}
