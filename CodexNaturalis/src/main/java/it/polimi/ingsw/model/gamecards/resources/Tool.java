package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.TUIAsciiArtist;
/**
 * tool characteristics of the card
 * @author Giuseppe Laguardia
 */
public enum Tool implements Resource {
    @SerializedName("f")
    FEATHER("F"),
    @SerializedName("s")
    SCROLL("S"),
    @SerializedName("p")
    PHIAL("P"),
    @SerializedName("e")
    EMPTY("E"),
    ;

    private String symbol;
    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public String getColorBG() {
        return "\u001B[103m";
    }

    @Override
    public String getColorFG() {
        return "\u001B[33m";
    }

    @Override
    public boolean isEmpty() {
        return this == Tool.EMPTY;
    }
    public String getColor(){
        return TUIAsciiArtist.YELLOW;
    }
    @Override
    public String getSymbol() {
        return symbol;
    }
     Tool(String s){
        this.symbol=s;
    }
}
