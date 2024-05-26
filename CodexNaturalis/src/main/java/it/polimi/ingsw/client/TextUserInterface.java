package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewRMIInterface;
import it.polimi.ingsw.view.ViewSocket;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Class that handles the displaying of the TUI and communicate with the server
 * @author Giuseppe Laguardia
 */
public class TextUserInterface  {
    private final TUIAsciiArtist artist = new TUIAsciiArtist();
    private final View view;
    private final UpdateTUI tuiUpdater;
    private final OutStreamWriter outWriter = new OutStreamWriter();
    private UUID myID;
    private String myName;
    private StringBuilder idleUI;
    private final Scanner s=new Scanner(System.in);

    /**
     * TextUserInterface's constructor: sets the View from witch the user will communicate with the server and creates the thread that handles the CLI
     * @param view View to communicate with servers.
     */
    public TextUserInterface(View view) {
        this.view = view;
        tuiUpdater=new UpdateTUI(outWriter,this);
    }

    /**
     * Return the username
     * @author Giuseppe Laguardia
     * @return a string containing the username
     */
    public String getMyName() {
        return myName;
    }
    /**
     * Set the username
     * @author Giuseppe Laguardia
     */
    public void setMyName(String myName) {
        this.myName = myName;
    }

    /**
     * Modifies the cli look according to data from View.
     * displays always all the players' username currently in game
     * if the game is not ready to start [i.e. the lobby is not full] displays the message "waiting for players..."
     * if the game is ready to start displays all the players' username, the scoreboard and suggest to user to choose the private goal and the starter card side
     * if the game is ended displays the winner's username
     * @author Giuseppe Laguardia
     * @throws RemoteException if there's a connection problem with the server
     * @throws InvalidUserId
     */

    public synchronized void updateIdleUI() throws RemoteException, InvalidUserId {
        idleUI=new StringBuilder();
        String winner=view.getWinner();
        List<String> players=view.getPlayersList();
        if(players!=null) {
            idleUI.append("Players: ");
            for (String player : players) {
                idleUI.append(player);
                if (!player.equals(players.getLast()))
                    idleUI.append(", ");
            }
            idleUI.append("\n\n");
        }
        if (view.isGameEnded()){
            idleUI.append(winner);
            idleUI.append(" WINS!!!\n\n");

        }else if(view.isGameStarted()){

            String currentPlayer= view.getCurrentPlayer();
            Map<String,Integer> scoreboard=view.getPlayersPoints();
            idleUI.append("\n");

            if (currentPlayer!=null) {
                if(currentPlayer.equals(myName)){
                    idleUI.append("It's your turn.\n");
                }
                else{
                    idleUI.append(currentPlayer);
                    idleUI.append(" is playing.\n");
                }

            }
            if(scoreboard!=null){
                idleUI.append("Scoreboard:\n");
                for (Map.Entry<String,Integer> entry: sortedScoreboard(scoreboard)){
                    idleUI.append(entry.getKey());
                    idleUI.append(": ");
                    idleUI.append(entry.getValue());
                    idleUI.append("\n");
                }
            }

            idleUI.append("\n\n");
            if(showPrivateGoal() ==null){
                idleUI.append("To start the game you have to choose your private goal from your goal options, try choose-goal\n");
            }if(!didIPlayStarterCard()){
                idleUI.append("To start the game you have to choose the side of your starter card, try choose-starter-card-side\n");
            }
        } else{
            idleUI.append("Waiting for players...");
        }

    }
    /**
     * @author Riccardo Lapi
     * given the command the user want to execute, it asks the user for the parameters it needs to perform that operation
     * @param cmd string that represent the command to use
     * @throws RemoteException
     * @throws IllegalOperationException
     * @throws InvalidUserId
     * @throws InvalidGoalException
     * @throws HandNotFullException
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     * @throws IllegalPositionException
     * @throws InvalidCardException
     * @throws HandFullException
     * @throws InvalidChoiceException
     * @throws DeckEmptyException
     * @author Riccardo Lapi
     * given the command the user want to execute, it asks the user for the parameters it needs to perform that operation
     */

