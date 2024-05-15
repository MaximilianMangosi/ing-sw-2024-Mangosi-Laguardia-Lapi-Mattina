package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;

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
                synchronized (this){
                    wait(30000);
                    System.out.println("I'm awake");
                    tui.updateIdleUI();
                    synchronized (writer){
                        writer.clearScreen();
                        writer.print(tui.getIdleUI());
                    }

                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }catch(InterruptedException ignore){}
            catch ( InvalidUserId e){
                System.out.println(e.getMessage());
            }
        }
    }
}
