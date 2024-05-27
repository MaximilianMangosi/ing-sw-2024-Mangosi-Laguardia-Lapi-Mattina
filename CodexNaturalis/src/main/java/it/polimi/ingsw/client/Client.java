package it.polimi.ingsw.client;

import it.polimi.ingsw.GUI.HelloApplication;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewRMIInterface;
import it.polimi.ingsw.view.ViewSocket;

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
        int connectionChoice= 0;
        while (connectionChoice!=1 && connectionChoice!=2 ) {
            System.out.println("Choose how to connect to Server.\n1)Socket\n2)RMI");
            connectionChoice = s.nextInt();
            s.nextLine();
        }
        try{
            GameData gameData= new GameData();
            if(connectionChoice==1) {
                Socket server;
                server = new Socket( InetAddress.getLocalHost(),2323);
                view=new ViewSocket(server.getOutputStream(),server.getInputStream(),gameData);
            }else {
                Registry registry = LocateRegistry.getRegistry(1099);
                view = (ViewRMIInterface) registry.lookup("ViewRMI");
            }

            System.out.println("\033c");
            System.out.println("Welcome to Codex Naturalis! \n Press Any Key To Start \n");
            System.out.println("\n\n");
            s.nextLine();
            System.out.println("Lets' start! Type 'start-game' to start a game\n");

            TextUserInterface tui= new TextUserInterface(view);


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
                        DeckEmptyException e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }catch (IOException e){
            System.out.println("Server not reachable");
        }
    }

}

