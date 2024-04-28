package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry();
            ViewInterface view = (ViewInterface) registry.lookup("ViewRMI");
            System.out.println("Connected to the server");

        }catch (Exception error){
            System.out.println(error.toString());
        }
    }
}
