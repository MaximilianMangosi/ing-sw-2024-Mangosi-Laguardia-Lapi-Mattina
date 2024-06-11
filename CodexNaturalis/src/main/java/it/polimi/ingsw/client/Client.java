package it.polimi.ingsw.client;

import it.polimi.ingsw.GUI.HelloApplication;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import it.polimi.ingsw.view.*;

import java.io.IOException;
import java.net.InetAddress;
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
        View view;
        Scanner s=new Scanner(System.in);
        TextUserInterface tui= new TextUserInterface();
        int connectionChoice= 0;
        while (connectionChoice!=1 && connectionChoice!=2 ) {
            System.out.println("Choose how to connect to Server.\n1)Socket\n2)RMI");
            try {
                connectionChoice = s.nextInt();
                s.nextLine();
            } catch (InputMismatchException e) {
                s.nextLine();
                System.out.println("\033c");
                System.out.println("Insert only numbers please");

            }
        }
        System.out.println("\033c");
        System.out.println("Welcome to Codex Naturalis! \n Press Any Key To Start");
        System.out.print("\n\n");
        s.nextLine();
        System.out.println("Lets' start! Type 'start-game' to start a game");
        try{
            GameData gameData= new GameData();
            if(connectionChoice==1) {
                Socket server;
                server = new Socket( InetAddress.getLocalHost(),2323);
                view=new ViewSocket(server.getOutputStream(),server.getInputStream(),gameData);
                tui.setView(view);
                tui.startGameSocket();
            }else {
                Registry registry = LocateRegistry.getRegistry(1099);
                ViewRMIContainerInterface viewContainer = (ViewRMIContainerInterface) registry.lookup("ViewRMI");
                tui.startGameRMI(viewContainer);
            }

            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    tui.execCmd(s.nextLine().toLowerCase(Locale.ROOT));
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Connection error");
                }catch (IllegalOperationException | InvalidUserId | HandFullException | IsNotYourTurnException |
                        HandNotFullException | InvalidCardException | InvalidGoalException | InvalidChoiceException |
                        NoGameExistsException | RequirementsNotMetException | UnacceptableNumOfPlayersException |
                        OnlyOneGameException | PlayerNameNotUniqueException | IllegalPositionException |
                        DeckEmptyException | InvalidGameID e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Server not reachable");
        }
    }

}

