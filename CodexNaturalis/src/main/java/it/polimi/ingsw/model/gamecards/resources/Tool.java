package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

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

    public String symbol;
    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this == Tool.EMPTY;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
     Tool(String s){
        this.symbol=s;
    }
}
