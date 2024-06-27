package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;

import java.rmi.RemoteException;

/**
 * thread updates the TUI and waits 15 seconds
 */
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
                synchronized (this) {
                    wait(15000);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }catch(InterruptedException ignore){}
        }
    }
}
