package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

public enum Reign implements Resource {
    @SerializedName("a")
    ANIMAL,
    @SerializedName("m")
    MUSHROOM,
    @SerializedName("b")
    BUG,
    @SerializedName("pl")
    PLANTS,
    @SerializedName("e")
    EMPTY,
    ;

    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this==Reign.EMPTY;
    }
}