    public void execCmd(String cmd) throws IOException, IllegalOperationException, InvalidUserId, HandFullException, InvalidChoiceException, IsNotYourTurnException, HandNotFullException, InvalidCardException, InvalidGoalException, ClassNotFoundException, NoGameExistsException, RequirementsNotMetException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalPositionException, DeckEmptyException {

        boolean error = true;
        synchronized (outWriter) {
            switch (cmd){
                case "start-game":
                    try {
                        joinGame();
                        
                    }catch (PlayerNameNotUniqueException e) {
                        outWriter.print(e.getMessage());
                        handleNameNotUnique();
                    }catch (NoGameExistsException e){
                        outWriter.print(e.getMessage());
                        while (error) {
                            try {
                                int numPlayers = promptForNumPlayers();
                                myID = view.bootGame(numPlayers, myName);
                                error = false;
                            } catch (UnacceptableNumOfPlayersException ex1) {
                                outWriter.print(ex1.getMessage());
                            } catch (OnlyOneGameException ex) {
                                outWriter.print(ex.getMessage());
                                try {
                                    view.joinGame(myName);
                                    error = false;
                                } catch (PlayerNameNotUniqueException e1) {
                                    outWriter.print(e1.getMessage());
                                    error = handleNameNotUnique();
                                } catch (NoGameExistsException ignore) {
                                }
                            }
                        }
                    }
                    if (view.isRMI()) {
                        PingPongRMI td1 = new PingPongRMI((ViewRMIInterface) view, myID);
                        td1.start();
                    }
                    else{
                        ServerHandler td2= new ServerHandler((ViewSocket) view,tuiUpdater,myID);
                        td2.start();

                    }
                    view.initializeFieldBuildingHelper( myName);
                    tuiUpdater.start();
                    break;
                case "choose-goal":
                    outWriter.print("Here are your goals, choose one (1,2)");
                    Goal[] myGoals = getGoalOptions();
                    artist.show(myGoals);
                    outWriter.print(artist.getMatrix());
                    String myGoal = s.nextLine();
                    Goal goal = Objects.equals(myGoal, "1") ? myGoals[0] : myGoals[1];
                    view.chooseGoal(myID, goal);
                    outWriter.print("Goal chosen,press enter to continue");
                    s.nextLine();
                    printIdleUI();
                    break;
                case "play-card":
                    Card chosenCard = null;
                    Coordinates chosenPosition=null;
                    boolean isChosenFront=false;
                    try {
                        chosenCard=promptForChosenCard();
                        isChosenFront = promptForSide();
                        
                        showField(myName,true);
                        
                        outWriter.print("Where do you want to place the selected card? (int)");

                        chosenPosition = promptForChosenPosition();

                        if(isChosenFront) view.playCardFront(chosenCard,chosenPosition, myID);
                        else view.playCardBack(chosenCard, chosenPosition, myID);


                    } catch (RequirementsNotMetException e) {
                        outWriter.print(e.getMessage());
                        while (error) {
                            try {
                                outWriter.print("Do you want play this card on back? Y/N");
                                boolean playBack = s.nextLine().toUpperCase(Locale.ROOT).equals("Y");
                                if (playBack) view.playCardBack(chosenCard, chosenPosition, myID);
                                else {
                                    outWriter.print("Do you want to play another card in this position? Y/N");
                                    boolean changeCard = s.nextLine().toUpperCase(Locale.ROOT).equals("Y");
                                    chosenCard = promptForChosenCard();
                                    isChosenFront = promptForSide();
                                    if (!changeCard) {
                                        showField(myName,true);
                                        chosenPosition=promptForChosenPosition();
                                    }
                                    if (isChosenFront)
                                        view.playCardFront(chosenCard, chosenPosition, myID);
                                    else
                                        view.playCardBack(chosenCard, chosenPosition, myID);
                                    error = false;
                                }
                            } catch (RequirementsNotMetException ex) {
                                outWriter.print(e.getMessage());
                            }
                        }
                    }
                    //print field
                    showField(myName,false);
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    printIdleUI();
                    break;
                case "choose-starter-card-side":
                    outWriter.print("Which side for the starter card? (f for front, b or any for back)");
                    artist.show(getStarterCard());
                    outWriter.print(artist.getMatrix());
                    boolean isChosenFrontStart = s.nextLine().equals("f");
                    view.chooseStarterCardSide(isChosenFrontStart, myID);
                    outWriter.print("Starter card chosen");
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    printIdleUI();
                    break;

                case "draw-card-from-deck":
                   while (error) {
                       try {
                           Integer chosenDeck = promptForChosenDeck();
                           if (chosenDeck == null) break;
                           view.drawFromDeck(myID, chosenDeck);
                           error=false;
                       } catch (InvalidChoiceException e) {
                           outWriter.print(e.getMessage()+"[0,1]");
                       }
                   }
                   showHand();
                   outWriter.print("Press enter to continue");
                   s.nextLine();
                   printIdleUI();
                   break;
                case "draw-card-visible":
                    List<Card> visibleCard= view.getVisibleCards();
                    for(Card card:visibleCard){
                        artist.show(card);
                    }
                    while (error) {
                        try {
                            outWriter.print("Which card do you want to draw? (0, 1, 2, 3)");
                            int chosenDrawCard = s.nextInt();
                            view.drawVisibleCard(myID,chosenDrawCard);
                            error=false;
                        } catch (InvalidChoiceException e) {
                            outWriter.print(e+"[0,3]");
                        }
                    }
                    showHand();
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    printIdleUI();
                    break;
                case "show-hand":
                    showHand();
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    break;
                case "show-my-goal":
                   Goal privateGoal= showPrivateGoal();
                   artist.show(new Goal[]{privateGoal});
                   outWriter.print(artist.getMatrix());
                   outWriter.print("Press enter to continue");
                   s.nextLine();
                   artist.resetMatrix();
                   break;
                case "show-public-goal":
                   artist.show(view.getPublicGoals());
                   outWriter.print(artist.getMatrix());

                   outWriter.print("Press enter to continue");
                   s.nextLine();
                   artist.resetMatrix();
                   break;
                case "show-my-field":
                    showField(myName,false);
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    break;
                case "show-field":
                    String player= null;
                    while(error) {
                        outWriter.print("What player do you want to see the field? Insert his username");
                        player = s.nextLine();
                        if(view.getPlayersList().contains(player)){ error=false;}
                        else{
                            outWriter.print("This player doesn't exists");
                        }
                    }
                    showField(player,false);
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    break;
                case "disconnect":
                    view.closeGame(myID);
                    tuiUpdater.interrupt();
                    outWriter.print("You quit the game, type 'start-game' to restart playing\n");
                    break;
                case "show-chat":
                    List<String> chatShow = view.getChatList();
                    for (String chats : chatShow){
                        outWriter.print(chats);
                    }
                    break;
                case "write-chat":
                    List<String> chatWrite = view.getChatList();
                    for (String chats : chatWrite){
                        outWriter.print(chats);
                    }
                    outWriter.print("Write your message: ");
                    view.sendChatMessage(myName + ": " + s.nextLine());
                    break;
                default:
                    outWriter.print("Unknown command");
            }
        }
    }

