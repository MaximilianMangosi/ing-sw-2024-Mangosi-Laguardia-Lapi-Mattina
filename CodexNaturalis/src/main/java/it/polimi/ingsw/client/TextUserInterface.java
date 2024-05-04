package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Class that handles the displaying of the TUI and communicate with the server
 * @author Giuseppe Laguardia
 */
public class TextUserInterface  {
    private final TUIAsciiArtist artist = new TUIAsciiArtist();
    private final ViewInterface view;
    private final UpdateTUI tuiUpdater;
    private final OutStreamWriter outWriter = new OutStreamWriter();
    private UUID myID;
    private String myName;
    private StringBuilder idleUI;

    /**
     * TextUserInterface's constructor: sets the View from witch the user will communicate with the server and creates the thread that handles the CLI
     * @param view View to communicate with servers.
     */
    public TextUserInterface(ViewInterface view) {
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
        idleUI.append("Players: ");
        for (String player : players) {
            idleUI.append(player);
            if (!player.equals(players.getLast()))
                idleUI.append(", ");
        }
        idleUI.append("\n");
        if (view.isGameEnded()){
            idleUI.append(winner);
            idleUI.append("WINS!!!\n\n");

        }else if(view.isGameStarted()){

            String currentPlayer= view.getCurrentPlayer();
            Map<String,Integer> scoreboard=view.getPlayersPoints();
            idleUI.append("\n");

            if (currentPlayer!=null) {
                idleUI.append(currentPlayer);
                idleUI.append(" is playing.\n");
            }
            idleUI.append("Scoreboard:\n");
            for (Map.Entry<String,Integer> entry: sortedScoreboard(scoreboard)){
                idleUI.append(entry.getKey());
                idleUI.append(": ");
                idleUI.append(entry.getValue());
                idleUI.append("\n");
            }
            idleUI.append("\n\n");
            if(view.showPrivateGoal(myID)==null){
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
     */

    public void execCmd(String cmd) throws RemoteException, IllegalOperationException, InvalidUserId, HandFullException, InvalidChoiceException, IsNotYourTurnException, DeckEmptyException, HandNotFullException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, InvalidGoalException {
        Scanner s=new Scanner(System.in);
        boolean error = true;
        synchronized (outWriter) {
            switch (cmd){
                case "start-game":
                    try {
                        outWriter.print("Insert username");
                        myName = s.nextLine();
                        myID=view.joinGame(myName);

                    }catch (PlayerNameNotUniqueException e) {
                        outWriter.print(e.getMessage());
                        while (error){
                            try {
                                outWriter.print("Insert username");
                                myName = s.nextLine();
                                myID = view.joinGame(myName);
                                error=false;
                            } catch (PlayerNameNotUniqueException ex) {
                                outWriter.print(ex.getMessage());
                            } catch (NoGameExistsException ignore) {
                            }
                        }
                    }catch (NoGameExistsException e){
                        outWriter.print(e.getMessage());
                        while (error) {
                            try {
                                outWriter.print("Insert number of players");
                                int numPlayers = s.nextInt();
                                s.nextLine();
                                myID = view.BootGame(numPlayers, myName);
                                error = false;
                            } catch (UnacceptableNumOfPlayersException ex1) {
                                outWriter.print(ex1.getMessage());
                            } catch (OnlyOneGameException ex) {
                                outWriter.print(ex.getMessage());
                                try{
                                    view.joinGame(myName);
                                }catch (PlayerNameNotUniqueException e1){
                                    outWriter.print(e1.getMessage());
                                    while (error){
                                        try {
                                            outWriter.print("Insert username");
                                            myName = s.nextLine();
                                            myID = view.joinGame(myName);
                                            error=false;
                                        } catch (PlayerNameNotUniqueException e2) {
                                            outWriter.print(e2.getMessage());
                                        } catch (NoGameExistsException ignore) {
                                        }
                                    }
                                }catch (NoGameExistsException ignore){

                                }
                            }
                        }
                    }
                    view.initializeFieldBuildingHelper( myName);
                    tuiUpdater.start();
                    break;
                case "choose-goal":
                    outWriter.print("Here are your goals, choose one (1,2)");
                    Goal[] myGoals = view.showPlayerGoalOptions(myID);
                    outWriter.print(myGoals[0].getUIDescription());
                    outWriter.print(myGoals[1].getUIDescription());
                    String myGoal = s.nextLine();
                    Goal goal = Objects.equals(myGoal, "1") ? myGoals[0] : myGoals[1];
                    view.chooseGoal(myID, goal);
                    outWriter.print("Goal chosen");
                    break;
                case "play-card":
                    outWriter.print("Which card do you want to play? (1,2,3)");
                    int chosenCardI = s.nextInt();
                    s.nextLine();
                    Card chosenCard = view.showPlayerHand(myID).get(chosenCardI - 1);
                    outWriter.print("Which side? (f for front, b or any for back)");
                    boolean isChosenFront = s.nextLine().equals("f");
                    outWriter.print("Where do you want to place the selected card [" + chosenCardI + "]? (int)");
                    int chosenPositionI = s.nextInt();
                    s.nextLine();
                    List<Coordinates> availableCoordinates = view.showPlayersLegalPositions(myID);
                    Coordinates chosenPosition = availableCoordinates.get(chosenPositionI);

                    if(isChosenFront) view.playCardFront(chosenCard,chosenPosition, myID );
                    else view.playCardBack(chosenCard, chosenPosition, myID);

                    HashMap<Coordinates, Card> myField=view.getPlayersField(myName);
                    List<Coordinates> myFieldBuildingHelper=view.getFieldBuildingHelper(myName);
                    artist.show(myField,myFieldBuildingHelper);


                    outWriter.print(artist.getAsciiField(),myFieldBuildingHelper);
                    System.out.println("Press any key to continue");
                    s.next();
                    break;

                case "choose-starter-card-side":
                    outWriter.print("Which side for the starter card? (f for front, b or any for back)");
                    boolean isChosenFrontStart = s.nextLine().equals("f");
                    view.chooseStarterCardSide(isChosenFrontStart, myID);
                    outWriter.print("Starter card chosen");
                    break;

                case "draw-card-from-deck":
                    outWriter.print("What deck do you want to draw from? (0,1)");
                    int chosenDeck = s.nextInt();
                    view.drawFromDeck(myID, chosenDeck);
                    break;

                case "draw-card-visible":
                    outWriter.print("Which card do you want to draw? (0, 1, 2, 3)");
                    int chosenDrawCard = s.nextInt();
                    view.drawVisibleCard(myID,chosenDrawCard);
                    break;
                case "show-hand":
                    List<Card> hand =view.showPlayerHand(myID);
                    for(Card card:hand){
                        artist.show(card);
                    }
                    System.out.println("Press any key to continue");
                    s.nextLine();
                    break;
                case "disconnect":
                    view.closeGame(myID);
                    tuiUpdater.interrupt();
                    break;
            }
        }
    }

    /**
     * Check if the player has played the starter card
     * @return true if the starter card is on the player's field, false otherwise
     * @throws RemoteException if there are connection problem with the server
     */
    private boolean didIPlayStarterCard() throws RemoteException {
        StarterCard myStarterCard=view.getStarterCard(myID);
        return view.getPlayersField(myName).containsValue(myStarterCard);
    }

    /**
     * sorts the scoreboard in order of points
     * @param scoreboard the scoreboard to sort
     * @return a List of Entry <Username,Points>

     */
    private List<Map.Entry<String, Integer>> sortedScoreboard(Map<String, Integer> scoreboard) {
        return scoreboard.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
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


    public void printIdleUI() throws InvalidUserId, RemoteException {
        updateIdleUI();
        outWriter.clearScreen();
        outWriter.print(String.valueOf(idleUI));
    }
}
