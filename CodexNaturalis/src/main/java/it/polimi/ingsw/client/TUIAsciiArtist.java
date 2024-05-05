package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.goals.IdenticalGoal;
import it.polimi.ingsw.model.gamecards.goals.LGoal;
import it.polimi.ingsw.model.gamecards.goals.StairGoal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TUIAsciiArtist implements CardDisplay {
    String[][] matrix = new String[5][46];
    private String[][] asciiField ;
    private StringBuilder strbuilder;
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[43m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";



    /**
     * uses object StringBuilder to build a string in ASCII art representing parameter Card, then prints the string on System.Out
     * @author Giorgio Mattina
     * @param card
     */
    @Override
    public void show(Card card) {
        strbuilder = new StringBuilder();
            String reignColor = card.getReign().getColor();
            char point = card.getPoints()>0? Character.forDigit(card.getPoints(),10):'-';
            String angleNW = card.getResource("NW") !=null? card.getResource("NW").getSymbol() : "×";
            String angleNE = card.getResource("NE") !=null? card.getResource("NE").getSymbol() : "×";
            String angleSW = card.getResource("SW") !=null? card.getResource("SW").getSymbol() : "×";
            String angleSE = card.getResource("SE") !=null? card.getResource("SE").getSymbol() : "×";

            if(card.getRequirements().isEmpty()){
                strbuilder.append(String.format(reignColor+"%s ----%c---- %s\n" +
                                                  reignColor+ "| ■■■■■■■■■ |\n" +
                                                 reignColor+  "| ■■■■■■■■■ |\n" +
                                                 reignColor+   "| ■■■■■■■■■ |\n" +
                                                 reignColor +"%s --------- %s\n"+RESET,angleNW,point, angleNE,angleSW,angleSE));

            }else{
                strbuilder.append(String.format(YELLOW+"%s ----%c---- %s\n|"+
                                 reignColor+" ■■■■■■■■■ "+YELLOW+"|\n|"+
                                  reignColor+" ■■■■■■■■■ "+YELLOW+"|\n|"+
                                  reignColor+" ■■■■■■■■■ "+YELLOW+"|\n"+
                                       "%s --------- %s\n"+RESET,angleNW,point, angleNE,angleSW,angleSE));
                int sum=0;
                List<Resource> req=new ArrayList<>();
                for(Resource s : card.getRequirements().keySet().stream().sorted().toList()){
                   for(int j = 0; j<card.getRequirements().get(s);j++)
                       req.add(s);
                }
                int bottomCenter=96;
                //regardless of the size
                strbuilder.replace(bottomCenter,bottomCenter+1,req.removeFirst().getSymbol());
                strbuilder.replace(bottomCenter+2,bottomCenter+3,req.removeFirst().getSymbol());
                if(req.size()==2){
                    strbuilder.replace(bottomCenter-1,bottomCenter,req.removeFirst().getSymbol());
                    strbuilder.replace(bottomCenter+3,bottomCenter+4,req.removeFirst().getSymbol());
                }else if (req.size()==3){
                    strbuilder.replace(bottomCenter-1,bottomCenter,req.removeFirst().getSymbol());
                    strbuilder.replace(bottomCenter+3,bottomCenter+4,req.removeFirst().getSymbol());
                    strbuilder.replace(bottomCenter+1,bottomCenter+2,req.removeFirst().getSymbol());

                }else{
                    strbuilder.replace(bottomCenter+1,bottomCenter+2,req.removeFirst().getSymbol());
                }
            }
            int center=30;
            if (card.getPoints()==2){
                strbuilder.replace(center,center+1,"A");
            }
            if(card.getTool()!=null){
                strbuilder.replace(center,center+1,card.getTool().getSymbol());
            }

        System.out.println(strbuilder.toString());
            //TODO invece di stampare aggiungi alla matrice
    }
    public void show(Goal goal){
        int k=0;
        for( k=0;k<42;k++){
           if(matrix[0][k]==null){
               break;
           }
        }
        if(goal instanceof IdenticalGoal){
          if(goal.getNumOfResource()==3){
              //identicalTool
              String symbol=((IdenticalGoal) goal).getResource().getSymbol();
              buildGoalCardStructure(k,goal,((IdenticalGoal) goal).getResource().getColor());
              matrix[2][k+6]=symbol;
              matrix[3][k+4]=symbol;
              matrix[3][k+8]=symbol;
          }else{
              String symbol=((IdenticalGoal) goal).getResource().getSymbol();
              buildGoalCardStructure(k,goal,((IdenticalGoal) goal).getResource().getColor());
              matrix[3][k+4]=symbol;
              matrix[3][k+8]=symbol;
          }


        }else if(goal instanceof LGoal){
           //Lgoal
            buildGoalCardStructure(k,goal,((LGoal) goal).getSecondaryReign().getColor());
            if(goal.getPrimaryReign() == Reign.PLANTS){
                matrix[2][k+6] = ((LGoal) goal).getSecondaryReign().getColor() + "█" + RESET;
                matrix[3][k+7] = goal.getPrimaryReign().getColor() + "■" + RESET;
            }else if(goal.getPrimaryReign() == Reign.BUG){
                matrix[2][k+6] = ((LGoal) goal).getSecondaryReign().getColor() + "█" + RESET;
                matrix[3][k+5] = goal.getPrimaryReign().getColor() + "■" + RESET;
            }else if(goal.getPrimaryReign() == Reign.MUSHROOM){
                matrix[3][k+6] = ((LGoal) goal).getSecondaryReign().getColor() + "█" + RESET;
                matrix[2][k+7] = goal.getPrimaryReign().getColor() + "■" + RESET;
            }else{
                matrix[3][k+6] = ((LGoal) goal).getSecondaryReign().getColor() + "█" + RESET;
                matrix[2][k+5] = goal.getPrimaryReign().getColor() + "■" + RESET;
            }
        }else if(goal instanceof StairGoal){
            //stair
            buildGoalCardStructure(k,goal,YELLOW);
            if(((StairGoal) goal).isToLowerRight()){
                matrix[1][k+4] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
                matrix[2][k+6] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
                matrix[3][k+8] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
            }else{
                matrix[1][k+8] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
                matrix[2][k+6] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
                matrix[3][k+4] = ((StairGoal) goal).getReign().getColor() + "■" + RESET;
            }
        }else{
           //distinct
            buildGoalCardStructure(k,goal,"\u001B[103m");
            matrix[3][k+4]="F";
            matrix[3][k+6]="P";
            matrix[3][k+8]="S";
        }

    }

    /**
     * builds the background of a goalCard in the matrix
     * @author Giorgio Mattina,Riccardo Lapi
     * @param columnStart inclusive index where the methods start to build the matrix
     * @param goal
     */
    private void buildGoalCardStructure (int columnStart, Goal goal,String bgColor){
        for (int i=0;i<5;i++){
            matrix[i][columnStart+14]=" "+RESET;
            for (int j=columnStart;j<columnStart+14;j++){
                if(i==0){
                    if(j==columnStart){
                        matrix[i][j]=YELLOW+"╔";
                    } else if (j==columnStart+13) {
                        matrix[i][j]="╗"+RESET;
                    }else{
                        matrix[i][j]="═";
                    }
                }
                if(i==1){
                    if(j==columnStart || j==columnStart+13){
                        matrix[i][j]=YELLOW+ "║"+RESET;
                    }else if(j==columnStart+5){
                        matrix[i][j]= String.valueOf(goal.getPoints());
                    }else{
                        matrix[i][j]=bgColor+" ";
                    }
                }
                if(i==2 || i==3){
                    if(j==columnStart || j==columnStart+13){
                        matrix[i][j]=YELLOW+ "║"+RESET;
                    }else{
                        matrix[i][j]=bgColor+" ";
                    }
                }
                if(i==4) {
                    if (j == columnStart) {
                        matrix[i][j] = YELLOW + "╚";
                    } else if (j == columnStart + 13) {
                        matrix[i][j] = "╝" + RESET;
                    } else {
                        matrix[i][j] = "═";
                    }
                }
            }
        }
    }
    public String[][] getAsciiField(){
        return asciiField;
    }
    public void show (HashMap<Coordinates, Card> field, List<Coordinates> fieldBuildingHelper){
        int i,j;
        asciiField = new String[240][880];
        for (Coordinates coordinate : fieldBuildingHelper){
            Card card = field.get(coordinate);
            String color = card.getReign()!=null? card.getReign().getColor():YELLOW;
            int x = coordinate.x;
            int y = coordinate.y;
            j = 440 + 10*(x);
            i = 120 - 2*(y);
            for (int k = i-1; k<=i+1; k++){
                for (int h = j-6 ; h<=j+4; h++){ //h is off-center for Ansi escape sequence reasons
                    asciiField[k][h] = " "+color;
                }
            }
            asciiField[i][j] = color+fieldBuildingHelper.indexOf(coordinate);
        }

    }


    public String[][] getMatrix() {
        return matrix;
    }
}
