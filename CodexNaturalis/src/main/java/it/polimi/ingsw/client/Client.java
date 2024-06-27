package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
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
    private UUID myUid;

    /**
     * @author Riccardo Lapi, Giuseppe Laguardia
     * main function of the Client, connect to the server and listen to users commands
     * @param args
     */
    public static void main(String[] args) {

        String serverAddress=args[0];
        View view;
        Scanner s=new Scanner(System.in);
        TextUserInterface tui= new TextUserInterface();
        System.out.println("\033c");
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
        tui.printLogo();
        System.out.print("\n\n");
        System.out.println("Press Any Key To Start");
        System.out.print("\n\n");
        s.nextLine();
        try{
            GameData gameData= new GameData();
            if(connectionChoice==1) {
                Socket server;
                server = new Socket( serverAddress,2323);;
                view=new ViewSocket(server,gameData);
                tui.setServerAddress(serverAddress);
                tui.setView(view);
                tui.startGame(false);

            }else {
                Registry registry = LocateRegistry.getRegistry(serverAddress,1099);
                ViewRMIContainerInterface viewContainer = (ViewRMIContainerInterface) registry.lookup("ViewRMI");
                tui.setViewContainer(viewContainer);
                tui.startGame(true);
            }

            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    tui.execCmd(s.nextLine().toLowerCase(Locale.ROOT));
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Connection error");
                }catch (IllegalOperationException | InvalidUserId | HandFullException | IsNotYourTurnException |
                        HandNotFullException | InvalidCardException | InvalidGoalException |  IllegalPositionException |
                        DeckEmptyException e) {
                    System.out.println(e.getMessage());
                    System.out.println("press enter to continue");
                    s.nextLine();
                    tui.printIdleUI();
                }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Server not reachable");
        }
    }

}

