package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * reign characteristics of the card
 * @author Giuseppe Laguardia
 */
public enum Reign implements Resource, Serializable {
    @SerializedName("a")
    ANIMAL("\u001B[104m","\u001B[34m","A"),
    @SerializedName("m")
    MUSHROOM("\u001B[101m","\u001B[31m","M"),
    @SerializedName("b")
    BUG("\u001B[105m","\u001B[35m","B"),
    @SerializedName("pl")
    PLANTS("\u001B[102m","\u001B[32m","P"),
    @SerializedName("e")
    EMPTY("\u001B[107m","\u001B[97m","■"),
    ;
    Reign(String BG,String FG,String symb){
        this.colorBG=BG;
        this.colorFG=FG;
        this.symbol=symb;
    }
    private String colorBG;
    private String colorFG;
    private String symbol;
    public String getColorBG (){
        return colorBG;
    }
    public String getColorFG(){
        return colorFG;
    }
    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this==Reign.EMPTY;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}