package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

public enum Reign implements Resource {
    @SerializedName("a")
    ANIMAL("\u001B[36m"),
    @SerializedName("m")
    MUSHROOM("\u001B[31m"),
    @SerializedName("b")
    BUG("\u001B[35m"),
    @SerializedName("pl")
    PLANTS("\u001B[32m"),
    @SerializedName("e")
    EMPTY("\u001B[37m"),
    ;
    Reign(String s){
        this.color=s;
    }
    private String color;
    public String getColor (){
        return color;
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