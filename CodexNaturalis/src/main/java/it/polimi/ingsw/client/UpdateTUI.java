package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class UpdateTUI extends Thread{
    final OutStreamWriter writer;
    TextUserInterface tui;
    UpdateTUI(OutStreamWriter w,TextUserInterface t){
        writer=w;
        tui=t;
    }
    @Override
    public void run(){
        while (true){
            try {
                tui.updateIdleUI();
                synchronized (writer){
                    writer.clearScreen();
                    writer.print(tui.getIdleUI());
                }
                 sleep(30000);

            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            catch ( InvalidUserId e){
                System.out.println(e.getMessage());
            }
        }
    }
}
