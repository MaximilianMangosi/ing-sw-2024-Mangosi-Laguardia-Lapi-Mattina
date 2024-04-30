package it.polimi.ingsw.client;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.GoldCard;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TUIAsciiArtist implements CardDisplay {
    private StringBuilder strbuilder ;
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    @Override
    public void show(Card card) {
        strbuilder = new StringBuilder();
            String reignColor = card.getReign().getColor();
            char point = card.getPoints()>0? Character.forDigit(card.getPoints(),10):'#';
            String angleNW = card.getResource("NW") !=null? card.getResource("NW").getSymbol() : "#";
            String angleNE = card.getResource("NE") !=null? card.getResource("NE").getSymbol() : "#";
            String angleSW = card.getResource("SW") !=null? card.getResource("SW").getSymbol() : "#";
            String angleSE = card.getResource("SE") !=null? card.getResource("SE").getSymbol() : "#";

            if(card.getRequirements().isEmpty()){
                strbuilder.append(String.format(reignColor+"%s ####%c#### %s\n" +
                                                  reignColor+ "# ######### #\n" +
                                                 reignColor+  "# ######### #\n" +
                                                 reignColor+   "# ######### #\n" +
                                                 reignColor +"%s ######### %s\n"+RESET,angleNW,point, angleNE,angleSW,angleSE));

            }else{
                strbuilder.append(String.format(YELLOW+"%s ####%c#### %s\n#"+
                                 reignColor+" ######### "+YELLOW+"#\n#"+
                                  reignColor+" ######### "+YELLOW+"#\n#"+
                                  reignColor+" ######### "+YELLOW+"#\n"+
                                       "%s ######### %s\n"+RESET,angleNW,point, angleNE,angleSW,angleSE));
                int sum=0;
                List<Resource> req=new ArrayList<>();
                for(Resource s : card.getRequirements().keySet()){
                   for(int j = 0; j<card.getRequirements().get(s);j++)
                       req.add(s);
                }
                //regardless of the size
                strbuilder.replace(96,97,req.removeFirst().getSymbol());
                strbuilder.replace(98,99,req.removeFirst().getSymbol());
                if(req.size()==2){
                    strbuilder.replace(95,96,req.removeFirst().getSymbol());
                    strbuilder.replace(99,100,req.removeFirst().getSymbol());
                }else if (req.size()==3){
                    strbuilder.replace(95,96,req.removeFirst().getSymbol());
                    strbuilder.replace(99,100,req.removeFirst().getSymbol());
                    strbuilder.replace(97,98,req.removeFirst().getSymbol());

                }else{
                    strbuilder.replace(97,98,req.removeFirst().getSymbol());
                }
            }
            if (card.getPoints()==2){
                strbuilder.replace(30,31,"A");
            }
            if(card.getTool()!=null){
                strbuilder.replace(30,31,"T");
            }

        System.out.println(strbuilder.toString());
        System.out.println("\n");
    }
}
