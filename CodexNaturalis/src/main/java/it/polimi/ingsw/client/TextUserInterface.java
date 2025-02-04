package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import it.polimi.ingsw.view.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that handles the displaying of the TUI and communicate with the server
 * @author Giuseppe Laguardia
 */
public class TextUserInterface extends UserInterface {
    private final TUIAsciiArtist artist;
    private final UpdateTUI tuiUpdater;
    private final OutStreamWriter outWriter = new OutStreamWriter();

    private StringBuilder idleUI;
    private final Scanner s=new Scanner(System.in);

    /**
     * TextUserInterface's constructor: sets the View from witch the user will communicate with the server and creates the thread that handles the CLI.
     */
    public TextUserInterface() {
        tuiUpdater=new UpdateTUI(outWriter,this);
        artist=new TUIAsciiArtist(outWriter);
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

    public synchronized void updateIdleUI() throws RemoteException {
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
            try {
                if(getPrivateGoal() ==null){
                    idleUI.append("To start the game you have to choose your private goal from your goal options, try choose-goal\n");
                }
                if(!didIPlayStarterCard()){
                    idleUI.append("To start the game you have to choose the side of your starter card, try choose-starter-card-side\n");
                }
            } catch ( InvalidUserId ignore) {}
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

    public void execCmd(String cmd) throws IOException, IllegalOperationException, IsNotYourTurnException, InvalidUserId, ClassNotFoundException, HandNotFullException, IllegalPositionException, InvalidCardException, HandFullException, DeckEmptyException, InvalidGoalException {

        boolean error = true;
        synchronized (outWriter) {
            switch (cmd){
                case "start-game":
                    if(isPlaying)
                        outWriter.print("You are already playing a game!");
                    else{
                        // if user choose socket connection viewContainer is not initialized
                       startGame(viewContainer!=null);
                    }
                    break;
                case "choose-goal":
                    outWriter.print("Here are your goals, choose one (1,2)");
                    Goal[] myGoals = getGoalOptions();
                    artist.show(myGoals);
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
                    boolean isChosenFront;
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
                    printIdleUI();
                    break;
                case "choose-starter-card-side":
                    outWriter.print("Which side for the starter card? (f for front, b or any for back)");
                    artist.show(getStarterCard(),true);
                    boolean isChosenFrontStart = s.nextLine().equals("f");
                    view.chooseStarterCardSide(isChosenFrontStart, myID);
                    outWriter.print("Starter card chosen");
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    printIdleUI();
                    break;

                case "draw-card-deck":
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
                    artist.show(visibleCard);
                    while (error) {
                        try {
                            outWriter.print("Which card do you want to draw? (0, 1, 2, 3)");
                            int chosenDrawCard = s.nextInt();
                            view.drawVisibleCard(myID,chosenDrawCard);
                            error=false;
                        } catch (InvalidChoiceException e) {
                            outWriter.print(e.getMessage()+"[0,3]");
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
                   Goal privateGoal= getPrivateGoal();
                   if(privateGoal==null){
                       throw new IllegalOperationException("show-my-goal");
                   }
                   artist.show(new Goal[]{privateGoal});
                   outWriter.print("Press enter to continue");
                   s.nextLine();
                   break;
                case "show-public-goals":
                   artist.show(view.getPublicGoals());
                   outWriter.print("Press enter to continue");
                   s.nextLine();
                   break;
                case "show-my-field":
                    showField(myName,false);
                    outWriter.print("Press enter to continue");
                    s.nextLine();
                    break;
                case "show-field":
                    String player= null;
                    while(error) {
                        outWriter.print("What player do you want to see the field? Insert his username, insert -q tu exit");
                        player = s.nextLine();
                        if(player.equals("-q")|| view.getPlayersList().contains(player)){ error=false;}
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
                    isPlaying=false;
                    outWriter.print("You quit the game, type 'start-game' to restart playing\n");
                    break;
                case "exit":
                    view.closeGame(myID);
                    tuiUpdater.interrupt();
                    System.exit(1);
                    break;
                case "open-global-chat":
                    outWriter.print("Write -c to exit from chat");
                    outWriter.print("Write -m to enter write mode");
                    AtomicBoolean stop2= new AtomicBoolean(false);
                    Thread t2 = new Thread(()->printGlobalChat(stop2));
                    t2.start();
                    while(true) {
                        outWriter.print("Write your message: ");
                        String message = promptForChat(stop2,t2);
                        if(message==null) break;
                        view.sendChatMessage(message);
                        stop2.set(false);
                    }
                    break;
                case "open-private-chat":

                    while(true) {
                        outWriter.print("Who do you want to message with? ");
                        String name = s.nextLine();
                        if (!view.getPlayersList().contains(name)) {
                            outWriter.print(name + " does not exist");
                        }if (name.equals(myName)){
                            outWriter.print("You can not write a message to yourself");
                        }else {
                            outWriter.print("Write -c to exit from chat");
                            outWriter.print("Write -m to enter write mode");
                            AtomicBoolean stop1=new AtomicBoolean(false);
                            Thread t1 = new Thread(()->printChat(name,stop1));
                            t1.start();
                            while(true) {
                                String message = promptForChat(stop1, t1);
                                if (message == null) break;
                                view.sendPrivateMessage(name, message, myID);
                                stop1.set(false);
                            }
                            break;
                        }
                    }
                    break;
                case "help-command":
                    printTutorial();
                    outWriter.print("press enter to continue");
                    s.nextLine();
                    break;
                case "help-card":
                    printTutorialCard();
                    outWriter.print("press enter to continue");
                    s.nextLine();
                    break;
                default:
                    outWriter.print("Unknown command, try help-command");
                    outWriter.print("press enter to continue");
                    s.nextLine();
            }
            updateIdleUI();
            printIdleUI();
        }
    }

    public void printTutorialCard() {
        Scanner s= new Scanner(getClass().getResourceAsStream("/TUI-tutorial-card.txt"));
        while (s.hasNext()){
            outWriter.print(s.nextLine());
        }
        s.close();
    }

    /**
     * prompts for the user to write for the chat
     * @author Giuseppe Laguardia
     * @param stop1
     * @param t1
     * @return the message to be sent
     *
     */
    private String promptForChat(AtomicBoolean stop1, Thread t1)  {
        String mode = s.nextLine();
        if(mode.equals("-m")) {
            stop1.set(true);
        } else if (mode.equals("-c")) {
            t1.interrupt();
            return null;
        }
        return myName +": "+ s.nextLine();
    }

    /**
     * Prints the global chat to the user using a thread to continue updating
     * @author Maximilian Mangosi
     * @param stop
     */
    private void printGlobalChat(AtomicBoolean stop) {
        try {
            while (true){
                if(!stop.get()) {
                    outWriter.clearScreen();
                    List<String> chat = view.getChatList();
                    printMessages(chat);
                    Thread.sleep(3000);
                }

            }
        }catch (RemoteException e){
            outWriter.print("connection error");
            System.exit(1);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * function to print the message
     * @author Giuseppe Laguardia
     * @param chat
     * @throws RemoteException
     * @throws InterruptedException
     */
    private void printMessages(List<String> chat) throws RemoteException, InterruptedException {
        if(chat!=null) {
            for (String message : chat) {
                int i=message.indexOf(":");
                String sender= message.substring(0,i);
                String color=artist.getAnsiColor(view.getPlayerColor(sender));
                outWriter.print(message.replace(sender,color+sender+ TUIAsciiArtist.RESET));
            }
        }
    }

    /**
     * prints the private chat using threads to update
     * @author Maximilian Mangosi
     * @param name
     * @param stop
     */
    private void printChat(String name, AtomicBoolean stop){
        try {
            while (true){
                if (!stop.get()) {
                    outWriter.clearScreen();
                    List<String> chat = getPrivateChat(name);
                    printMessages(chat);
                    Thread.sleep(3000);
                }
            }
        }catch (RemoteException e){
            outWriter.print("connection error");
            System.exit(1);
        } catch (InterruptedException ignored) {}
    }

    /**
     * reads from terminal the choice of the position, then returns the Coordinates of that choice
     * @author Giuseppe Laguardia
     * @return coordinates of the card corresponding to the user's choice
     * @throws RemoteException
     * @throws InvalidUserId
     */
    private Coordinates promptForChosenPosition() throws RemoteException, InvalidUserId, IllegalOperationException {
        int chosenPositionI = s.nextInt();
        s.nextLine();
        List<Coordinates> availableCoordinates = getPlayersLegalPositions();
        Coordinates coordinates = availableCoordinates.get(chosenPositionI);
        return coordinates;
    }

    /**
     * prompt the user to insert the number of users
     * @author Giuseppe Laguardia
     * @return
     */
    private int promptForNumPlayers() {
        outWriter.print("Insert number of players");
        int numPlayers = 0;
        boolean error=true;
        while (error) {
            try {
                numPlayers = s.nextInt();
                s.nextLine();
                error=false;
            } catch (InputMismatchException e) {
                s.nextLine();
                outWriter.print("Please insert only a number");
            }

        }
        return numPlayers;
    }

    /**
     * function to join a selected game
     * @author Giuseppe Laguardia
     * @param isRMI
     * @param gameID
     * @throws InvalidGameID
     */
    private void joinGame(boolean isRMI,UUID gameID) throws InvalidGameID {
        boolean error=true;
        promptForUsername();
        while (error) {
            try {
                if (isRMI) {
                    myID=viewContainer.joinGame(gameID,myName);
                    view=viewContainer.getView(gameID);
                }else {
                    myID=view.joinGame(gameID, myName);
                }
                error=false;
            } catch (PlayerNameNotUniqueException e) {
               outWriter.print(e.getMessage());
               promptForUsername();
            } catch (IOException | ClassNotFoundException e1) {
                outWriter.print("Connection error");
                System.exit(1);
            }  catch (IllegalOperationException ignore) {}
        }

    }
    /**
     * user selects which deck to draw from
     * @author Giuseppe Laguardia
     * @return chosenDeck
     * @throws RemoteException
     */
    private Integer promptForChosenDeck() throws RemoteException, IllegalOperationException {
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

    /**
     * user decides the side of the card
     * @author Giuseppe Laguardia
     * @return
     */
    private boolean promptForSide(){
        boolean isChosenFront;
        outWriter.print("Which side? (f for front, b or any for back)");
        isChosenFront = s.nextLine().equals("f");
        return isChosenFront;
    }
    /**
     * user prompts to see his current hand
     * @author Giuseppe Laguardia
     * @throws RemoteException
     * @throws InvalidUserId
     */
    private void showHand() throws RemoteException, InvalidUserId, IllegalOperationException {
        List<Card> hand ;
        hand=getHand();
        artist.show(hand);
    }

    /**
     * Check if the player has played the starter card
     * @return true if the starter card is on the player's field, false otherwise
     * @throws RemoteException if there are connection problem with the server
     */
    private boolean didIPlayStarterCard() throws RemoteException {
        StarterCard myStarterCard= getStarterCard();
        Map<Coordinates,Card> field= null;
        try {
            field = view.getPlayersField(myName);
        } catch (IllegalOperationException ignore) {}
        if(field!=null)
            return field.containsValue(myStarterCard);
        return false;
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
    public void printIdleUI() throws RemoteException {
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
    private Card promptForChosenCard() throws InvalidUserId, RemoteException, IllegalOperationException {
        outWriter.print("Which card do you want to play? (1,2,3)");
        showHand();
        int chosenCardI= s.nextInt();
        s.nextLine();
        return getHand().get(chosenCardI - 1);
    }


    /**
     * @author Giuseppe Laguardia
     * print the filed with the users cards, and the available positions where he can play the new card
     * @param username the player username
     * @param availablePosition boolean that indicate whether to also print the available position of the new card
     * @throws InvalidUserId
     * @throws RemoteException
     */
    private void showField(String username,boolean availablePosition) throws InvalidUserId, RemoteException, IllegalOperationException {
        Map<Coordinates, Card> field=view.getPlayersField(username);
        List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);
        if(availablePosition) {
            List<Coordinates> availablePositions=getPlayersLegalPositions();
            artist.show(field,fieldBuildingHelper,availablePositions);
            return;
        }
        artist.show(field,fieldBuildingHelper);
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
                    artist.show(st,false);
                    outWriter.print(artist.getMatrix(),st.isFront());
                }
                else{
                    Coordinates chosenCardPosition=fieldBuildingHelper.get(chosenCardInt);
                    artist.show(field.get(chosenCardPosition),true);
                }

            }
        }

    }

    /**
     * prompt to create or join a game
     * @author Giuseppe Laguardia
     * @return
     */
    private String promptForGameChoice() {
        outWriter.print("If you want join one of this game insert the corresponding number");
        outWriter.print("If you want to create a new game press enter");
        outWriter.print("If you want to refresh the game list press -r");
        return s.nextLine();
    }

    /**
     * shows the list of joinable games
     * @author Giuseppe Laguardia
     * @param views the map containing the list of players for each game
     * @return the list of GameID
     */
    private List<UUID> showJoinableGames(Map<UUID, List<String>> views) {
       List<UUID> joinableGames=new ArrayList<>();
        int i=1;
        for (Map.Entry<UUID, List<String>> entry : views.entrySet()) {
            outWriter.print(String.format("Game %d:", i));
            outWriter.print(entry.getValue());
            joinableGames.add(entry.getKey());
            i++;
        }
        return joinableGames;
    }

    /**
     * boots the game
     * @author Giuseppe Laguardia
     * @param isRMI parameter to see if the choice is rmi or socket
     * @throws IOException when a connection error occurs
     * @throws ClassNotFoundException when a connection error occurs
     */
    private void bootGame(boolean isRMI) throws IOException, ClassNotFoundException {
        boolean error=true;
        promptForUsername();
        int numOfPlayers=promptForNumPlayers();
        while (error) {
            try {
                GameKey gameKey= isRMI ? viewContainer.bootGame(numOfPlayers,myName):view.bootGame(numOfPlayers,myName);
                myID=gameKey.userID();
                gameID=gameKey.gameID();
                if(isRMI)
                    view=viewContainer.getView(gameID);
                error=false;
            } catch (UnacceptableNumOfPlayersException e) {
                outWriter.print(e.getMessage());
                numOfPlayers = promptForNumPlayers();
            } catch (PlayerNameNotUniqueException e1) {
                outWriter.print(e1.getMessage());
                promptForUsername();
            } catch (IllegalOperationException | InvalidGameID ignore) {}
        }
        isPlaying=true;
        if (!isRMI) {
            new ServerHandler((ViewSocket) view, tuiUpdater, new GameKey(gameID, myID),serverAddress).start();
        }
        new PingPong(view,myID).start();
        tuiUpdater.start();

    }

    /**
     * user inserts the username
     * @author Giuseppe Laguardia
     */
    private void promptForUsername() {
        outWriter.print("Insert username");
        myName = s.nextLine();
        while (myName.isBlank() ) {
            outWriter.print("Username cannot be blank,please insert another one");
            myName = s.nextLine();
        }
    }

    /**
     * the user starts the game
     * @author Giuseppe Laguardia
     * @param isRMI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void startGame(boolean isRMI) throws IOException, ClassNotFoundException {
        ViewSocket viewSocket = (ViewSocket) view;
        Map<UUID, List<String>> views = isRMI? viewContainer.getJoinableGames():viewSocket.getJoinableGames();
        if(views.isEmpty()) {
            outWriter.print("Currently there isn't any game hosted, you wish to create a new one? Type yes in this case");
            String reply=s.nextLine().toLowerCase();
            if(reply.equals("yes")) {
                bootGame(isRMI);
                return;
            }
        }
        List<UUID> joinableGames = showJoinableGames(views);
        String choice = promptForGameChoice();
        while (choice.isBlank() || choice.equals("-r")) {
            if (choice.isBlank()) {
                bootGame(isRMI);
                return;
            }
            views = isRMI? viewContainer.getJoinableGames():viewSocket.getJoinableGames();
            joinableGames = showJoinableGames(views);
            choice = promptForGameChoice();
        }
        boolean error = true;
        while (error) {
            try {
                int choiceInt = Integer.parseInt(choice)-1;
                gameID = joinableGames.get(choiceInt);
                joinGame(isRMI,gameID);
                error = false;
            } catch (NumberFormatException e) {
                outWriter.print("Please insert a number");
                choice = s.nextLine();
            }catch (IndexOutOfBoundsException e1) {
                outWriter.print("Invalid number,insert again");
                choice=s.nextLine();
            } catch (InvalidGameID e) {
                outWriter.print(e.getMessage());
                outWriter.print("The game chosen is been closed");
                startGame(isRMI);
            }
        }
        isPlaying=true;
        if (!isRMI) {
            new ServerHandler(viewSocket, tuiUpdater, new GameKey(gameID, myID),serverAddress ).start();
        }
        new PingPong(view,myID).start();
        tuiUpdater.start();

    }

    /**
     * prints the logo of codex
     * @author Giuseppe Laguardia
     */
    void printLogo(){
        Scanner s= new Scanner(getClass().getResourceAsStream("/ascii-logo"));
        outWriter.print("\u001B[33m");
        while (s.hasNext()){

            outWriter.print(s.nextLine());
        }
        outWriter.print(TUIAsciiArtist.RESET);
        s.close();
    }

    /**
     * prints the tutorial of the game
     * @author Giuseppe Laguardia
     */
    public void printTutorial(){
        Scanner s= new Scanner(getClass().getResourceAsStream("/TUI-tutorial"));
        while (s.hasNext()){
            outWriter.print(s.nextLine());
        }
        s.close();
    }
}