    private Goal[] getGoalOptions() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPlayerGoalOptions(myID);
        return view.showPlayerGoalOptions();
    }

    private Goal showPrivateGoal() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPrivateGoal(myID);
        return view.showPrivateGoal();
    }

    /**
     * reads from terminal the choice of the position, then returns the Coordinates of that choice
     * @author Giuseppe Laguardia
     * @return coordinates of the card corresponding to the user's choice
     * @throws RemoteException
     * @throws InvalidUserId
     */
    private Coordinates promptForChosenPosition() throws RemoteException, InvalidUserId {
        int chosenPositionI = s.nextInt();
        s.nextLine();
        List<Coordinates> availableCoordinates = view.showPlayersLegalPositions(myID);
        Coordinates chosenPosition = availableCoordinates.get(chosenPositionI);
        return chosenPosition;
    }

    /**
     * tryes to call joinGame and catches PlayerNameNotUniqueException and NoGameExistsException
     * @return false after calling joinGame successfully
     * @author Giuseppe Laguardia
     * @throws RemoteException
     * @throws IllegalOperationException
     */
    private boolean handleNameNotUnique() throws IOException, IllegalOperationException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        while (true){
            try {
                joinGame();
                return false;
            } catch (PlayerNameNotUniqueException ex) {
                outWriter.print(ex.getMessage());
            } catch (NoGameExistsException ignore) {
            }
        }
    }

    private int promptForNumPlayers() {
        outWriter.print("Insert number of players");
        int numPlayers = s.nextInt();
        s.nextLine();
        return numPlayers;
    }

    private void joinGame() throws IOException, NoGameExistsException, PlayerNameNotUniqueException, IllegalOperationException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        outWriter.print("Insert username");
        myName = s.nextLine();
        myID=view.joinGame(myName);
    }

    private Integer promptForChosenDeck() throws RemoteException {
        outWriter.print("From which deck do you want to draw? (0,1)");
        Reign topResourceCard= null;
        Reign topGoldCard= null;
        try {
            topResourceCard = view.getTopOfResourceCardDeck();
            topGoldCard = view.getTopOfGoldCardDeck();
        } catch (NoSuchElementException ignore) {}
        if(topResourceCard==null && topGoldCard  == null){
            outWriter.print("Both deck are empty, please draw from visible card");
            return null;
        }
        if(topResourceCard!=null ) {
            outWriter.print("The card on top of Resource Card deck is: " + topResourceCard.getColorFG() + topResourceCard.getSymbol()+TUIAsciiArtist.RESET);
        }else{
            outWriter.print("The Resource Card deck is empty");
        }
        if(topGoldCard!=null){
            outWriter.print("The card on top of Gold Card deck is:"+topGoldCard.getColorFG()+ topGoldCard.getSymbol()+TUIAsciiArtist.RESET);
        }else {
            outWriter.print("The Gold Card deck is empty");
        }
        int chosenDeck = s.nextInt();
        s.nextLine();
        return chosenDeck;
    }

    private boolean promptForSide(){
        boolean isChosenFront;
        outWriter.print("Which side? (f for front, b or any for back)");
        isChosenFront = s.nextLine().equals("f");
        return isChosenFront;
    }

    private void showHand() throws RemoteException, InvalidUserId {
        List<Card> hand ;
        hand=getHand();
        for(Card card:hand){
            artist.show(card);
        }
    }

    /**
     * Check if the player has played the starter card
     * @return true if the starter card is on the player's field, false otherwise
     * @throws RemoteException if there are connection problem with the server
     */
    private boolean didIPlayStarterCard() throws RemoteException {
        StarterCard myStarterCard= getStarterCard();
        Map<Coordinates,Card> field= view.getPlayersField(myName);
        if(field!=null)
            return field.containsValue(myStarterCard);
        return false;
    }

    private StarterCard getStarterCard() throws RemoteException {
        if(view.isRMI())
            return view.getStarterCard(myID);
        return view.getStarterCard();
    }

    /**
     * sorts the scoreboard in order of points
     * @param scoreboard the scoreboard to sort
     * @return a List of Entry <Username,Points>

     */
    private List<Map.Entry<String, Integer>> sortedScoreboard(Map<String, Integer> scoreboard) {
        return scoreboard.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList().reversed();
    }

    /**
     * IdleUI getter
     * @return the string containing the UI to display on command line
     */
    public String getIdleUI() {
        return idleUI.toString();
    }

    /**
     * Getter of the user's identifier
     * @return the UUID associated to the users
     */
    public UUID getMyID() {
        return myID;
    }

    /**
     * @author Giuseppe Laguardia
     * update the ui
     * @throws InvalidUserId
     * @throws RemoteException
     */
    public void printIdleUI() throws InvalidUserId, RemoteException {
        updateIdleUI();
        outWriter.clearScreen();
        outWriter.print(String.valueOf(idleUI));
    }

    /**
     * @author Giuseppe Laguardia
     * ask the user which card to play and return it
     * @return the choosen Card
     * @throws InvalidUserId
     * @throws RemoteException
     */
    private Card promptForChosenCard() throws InvalidUserId, RemoteException {
        outWriter.print("Which card do you want to play? (1,2,3)");
        showHand();
        int chosenCardI= s.nextInt();
        s.nextLine();
        return getHand().get(chosenCardI - 1);
    }

    private List<Card> getHand() throws RemoteException, InvalidUserId {
        if(view.isRMI())
            return view.showPlayerHand(myID);
        return view.showPlayerHand();

    }

    /**
     * @author Giuseppe Laguardia
     * print the filed with the users cards, and the available positions where he can play the new card
     * @param username the player username
     * @param availablePosition boolean that indicate whether to also print the available position of the new card
     * @throws InvalidUserId
     * @throws RemoteException
     */
    private void showField(String username,boolean availablePosition) throws InvalidUserId, RemoteException {
        Map<Coordinates, Card> field=view.getPlayersField(username);
        List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);
        artist.show(field,fieldBuildingHelper);
        if(availablePosition) {
            artist.addAvailablePosToField(view.showPlayersLegalPositions(myID));
            outWriter.print(artist.getAsciiField(), fieldBuildingHelper);
            return;
        }
        outWriter.print(artist.getAsciiField(), fieldBuildingHelper);
        outWriter.print("Do you want see a card? Insert the card number or insert any other character to continue:");
        while(true) {
            int chosenCardInt= 0;
            try {
                chosenCardInt = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) { return;}
            if(chosenCardInt>=fieldBuildingHelper.size()){
                outWriter.print("Invalid number!");
            }else{
                if(chosenCardInt==0){
                    StarterCard st = getStarterCard();
                    artist.show(st);
                    outWriter.print(artist.getMatrix(),st.IsFront());//todo update with showStarterCard
                }
                else{
                    Coordinates chosenCardPosition=fieldBuildingHelper.get(chosenCardInt);
                    artist.show(field.get(chosenCardPosition));
                }

            }
        }

    }
}
