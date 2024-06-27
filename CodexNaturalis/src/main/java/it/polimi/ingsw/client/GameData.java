package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;

import java.util.*;
/*
    Class containing data visualization of Game
 */
public class GameData {

    private boolean isGameStarted;
    private boolean isGameEnded;
    private HashMap<String, Integer> playersPoints;
    private String winner;
    private int numOfResourceCards;
    private int numOfGoldCards;
    private Reign topOfGoldsDeck;
    private Reign topOfResourcesDeck;
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
    private Map<String,String> playerToColor;
    private Map<UUID,List<String>> joinableGames =new HashMap<>();

    /**
     * Gets the map of joinable games.
     *
     * @return A map where the key is the game UUID and the value is a list of player names.
     */
    public Map<UUID, List<String>> getJoinableGames() {
        return joinableGames;
    }

    /**
     * Sets the map of joinable games.
     *
     * @param joinableGames A map where the key is the game UUID and the value is a list of player names.
     */
    public void setJoinableGames(Map<UUID, List<String>> joinableGames) {
        this.joinableGames = joinableGames;
    }

    /**
     * Gets the color assigned to a player.
     *
     * @param player The player's name.
     * @return The color assigned to the player.
     */
    public String getPlayerToColor(String player) {
        return playerToColor.get(player);
    }

    /**
     * Sets the map of players to their assigned colors.
     *
     * @param playerToColor A map where the key is the player's name and the value is their color.
     */
    public void setPlayerToColor(Map<String, String> playerToColor) {
        this.playerToColor = playerToColor;
    }

    /**
     * Sets the private chat messages for a user.
     *
     * @param user The user's name.
     * @param chat The list of chat messages.
     */
    public void setPrivateChat(String user, List<String> chat) {
        privateChats.put(user, chat);
    }

    /**
     * Gets the private chat messages for a user.
     *
     * @param user The user's name.
     * @return The list of chat messages.
     */
    public List<String> getPrivateChat(String user) {
        return privateChats.get(user);
    }

    /**
     * Gets the public chat data.
     *
     * @return The list of public chat messages.
     */
    public List<String> getChatData() {
        return chatData;
    }

    /**
     * Sets the public chat data.
     *
     * @param chatData The list of public chat messages.
     */
    public void setChatData(List<String> chatData) {
        System.out.println("chat received");
        this.chatData = chatData;
    }

    /**
     * Checks if the game has started.
     *
     * @return True if the game has started, otherwise false.
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * Sets the game start status.
     *
     * @param gameStarted True if the game has started, otherwise false.
     */
    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    /**
     * Checks if the game has ended.
     *
     * @return True if the game has ended, otherwise false.
     */
    public boolean isGameEnded() {
        return isGameEnded;
    }

    /**
     * Sets the game end status.
     *
     * @param gameEnded True if the game has ended, otherwise false.
     */
    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    /**
     * Gets the map of players and their points.
     *
     * @return A map where the key is the player's name and the value is their points.
     */
    public Map<String, Integer> getPlayersPoints() {
        return playersPoints;
    }

    /**
     * Sets the map of players and their points.
     *
     * @param playersPoints A map where the key is the player's name and the value is their points.
     */
    public void setPlayersPoints(HashMap<String, Integer> playersPoints) {
        this.playersPoints = playersPoints;
    }

    /**
     * Gets the winner of the game.
     *
     * @return The name of the winning player.
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The name of the winning player.
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Gets the number of resource cards.
     *
     * @return The number of resource cards.
     */
    public int getNumOfResourceCards() {
        return numOfResourceCards;
    }

    /**
     * Sets the number of resource cards.
     *
     * @param numOfResourceCards The number of resource cards.
     */
    public void setNumOfResourceCards(int numOfResourceCards) {
        this.numOfResourceCards = numOfResourceCards;
    }

    /**
     * Gets the number of gold cards.
     *
     * @return The number of gold cards.
     */
    public int getNumOfGoldCards() {
        return numOfGoldCards;
    }

    /**
     * Sets the number of gold cards.
     *
     * @param numOfGoldCards The number of gold cards.
     */
    public void setNumOfGoldCards(int numOfGoldCards) {
        this.numOfGoldCards = numOfGoldCards;
    }

    /**
     * Gets the player's hand of cards.
     *
     * @return The list of cards in the player's hand.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Sets the player's hand of cards.
     *
     * @param hand The list of cards to be set as the player's hand.
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * Gets the player's field of cards by username.
     *
     * @param username The player's username.
     * @return A map where the key is the coordinates and the value is the card at that position.
     */
    public Map<Coordinates, Card> getPlayerField(String username) {
        return playersField.get(username);
    }

