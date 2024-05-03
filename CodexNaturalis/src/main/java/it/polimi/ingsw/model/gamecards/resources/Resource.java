package it.polimi.ingsw.model.gamecards.resources;

import java.io.Serializable;

public interface Resource extends Serializable {
    public Resource getResource();

    public String getColor();
    public boolean isEmpty();
    public String  getSymbol();

}

