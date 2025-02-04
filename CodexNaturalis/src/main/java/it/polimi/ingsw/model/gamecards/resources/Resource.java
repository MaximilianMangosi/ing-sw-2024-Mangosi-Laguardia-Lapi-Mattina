package it.polimi.ingsw.model.gamecards.resources;

import java.io.Serializable;
/**
 * resource characteristics of the card
 * @author Giuseppe Laguardia
 */
public interface Resource extends Serializable {
    public Resource getResource();

    public String getColorBG();
    public String getColorFG();
    public boolean isEmpty();
    public String  getSymbol();

}

