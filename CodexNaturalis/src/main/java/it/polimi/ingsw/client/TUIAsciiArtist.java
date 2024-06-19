package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.goals.IdenticalGoal;
import it.polimi.ingsw.model.gamecards.goals.LGoal;
import it.polimi.ingsw.model.gamecards.goals.StairGoal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamecards.resources.Resource;
import it.polimi.ingsw.model.gamecards.resources.Tool;

import java.util.*;
import java.util.List;

public class TUIAsciiArtist {
    private String[][] matrix = new String[5][46];
    private String[][] asciiField ;
    private  OutStreamWriter writer;
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[43m";
    public static final String WHITE = "\u001B[47m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String LIGHT_WHITE = "\u001B[97m";

    public TUIAsciiArtist(OutStreamWriter writer) {
        this.writer = writer;
    }

    /**
     * write on matrix the ASCII art representing parameter Card
     * @author Giorgio Mattina
     * @param card
     */

    public void show(Card card,boolean shouldPrint) {
        int startCol= 0;
        String angleNW = card.getResource("NW") !=null? card.getResource("NW").getSymbol() : "╔";
        String angleNE = card.getResource("NE") !=null? card.getResource("NE").getSymbol() : "╗";
        String angleSW = card.getResource("SW") !=null? card.getResource("SW").getSymbol() : "╚";
        String angleSE = card.getResource("SE") !=null? card.getResource("SE").getSymbol() : "╝";
        boolean printBack = shouldPrint && !card.isFront();
        if (!shouldPrint) {
            for (int i = 0; i < matrix[0].length ; i++) {
                if(matrix[0][i]==null){
                    startCol=i;
                    break;
                }
            }
        }else{
            matrix=new String[5][16];
        }
        if(printBack){
            angleNW="■";
            angleNE="■";
            angleSW="■";
            angleSE="■";
        }
        String reignColor = card.getReign().getColorBG();
        String point = card.getPoints()>0? String.valueOf(card.getPoints()):"═";

        HashMap<Reign, Integer> requirements = card.getRequirements();
        for(int i=1;i<14;i++){ //write first and last row
            matrix[0][startCol+i]="═";
            matrix[4][startCol+i]="═";
        }
        String color;
        if(requirements.isEmpty()) // ResourceCard
            color= LIGHT_WHITE +reignColor;
        else //GoldCard
            color= LIGHT_WHITE +YELLOW;
        matrix[0][startCol]=color+angleNW;
        matrix[0][startCol+14]=angleNE+RESET;

        for (int i=1;i<4;i++){
            matrix[i][startCol]=color+"║"+reignColor;
            for (int j = 1; j < 14; j++) {
                matrix[i][startCol+j]=" ";
            }
            matrix[i][startCol+14]=color+"║"+RESET;
        }
        //adding spaces between cards
        matrix[0][startCol+15]=" ";
        matrix[4][startCol]=color+angleSW;
        matrix[4][startCol+14]=angleSE+RESET;
        if (!printBack) {
            Tool tool = card.getTool();
            if(card.getPoints()==2) //GoldCardAngles
                matrix[1][startCol+7]="A";
            else if (tool !=null) { //GoldCardTool
                matrix[1][startCol+7]=tool.getSymbol();

            }
            List<Resource> requirementsList=new ArrayList<>();
            for(Resource s : requirements.keySet().stream().sorted().toList()){
               for(int j = 0; j< requirements.get(s); j++)
                   requirementsList.add(s);
            }
            switch (requirementsList.size()){
                case 3->{
                    int i=startCol+6;
                    for (Resource req:requirementsList){
                        matrix[3][i]=req.getSymbol();
                        i++;
                    }
                }
                case 4->{
                    int i=startCol+5;
                    for (Resource req:requirementsList){
                        if(i==startCol+7) {
                            matrix[3][i+1]=req.getSymbol();
                            i+=2;
                            continue;
                        }
                        matrix[3][i]=req.getSymbol();
                        i++;
                    }
                }
                case 5->{
                    int i=startCol+5;
                    for (Resource req:requirementsList){
                        matrix[3][i]=req.getSymbol();
                        i++;
                    }
                }
            }
        }
        if(card.getPoints()>0)
            matrix[0][startCol+7]=point;
        if(shouldPrint)
            writer.print(matrix);


    }


    public void show(List<Card> cards){
        if(cards.size()<=3){ //show Hand
            matrix=new String[5][48];
            for (Card card: cards){
                show(card,false);
            }
            writer.print(matrix);
        }
        else {
            matrix=new String[5][32];
            show(cards.getFirst(),false);
            show(cards.get(1),false);
            writer.print(matrix);
            writer.print("\n");
            matrix=new  String[5][32];
            show(cards.get(2),false);
            show(cards.get(3),false);
            writer.print(matrix);

        }
    }

    /**
     * @author Giuseppe Laguardia and Riccardo Lapi
     * print the goals of the player
     * @param goals array of the players goals
     */
    public void show(Goal[] goals){
        int k=0;
        matrix=new String[5][46];
        for (Goal goal:goals) {
            if (goal instanceof IdenticalGoal) {
                if (goal.getNumOfResource() == 3) {
                    //identicalTool
                    String symbol = BLACK + ((IdenticalGoal) goal).getResource().getSymbol() + RESET;
                    buildGoalCardStructure(k, goal, ((IdenticalGoal) goal).getResource().getColorBG());
                    matrix[2][k + 6] = symbol;
                    matrix[3][k + 4] = symbol;
                    matrix[3][k + 8] = symbol;
                } else {
                    String symbol = BLACK + ((IdenticalGoal) goal).getResource().getSymbol() + RESET;
                    buildGoalCardStructure(k, goal, ((IdenticalGoal) goal).getResource().getColorBG());
                    matrix[3][k + 4] = symbol;
                    matrix[3][k + 8] = symbol;
                }


            } else if (goal instanceof LGoal) {
                //Lgoal
                buildGoalCardStructure(k, goal, ((LGoal) goal).getSecondaryReign().getColorBG());
                if (goal.getPrimaryReign() == Reign.PLANTS) {
                    matrix[2][k + 6] = ((LGoal) goal).getSecondaryReign().getColorFG() + "█" + RESET;
                    matrix[3][k + 7] = goal.getPrimaryReign().getColorFG() + "■" + RESET;
                } else if (goal.getPrimaryReign() == Reign.BUG) {
                    matrix[2][k + 6] = ((LGoal) goal).getSecondaryReign().getColorFG() + "█" + RESET;
                    matrix[3][k + 5] = goal.getPrimaryReign().getColorFG() + "■" + RESET;
                } else if (goal.getPrimaryReign() == Reign.MUSHROOM) {
                    matrix[3][k + 6] = ((LGoal) goal).getSecondaryReign().getColorFG() + "█" + RESET;
                    matrix[2][k + 7] = goal.getPrimaryReign().getColorFG() + "■" + RESET;
                } else {
                    matrix[3][k + 6] = ((LGoal) goal).getSecondaryReign().getColorFG() + "█" + RESET;
                    matrix[2][k + 5] = goal.getPrimaryReign().getColorFG() + "■" + RESET;
                }
            } else if (goal instanceof StairGoal) {
                //stair
                buildGoalCardStructure(k, goal, ((StairGoal) goal).getReign().getColorBG());
                if (((StairGoal) goal).isToLowerRight()) {
                    matrix[1][k + 4] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                    matrix[2][k + 6] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                    matrix[3][k + 8] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                } else {
                    matrix[1][k + 8] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                    matrix[2][k + 6] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                    matrix[3][k + 4] = ((StairGoal) goal).getReign().getColorFG() + "■" + RESET;
                }
            } else {
                //distinct
                buildGoalCardStructure(k, goal, "\u001B[103m");
                matrix[3][k + 4] = BLACK + "F" + RESET;
                matrix[3][k + 6] = BLACK + "P" + RESET;
                matrix[3][k + 8] = BLACK + "S" + RESET;
            }
            k+=15;
        }
        writer.print(matrix);
    }

    /**
     * calls buildStarterCardStructure and replaces angle and central resources based on parameter starterCard in matrix
     * @author Giorgio Mattina
     * @param starterCard
     */
    public void show(StarterCard starterCard,boolean printBothSide){
            boolean printFront= starterCard.isFront();
            buildStarterCardStructure(printBothSide);
            if(printBothSide) {
                showFront(starterCard);
                showBack(starterCard);
                matrix[5][0] = "FRONT";
                matrix[5][14] = "BACK";
            } else if (printFront) {
                showFront(starterCard);
            }else {
                showBack(starterCard);
            }
        writer.print(matrix);
    }

    private void showBack(StarterCard starterCard) {
        matrix[0][14] = YELLOW + starterCard.getResourceBack("NW").getColorFG() + starterCard.getResourceBack("NW").getSymbol() + LIGHT_WHITE ;
        matrix[0][26] = YELLOW + starterCard.getResourceBack("NE").getColorFG() + starterCard.getResourceBack("NE").getSymbol() +RESET+ LIGHT_WHITE;
        matrix[4][14] = YELLOW + starterCard.getResourceBack("SW").getColorFG() + starterCard.getResourceBack("SW").getSymbol() + LIGHT_WHITE ;
        matrix[4][26] = YELLOW + starterCard.getResourceBack("SE").getColorFG() + starterCard.getResourceBack("SE").getSymbol() +RESET+ LIGHT_WHITE;
    }

    private void showFront(StarterCard starterCard) {
        List<Resource> centralResources = starterCard.getCentralResource();
        int numOfCentralResources = centralResources.size();

        Resource NW = starterCard.getResource("NW");
        Resource NE = starterCard.getResource("NE");
        Optional<Resource> SW = Optional.ofNullable(starterCard.getResource("SW"));
        Optional<Resource> SE = Optional.ofNullable(starterCard.getResource("SE"));

        String angleNW = YELLOW + NW.getColorFG() + NW.getSymbol() + LIGHT_WHITE + YELLOW;
        String angleNE = YELLOW + NE.getColorFG() + NE.getSymbol() + LIGHT_WHITE + YELLOW;
        String angleSW = SW.map(resource -> YELLOW + resource.getColorFG() + resource.getSymbol() + LIGHT_WHITE + YELLOW).orElse("╚");
        String angleSE = SE.map(resource -> YELLOW + resource.getColorFG() + resource.getSymbol() + LIGHT_WHITE + YELLOW).orElse("╝");

        if (numOfCentralResources == 1) {

            matrix[0][0] = angleNW;
            matrix[0][12] = angleNE;
            matrix[4][0] = angleSW;
            matrix[4][12] = angleSE;
            matrix[2][6] = centralResources.get(0).getColorFG() + centralResources.getFirst().getSymbol();

        } else if (numOfCentralResources == 2) {

            matrix[0][0] = angleNW;
            matrix[0][12] = angleNE;
            matrix[4][0] = angleSW;
            matrix[4][12] = angleSE;
            matrix[1][6] = centralResources.get(0).getColorFG() + centralResources.get(0).getSymbol();
            matrix[3][6] = centralResources.get(0).getColorFG() + centralResources.get(1).getSymbol();

        } else if (numOfCentralResources == 3) {

            matrix[0][0] = angleNW;
            matrix[0][12] = angleNE;
            matrix[1][6] = centralResources.get(0).getColorFG() + centralResources.get(0).getSymbol() + BLACK;
            matrix[2][6] = centralResources.get(1).getColorFG() + centralResources.get(1).getSymbol() + BLACK;
            matrix[3][6] = centralResources.get(2).getColorFG() + centralResources.get(2).getSymbol() + BLACK;
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
                    }else if(j==columnStart+6){
                        matrix[i][j]= BLACK+String.valueOf(goal.getPoints())+RESET;
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

    /**
     * Builds the background of the front and back of a starter card, which never changes and writes it in matrix
     * @author Giorgio Mattina
     */
    private void buildStarterCardStructure(boolean buildBothSide){
        int endCol;
        if(buildBothSide) {
            matrix=new String[6][27];
            endCol = 27;
        }
        else{
            matrix=new String[5][15];
            endCol=15;
        }
       
        int j=0;
        for (int i = 0; i<5;i++){
            if(i==0){//first row
                for ( j=0;j<endCol;j++){
                    if(j==0 || j==14){
                        matrix[i][j]=LIGHT_WHITE+YELLOW+"╔"+YELLOW;
                    }else if(j==12 || j==26){
                        matrix[i][j]="╗"+RESET;
                    }else if(j==13){
                        matrix[i][j]=RESET+" ";
                    }else{
                        matrix[i][j]="═";
                    }
                }

            }else if (i==1 || i==2 || i==3){//middle rows
                for ( j=0;j<endCol;j++){
                    if(j==0 || j==14 || j==12 || j==26){
                        matrix[i][j]=RESET+LIGHT_WHITE+YELLOW+"║"+RESET;

                    }else if( j==13){
                        matrix[i][j]=RESET+" ";
                    }else{

                        matrix[i][j]="\u001B[103m"+" ";
                    }
                }
                matrix[i][26] += RESET;
            }else {//the last row
                for ( j=0;j<endCol;j++){
                    if(j==0 || j==14){
                        matrix[i][j]=YELLOW+LIGHT_WHITE+"╚"+YELLOW;
                    }else if(j==12 || j==26){
                        matrix[i][j]="╝"+RESET;
                    }else if(j==13){
                        matrix[i][j]=RESET+" ";
                    }else{
                        matrix[i][j]= "═";
                    }
                }

            }
        }

    }

    /**
     * @author Giorgio Mattina
     * @return the player field
     */
    public String[][] getAsciiField(){
        return asciiField;
    }

    /**
     * @author Giuseppe Laguardia, Giorgio Mattina, Riccardo Lapi, Maximilian Mangosi
     * print the user field
     * @param field the user field
     * @param fieldBuildingHelper List of the coordinates in order of insertion
     */
    public void show (Map<Coordinates, Card> field, List<Coordinates> fieldBuildingHelper){
        int i,j;
        matrix = new String[240][880];
        for (Coordinates coordinate : fieldBuildingHelper){
            Card card = field.get(coordinate);
            String color = card.getReign()!=null? card.getReign().getColorBG():YELLOW;
            int x = coordinate.x;
            int y = coordinate.y;
            j = 440 + 10*(x);
            i = 120 - 2*(y);
            for (int k = i-1; k<=i+1; k++){
                for (int h = j-6 ; h<=j+4; h++){ //h is off-center for Ansi escape sequence reasons
                    matrix[k][h] = " "+color;
                }
            }
            matrix[i][j] = color+fieldBuildingHelper.indexOf(coordinate);
        }
        writer.print(matrix);
    }
    /**
     * @author Giuseppe Laguardia, Giorgio Mattina, Riccardo Lapi, Maximilian Mangosi
     * print the user field and the available positions
     * @param field the user field
     * @param fieldBuildingHelper List of the coordinates in order of insertion
     * @param availablePositions List of coordinates where is legal play a card
     */
    public void show(Map<Coordinates, Card> field, List<Coordinates> fieldBuildingHelper,List<Coordinates> availablePositions){
        addAvailablePosToField(availablePositions);
        show(field,fieldBuildingHelper);
    }

    /**
     * @author Riccardo Lapi
     * add the available postions of the player inside the matrix
     * @param availablePositions List of position where the user can play the card
     */
    private void addAvailablePosToField(List<Coordinates> availablePositions ){

        for(Coordinates coordinates : availablePositions){
            int j = 440 + 10*(coordinates.x);
            int i = 120 - 2*(coordinates.y);
            matrix[i][j] = String.valueOf(availablePositions.indexOf(coordinates));
        }
    }

    /**
     * @author Giuseppe Laguardia
     * @return matrix
     */
    public String[][] getMatrix() {
        return matrix;
    }

    /**
     * @author Giuseppe Laguardia
     * reset the matrix
     */
    public void resetMatrix() {
        matrix=new String[5][46];
    }

    /**
     *
     * return the ANSI escape sequence for the given check
     * @param check a String
     * @return a String containing a Ansi escape sequence
     */
    public String getAnsiColor(String check){
        return switch (check) {
            case "Green" -> GREEN;
            case "Yellow" -> YELLOW;
            case "Blue" -> BLUE;
            case "Red" -> RED;
            default -> RESET;
        };

    }

}
