package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * @author Giuseppe Laguardia
 *  A couple of signed integer representing a position on a Cartesian plane
 */
public class Coordinates implements Serializable {

    public int x;
    public int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * Overrides Object.equals(), two Coordinates are equal if they have the same x and same y
     * @author Giuseppe Laguardia
     * @param other the Object to compare with
     * @return true if this equals other, otherwise returns false
     */
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof Coordinates temp)
            return this.x==temp.x && this.y==temp.y;
        return false;

    }
    /**
     * Overrides hashCode() for Coordinates, using Cantor's pairing function for signed value
     * @author Giuseppe Laguardia
     * @return the int value of the hash code
     */
    @Override
    public int hashCode(){
        int a=(x>=0 ? 2*x :(-2*x)-1);
        int b=(y>=0 ? 2*y :(-2*y)-1);
        return (int) (0.5*(a+b)*(a+b+1)+b);
    }
}
