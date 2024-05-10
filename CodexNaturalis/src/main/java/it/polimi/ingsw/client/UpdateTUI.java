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
                tui.updateIdleUI();
                synchronized (writer){
                    writer.clearScreen();
                    writer.print(tui.getIdleUI());
                }
                 sleep(30000);

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }catch(InterruptedException ignore){

            }
            catch ( InvalidUserId e){
                System.out.println(e.getMessage());
            }
        }
    }
}
