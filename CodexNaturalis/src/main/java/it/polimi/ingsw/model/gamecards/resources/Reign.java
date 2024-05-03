package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum Reign implements Resource, Serializable {
    @SerializedName("a")
    ANIMAL("\u001B[46m","a"),
    @SerializedName("m")
    MUSHROOM("\u001B[41m","m"),
    @SerializedName("b")
    BUG("\u001B[45m","b"),
    @SerializedName("pl")
    PLANTS("\u001B[42m","p"),
    @SerializedName("e")
    EMPTY("\u001B[47m","■"),
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