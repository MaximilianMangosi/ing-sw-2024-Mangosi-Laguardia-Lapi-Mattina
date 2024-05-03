package it.polimi.ingsw.client;

public class OutStreamWriter {
    public void print(String str ){
        System.out.println(str);
    }
    public void print(String[][] matrix){
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
    public void clearScreen() {
        System.out.println("\033c");
    }
}
