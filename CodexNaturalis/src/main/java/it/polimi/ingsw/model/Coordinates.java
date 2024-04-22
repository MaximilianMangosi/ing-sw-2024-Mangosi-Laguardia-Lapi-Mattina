package it.polimi.ingsw.model;

public class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object other){
        if(other==this)
            return true;
        if(other instanceof Coordinates temp)
            return this.x==temp.x && this.y==temp.y;
        return false;

    }
    @Override
    public int hashCode(){
        return (int)( 0.5*(x+y)*(x+y+1)+y);
    }
}
