package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewRMIInterface;
import it.polimi.ingsw.view.ViewSocket;

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
        View view;
        Scanner s=new Scanner(System.in);
        int connectionChoice= 0;
        while (connectionChoice!=1 && connectionChoice!=2 ) {
            System.out.println("Choose how to connect to Server.\n1)Socket\n2)RMI");
            connectionChoice = s.nextInt();
            s.nextLine();
        }
        try{
            if(connectionChoice==1) {
                Socket server;
                server = new Socket("192.168.0.1", 2323);
                view=new ViewSocket(server.getOutputStream(),server.getInputStream());
            }else {

                Registry registry = LocateRegistry.getRegistry(1099);
                view = (ViewRMIInterface) registry.lookup("ViewRMI");
            }

            System.out.println("\033c");
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
                    } catch (ClassNotFoundException | IOException e) {
                        System.out.println("Connection error");
                    }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }catch (IOException e){
            System.out.println("Server not reachable");
        }
    }

}

