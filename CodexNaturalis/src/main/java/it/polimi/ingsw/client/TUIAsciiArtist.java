package it.polimi.ingsw.client;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.goals.IdenticalGoal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TUIAsciiArtist implements CardDisplay {
    String[][] matrix = new String[5][42];
    private StringBuilder strbuilder;
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    /**
     * uses object StringBuiler to build a string in ASCII art representing parameter Card, then prints the string on System.Out
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
           if( !matrix[0][k].equals(" ")){
               break;
           }
        }

        if(goal.getNumOfResource()==2){
           //identicalTool
          for(int i=0;i<5;i++){
              for(int j=k+1;j<k+14;j++){
                  matrix[i][j]=YELLOW;
              }
          }
        }else if(goal.getNumOfResource()==3){
            //Identical Reign
        }else if(goal.getPrimaryReign()!=null){
           //Lgoal
        }else if(goal.getPoints()==2){
            //stair
        }else{
           //distinct
        }


    }
}
