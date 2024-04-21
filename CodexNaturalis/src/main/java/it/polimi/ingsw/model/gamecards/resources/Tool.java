package it.polimi.ingsw.model.gamecards.resources;

import com.google.gson.annotations.SerializedName;

public enum Tool implements Resource {
    @SerializedName("f")
    FEATHER,
    @SerializedName("s")
    SCROLL,
    @SerializedName("p")
    PHIAL,
    @SerializedName("e")
    EMPTY,
    ;

    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this == Tool.EMPTY;
    }
}
