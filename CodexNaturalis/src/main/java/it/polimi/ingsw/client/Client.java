package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {
    private final String[] commands = {"start-game", "play-card", "choose-goal","choose-starter-card-side", "draw-card-from-deck", "draw-card-visible", "disconnect" };
    private UUID myUid;
    public static void main(String[] args) {
        Client client=new Client();
        /*
        * startGame
        * playCard
        * chooseGoal
        * chooseStarterCardSide
        * drawCardFromDeck
        * drawCardVisible
        * disconnect
        * */

        try{
            Registry registry = LocateRegistry.getRegistry();
            ViewInterface view = (ViewInterface) registry.lookup("ViewRMI");

            Scanner s=new Scanner(System.in);
            System.out.println("Welcome to Codex, press any key to start");
            s.nextLine();

            OutStreamWriter outWriter=new OutStreamWriter();
            TextUserInterface tui= new TextUserInterface(view);
            UpdateTUI updaterTUI=new UpdateTUI(outWriter,tui);
            updaterTUI.start();

            while (true) {
                    try {
                        client.execCmd(s.nextLine().toLowerCase(Locale.ROOT),view);
                    } catch (UnacceptableNumOfPlayersException | InvalidUserId | InvalidGoalException |
                             PlayerNameNotUniqueException | IllegalOperationException | HandNotFullException |
                             IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException | InvalidCardException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }

    }
    private void execCmd(String cmd, ViewInterface view) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException, RemoteException, IllegalOperationException, InvalidUserId, InvalidGoalException, HandNotFullException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException {
        Scanner s=new Scanner(System.in);
        switch (cmd){
            case "start-game":
                System.out.println("Insert username");
                String username = s.nextLine();
                System.out.println("Insert numbers of players");
                int numPlayers = s.nextInt();
                myUid = view.BootGame(numPlayers, username);
                break;
            case "choose-goal":
                System.out.println("Here are your goals, choose one (1,2)");
                Goal[] myGoals = view.showPlayerGoalOptions(myUid);
                System.out.println(myGoals[0].getUIDescription());
                System.out.println(myGoals[1].getUIDescription());
                String myGoal = s.nextLine();
                Goal goal = Objects.equals(myGoal, "1") ? myGoals[0] : myGoals[2];
                view.chooseGoal(myUid, goal);
                break;
            case "play-card":
                System.out.println("Which card do you want to play? (1,2,3)");
                int chosenCardI = s.nextInt();
                Card chosenCard = view.showPlayerHand(myUid).get(chosenCardI - 1);
                System.out.println("Which side? (f for front any for back)");
                boolean isChosenFront = s.nextLine().equals("f");
                System.out.println("Where do you want to place the selected card [" + chosenCardI + "]? (int)");
                int chosenPositionI = s.nextInt();
                List<Coordinates> availableCoordinates = view.showPlayersLegalPositions(myUid);
                Coordinates chosenPosition = availableCoordinates.get(chosenPositionI);

                if(isChosenFront) view.playCardFront(chosenCard,chosenPosition, myUid );
                else view.playCardBack(chosenCard, chosenPosition, myUid);
                break;

        }
    }
    private static void clearScreen () {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }catch (IOException | InterruptedException ignored) {}
    }
}
