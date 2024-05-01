package it.polimi.ingsw.client;

public class OutStreamWriter {
    public void print(String str ){
        System.out.println(str);
    }
    public void clearScreen() {
        System.out.println("\033c");
    }
}
