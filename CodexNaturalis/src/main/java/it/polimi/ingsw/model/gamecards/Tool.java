package it.polimi.ingsw.model.gamecards;

import com.google.gson.annotations.SerializedName;

public enum Tool implements Resource{
    @SerializedName("f")
    FEATHER,
    @SerializedName("s")
    SCROLL,
    @SerializedName("p")
    PHIAL,
    @SerializedName("e")
    EMPTY,
    @SerializedName("n")
    NONEXISTENT,
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
