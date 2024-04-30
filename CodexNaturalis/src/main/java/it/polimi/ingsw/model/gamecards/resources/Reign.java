package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum Reign implements Resource, Serializable {
    @SerializedName("a")
    ANIMAL("\u001B[36m","a"),
    @SerializedName("m")
    MUSHROOM("\u001B[31m","m"),
    @SerializedName("b")
    BUG("\u001B[35m","b"),
    @SerializedName("pl")
    PLANTS("\u001B[32m","p"),
    @SerializedName("e")
    EMPTY("\u001B[37m","E"),
    ;
    Reign(String s,String symb){
        this.color=s;
        this.symbol=symb;
    }
    private String color;
    private String symbol;
    public String getColor (){
        return color;
    }
    public String getSymbol(){
        return symbol;
    }
    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this==Reign.EMPTY;
    }
}