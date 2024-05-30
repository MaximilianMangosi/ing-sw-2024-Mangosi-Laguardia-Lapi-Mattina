package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {

    private boolean isGameStarted;
    private boolean isGameEnded;
    private HashMap<String, Integer> playersPoints;
    private String winner;
    private int numOfResourceCards;
    private int numOfGoldCards;
    private List<Card> hand;
    private HashMap<String, Map<Coordinates, Card>> playersField=new HashMap<String, Map<Coordinates, Card>>();
    private List<Coordinates> legalPositions;
    private HashMap<String,List<Coordinates>> fieldBuilderHelper=new HashMap<>();
    private List<String> playersList;
    private Goal[] publicGoals;
    private Goal[] goalOptions;
    private Goal privateGoal;
    private String currentPlayer;
    private StarterCard starterCard;
    private List<Card> visibleCards;
    private List<String> chatData = new ArrayList<>(250);
    private Map<String,List<String>> privateChats = new HashMap<>();





    public void setPrivateChat(String user,List<String> chat){
        privateChats.put(user,chat);
    }
    public List<String> getPrivateChat(String user){
        return privateChats.get(user);
    }

    public  List<String> getChatData(){
        return  chatData;
    }

    public void setChatData(List<String> chatData){
        System.out.println("chat received");
        this.chatData = chatData;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }
    public Map<String, Integer> getPlayersPoints() {
        return playersPoints;
    }

    public void setPlayersPoints(HashMap<String, Integer> playersPoints) {
        this.playersPoints = playersPoints;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getNumOfResourceCards() {
        return numOfResourceCards;
    }

    public void setNumOfResourceCards(int numOfResourceCards) {
        this.numOfResourceCards = numOfResourceCards;
    }

    public int getNumOfGoldCards() {
        return numOfGoldCards;
    }

    public void setNumOfGoldCards(int numOfGoldCards) {
        this.numOfGoldCards = numOfGoldCards;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Map<Coordinates, Card> getPlayerField(String username) {
        return playersField.get(username);
    }

    public void setPlayersField(HashMap<String, Map<Coordinates, Card>> playersField) {
        this.playersField = playersField;
    }
    public void setPlayerField(String username, Map<Coordinates, Card> playersField) {
        this.playersField.put(username,playersField);
    }
    public void removePlayerField(String username){
        playersField.remove(username);
    }
    public List<Coordinates> getLegalPositions() {
        return legalPositions;
    }

    public void setLegalPositions(List<Coordinates> legalPosition) {
        this.legalPositions = legalPosition;
    }

    public List<Coordinates> getFieldBuilderHelper(String username) {
        return fieldBuilderHelper.get(username);
    }

    public void setFieldBuilderHelper(String username,List<Coordinates> fieldBuilderHelper) {
        this.fieldBuilderHelper.put(username,fieldBuilderHelper);
    }

    public List<String> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }

    public Goal[] getPublicGoals() {
        return publicGoals;
    }

    public void setPublicGoals(Goal[] publicGoals) {
        this.publicGoals = publicGoals;
    }

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    public Goal[] getGoalOptions() {
        return goalOptions;
    }

    public void setGoalOptions(Goal[] goalOptions) {
        this.goalOptions = goalOptions;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public List<Card> getVisibleCards() {
        return visibleCards;
    }

    public void setVisibleCards(List<Card> visibleCards) {
        this.visibleCards = visibleCards;
    }


}
