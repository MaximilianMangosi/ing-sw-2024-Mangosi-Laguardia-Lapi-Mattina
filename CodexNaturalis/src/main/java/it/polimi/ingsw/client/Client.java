package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {
    private final String[] commands = {"start-game", "play-card", "choose-goal","choose-starter-card-side", "draw-card-from-deck", "draw-card-visible", "disconnect" };
    private UUID myUid;

    /**
     * @author Riccardo Lapi, Giuseppe Laguardia
     * main function of the Client, connect to the server and listen to users commands
     * @param args
     */
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

            Socket server;
            try{
                server = new Socket("192.168.0.1", 2323);
            }catch (IOException e){
                System.out.println("Server not reachable\n");
            }


            Registry registry = LocateRegistry.getRegistry( 1099);
            ViewInterface view = (ViewInterface) registry.lookup("ViewRMI");

            Scanner s=new Scanner(System.in);
            //System.out.println("\033c");
            System.out.println("Welcome to Codex Naturalis ! \n Press Any Key To Start \n");

            System.out.println("\n\n");
            s.nextLine();
            System.out.println("Lets' start! Type 'start-game' to start a game\n");

            TextUserInterface tui= new TextUserInterface(view);


            while (true) {
                    try {
                        tui.execCmd(s.nextLine().toLowerCase(Locale.ROOT));

                    } catch (InvalidUserId | InvalidGoalException |
                             IllegalOperationException | HandNotFullException |
                             IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException |
                             InvalidCardException | HandFullException | InvalidChoiceException | DeckEmptyException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }

    }



}
