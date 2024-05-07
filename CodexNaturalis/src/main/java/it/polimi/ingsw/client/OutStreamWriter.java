package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Coordinates;

import java.util.Comparator;
import java.util.List;

public class OutStreamWriter {
    public void print(String str ){
        System.out.println(str);
    }
    public void print(String[][] matrix, List<Coordinates> helper){
        //getMin (getMax) returns the center of the card express in Cartesian coordinates then I have to express it  in matrix index notation and shift by 2 or 3 to print the whole card
        int startRow= 120 - 2*getMinY(helper) - 10;
        int endRow= 120 - 2*getMaxY(helper) + 10;
        int startCol= 440 + 10*getMinX(helper) - 15;
        int endCol= 440 + 10*getMaxX(helper) + 15;

        for (int i = startRow; i<endRow; i++){
            for (int j = startCol; j<endCol; j++){
                if(matrix[i][j]!=null) {
                    System.out.print(matrix[i][j]);
                }
                else {
                    System.out.print(" \u001B[0m");
                }
            }
            System.out.println(" \u001B[0m");
        }
    }

    /**
     * prints starter card either only the front if isFront = true or back if isFront=false
     * @author Giorgio Mattina
     * @param matrix ASCII representation of StarterCard
     * @param isFront
     */
    public void print(String[][] matrix,boolean isFront){
        if(isFront){
            for(int i=0;i<5;i++){
                for (int j=0;j<14;j++){
                    if(matrix[i][j]!=null) {
                        System.out.print(matrix[i][j]);
                    }
                    else {
                        System.out.print(" \u001B[0m");
                    }


                }
                System.out.println(" \u001B[0m");
            }
        }else{
            for(int i=0;i<5;i++){
                for (int j=14;j<26;j++){
                    if(matrix[i][j]!=null) {
                        System.out.print(matrix[i][j]);
                    }
                    else {
                        System.out.print(" \u001B[0m");
                    }


                }
                System.out.println(" \u001B[0m");
            }
        }
    }

    private Integer getMaxX(List<Coordinates> helper) {
        return helper.stream().map(c -> c.x).max(Integer::compare).orElse(398);
    }

    private Integer getMinX(List<Coordinates> helper) {
        return helper.stream().map(c -> c.x).min(Integer::compare).orElse(1);
    }

    private Integer getMaxY(List<Coordinates> helper) {
        return helper.stream().map(c -> c.y).max(Integer::compare).orElse(238);
    }

    private Integer getMinY(List<Coordinates> helper) {
        return helper.stream().map(c -> c.y).min(Integer::compare).orElse(1);
    }

    public void clearScreen() {
        System.out.println("\033c");
    }

    public void print(String[][] matrix) {
        for (String[] row : matrix){
            for(String cell: row) {
                if(cell != null)
                    System.out.print(cell);
                else {
                    System.out.print(" ");
                }
            }
            System.out.println(" \u001B[0m");
        }

    }
}