    /**
     * Sets the map of players and their fields.
     *
     * @param playersField A map where the key is the player's username and the value is another map of coordinates to cards.
     */
    public void setPlayersField(HashMap<String, Map<Coordinates, Card>> playersField) {
        this.playersField = playersField;
    }

    /**
     * Sets the player's field of cards.
     *
     * @param username The player's username.
     * @param playersField A map where the key is the coordinates and the value is the card at that position.
     */
    public void setPlayerField(String username, Map<Coordinates, Card> playersField) {
        this.playersField.put(username, playersField);
    }

    /**
     * Removes the player's field of cards by username.
     *
     * @param username The player's username.
     */
    public void removePlayerField(String username) {
        playersField.remove(username);
    }

    /**
     * Gets the list of legal positions for placing cards.
     *
     * @return The list of legal coordinates.
     */
    public List<Coordinates> getLegalPositions() {
        return legalPositions;
    }

    /**
     * Sets the list of legal positions for placing cards.
     *
     * @param legalPositions The list of legal coordinates.
     */
    public void setLegalPositions(List<Coordinates> legalPositions) {
        this.legalPositions = legalPositions;
    }

    /**
     * Gets the field builder helper positions for a player.
     *
     * @param username The player's username.
     * @return The list of coordinates for the field builder helper.
     */
    public List<Coordinates> getFieldBuilderHelper(String username) {
        return fieldBuilderHelper.get(username);
    }

    /**
     * Sets the field builder helper positions for a player.
     *
     * @param username The player's username.
     * @param fieldBuilderHelper The list of coordinates for the field builder helper.
     */
    public void setFieldBuilderHelper(String username, List<Coordinates> fieldBuilderHelper) {
        this.fieldBuilderHelper.put(username, fieldBuilderHelper);
    }

    /**
     * Gets the list of players.
     *
     * @return The list of player names.
     */
    public List<String> getPlayersList() {
        return playersList;
    }

    /**
     * Sets the list of players.
     *
     * @param playersList The list of player names.
     */
    public void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }

    /**
     * Gets the array of public goals.
     *
     * @return An array of public goals.
     */
    public Goal[] getPublicGoals() {
        return publicGoals;
    }

    /**
     * Sets the array of public goals.
     *
     * @param publicGoals An array of public goals.
     */
    public void setPublicGoals(Goal[] publicGoals) {
        this.publicGoals = publicGoals;
    }

    /**
     * Gets the private goal of the current player.
     *
     * @return The private goal.
     */
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**
     * Sets the private goal of the current player.
     *
     * @param privateGoal The private goal.
     */
    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**
     * Gets the array of goal options.
     *
     * @return An array of goal options.
     */
    public Goal[] getGoalOptions() {
        return goalOptions;
    }

    /**
     * Sets the array of goal options.
     *
     * @param goalOptions An array of goal options.
     */
    public void setGoalOptions(Goal[] goalOptions) {
        this.goalOptions = goalOptions;
    }

    /**
     * Gets the current player's name.
     *
     * @return The current player's name.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player's name.
     *
     * @param currentPlayer The current player's name.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets the starter card.
     *
     * @return The starter card.
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Sets the starter card.
     *
     * @param starterCard The starter card.
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Gets the list of visible cards.
     *
     * @return The list of visible cards.
     */
    public List<Card> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Sets the list of visible cards.
     * @param visibleCards The list of visible cards.
     */
    public void setVisibleCards(List<Card> visibleCards) {
        this.visibleCards = visibleCards;
    }

    /**
     * Gets the top card of the golds deck.
     * @return The top card of the golds deck.
     */
    public Reign getTopOfGoldsDeck() {
        return topOfGoldsDeck;
    }

    /**
     * Sets the top card of the golds deck.
     *
     * @param topOfGoldsDeck The top card of the golds deck.
     */
    public void setTopOfGoldsDeck(Reign topOfGoldsDeck) {
        this.topOfGoldsDeck = topOfGoldsDeck;
    }

    /**
     * Gets the top card of the resources deck.
     * @return The top card of the resources deck.
     */
    public Reign getTopOfResourcesDeck() {
        return topOfResourcesDeck;
    }

    /**
     * Sets the top card of the resources deck.
     *
     * @param topOfResourcesDeck The top card of the resources deck.
     */
    public void setTopOfResourcesDeck(Reign topOfResourcesDeck) {
        this.topOfResourcesDeck = topOfResourcesDeck;
    }
}
