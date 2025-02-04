package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;

public class InGameController extends GUIController {
    @FXML
    private HBox playerListBox;
    @FXML
    private Label errorMsg;
    @FXML
    private ImageView resourceCardDeck;
    @FXML
    private ImageView goldCardDeck;
    @FXML
    private HBox goalsBox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox privateGoalBox;
    @FXML
    private ImageView visibleCard1;
    @FXML
    private ImageView visibleCard2;
    @FXML
    private ImageView visibleCard3;
    @FXML
    private ImageView visibleCard4;
    @FXML
    private HBox handBox;
    @FXML
    private VBox deckBox;
    @FXML
    private VBox scoreboardBox;
    @FXML
    private VBox chatBox;
    @FXML
    private VBox messageBox;
    @FXML
    private ImageView redCheck;
    @FXML
    private ImageView blueCheck;
    @FXML
    private ImageView yellowCheck;
    @FXML
    private ImageView greenCheck;
    @FXML
    private Button returnToMyFieldButton;
    private List<ImageView> imageChecks=new ArrayList<>();
    @FXML
    private StackPane fieldPane;
    @FXML
    private Button scoreboardButton;
    @FXML
    private Button deckButton;
    @FXML
    private Button hideScoreboardButton;
    @FXML
    private Button hideChatButton;
    @FXML
    private Button hideDeckButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button chatButton;
    @FXML
    private MenuButton chatMenu = new MenuButton();
    @FXML
    private TextArea inputChat;

    @FXML
    private VBox winModal;

    @FXML
    private Label winnerTxt;
    @FXML
    private VBox playerLeaderboardContainer;
    private Map<ImageView,Integer> handCardsId = new HashMap<>();
    private ImageView selectedCardToPlay;
    private boolean returnButtonPresent= false;
    private final PauseTransition hideError = new PauseTransition(Duration.seconds(3));
    private final Map<Integer,Coordinates> scoreMap=new HashMap<>();
    private StackPane myField;
    private Timeline overlapAnimation;
    private StackPane removedStack;
    private String chatMessage;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> updateChatTask;
    private AtomicBoolean gameInfoStop=new AtomicBoolean(true);
    private List<Coordinates> avlbPositions;

    private final GameData gd= new GameData();

    /**
     * initializes the scene
     * @author Maximilian Mangosi Giuseppe Laguardia Giorgio Mattina
     * @throws RemoteException
     */
    public void init() throws RemoteException {
        try {
            deckBox.setVisible(false);
            chatBox.setVisible(false);
            hideDeckButton.setVisible(false);
            scoreboardBox.setVisible(false);
            hideScoreboardButton.setVisible(false);
            hideChatButton.setVisible(false);
            errorMsg.setVisible(false);
            hideError.setOnFinished(event -> errorMsg.setVisible(false));
            returnToMyFieldButton.setVisible(false);
            for (String player : view.getPlayersList()){
                if(!(player.equals(myName)))
                    chatMenu.getItems().add(new MenuItem(player));
            }

            chatMenu.getItems().add(new MenuItem("Global"));

            chatMenu.setText("Global");

            initializeScoreMap();

            updatePlayersList(view.getPlayersList());
            fieldPane.setOnDragOver(this::handleDragOver);
            fieldPane.setOnDragDropped(this::handleDragDropped);
            updateHand(getHand());
            for(Node cardStack: handBox.getChildren() ){
                cardStack.setOnMouseClicked(this::flipCard);
                ((StackPane) cardStack).getChildren().getFirst().setOnDragDetected(this::handleDragDetected);
                ((StackPane) cardStack).getChildren().get(1).setOnDragDetected(this::handleDragDetected);
                ((StackPane) cardStack).getChildren().getFirst().setOnDragDone(this::handleDragDone);
                ((StackPane) cardStack).getChildren().get(1).setOnDragDone(this::handleDragDone);

            }
            int i=0;
            for (Goal g: view.getPublicGoals()){
                int id= g.getId();
                Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
                ImageView goalView= (ImageView) goalsBox.getChildren().get(i);
                goalView.setImage(goalPng);
                i++;
            }
            int id = getPrivateGoal().getId();
            Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView goalView= (ImageView) privateGoalBox.getChildren().getFirst();
            goalView.setImage(goalPng);

            resourceCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(0));
            goldCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(1));
            visibleCard1.setOnMouseClicked(mouseEvent -> drawVisibleCard(0));
            visibleCard2.setOnMouseClicked(mouseEvent -> drawVisibleCard(1));
            visibleCard3.setOnMouseClicked(mouseEvent -> drawVisibleCard(2));
            visibleCard4.setOnMouseClicked(mouseEvent -> drawVisibleCard(3));

            StarterCard stc = getStarterCard();
            id= getStarterCard().getId();
            Image scPng;
            if(stc.isFront()){
                 scPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            }else{
                 scPng= new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"));
            }
            ImageView scView=new ImageView(scPng);
            scView.setFitWidth(200);
            scView.setFitHeight(150);
            fieldPane.getChildren().add(scView);
            checkGameInfo();
        } catch (InvalidUserId | IllegalOperationException ignore) {}
    }

    /**
     * updates the players list
     * @author Giuseppe Laguardia
     * @param players
     */
    public void updatePlayersList(List<String> players)  {
        playerListBox.getChildren().clear();
        try {
            int maxWidth= players.stream().max(Comparator.comparingInt(String::length)).get().length();
            for (String p : players) {
                StackPane sp = new StackPane();
                sp.setPrefWidth(maxWidth*20);
                String color=view.getPlayerColor(p);
                String colorHex=getHex(color);
                sp.setStyle("-fx-background-color: "+colorHex);
                Label label = new Label(p);
                label.setFont(new Font("Bodoni MT",25));
                if (view.getCurrentPlayer().equals(p))
                    label.setStyle("-fx-background-color: d9be4a");
                sp.getChildren().add(label);
                playerListBox.getChildren().add(sp);
                //makes label clickable
                label.setOnMouseClicked(this::showEnemyField);
                label.setCursor(Cursor.HAND);


            }
        } catch (RemoteException e) {
            showErrorMsg("connection error");
            System.exit(1);
        }
    }

    /**
     * gets the hex code for the color
     * @author Giuseppe Laguardia
     * @param color
     * @return color
     */
    private String getHex(String color) {
        switch (color){
            case "Green" -> {return "#1a800a";}
            case "Yellow"->{ return  "#ffd500";}
            case "Blue"->{return "#4e83f5";}
            case "Red"->{return "#fc2f21";}
            case null, default -> {return "default";}

        }
    }

    /**
     * initializes the score map
     * @author Giuseppe Laguardia
     */
    private void initializeScoreMap() {
        Scanner file= new Scanner(getClass().getResourceAsStream("/scoreboard-coordinates.txt"));
        while(file.hasNext()){
            String[] line=file.nextLine().split(" ");
            int score=parseInt(line[0]);
            String[] cord=line[1].split(",");
            scoreMap.put(score,new Coordinates(parseInt(cord[0]),parseInt(cord[1])));
        }
    }


    /**
     * draws card from deck
     * @author Giuseppe Laguardia
     * @param i
     */
    private void drawFromDeck(int i) {
        try {
            view.drawFromDeck(myID,i);
            List<Card> newHand= getHand();
            updateHand(newHand);
        } catch (IOException | ClassNotFoundException | InvalidUserId e) {
           showErrorMsg("CONNECTION ERROR");
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException |DeckEmptyException e) {
            showErrorMsg(e.getMessage());
        }
    }

    /**
     * checks the game info
     * @author Giuseppe Laguardia Riccardo Lapi
     */
    private void checkGameInfo(){

        new Thread(() -> {

            List<Card> oldVisibleCards = new ArrayList<>();
            Reign oldTopResource=null;
            Reign oldTopGold=null;
            List<Card> oldHand= new ArrayList<>();
            List<String> oldPlayersList= new ArrayList<>();
            String oldCurrentPlayer = "";

            while(gameInfoStop.get()){
                try {
                    //playersList
                    List<String> newPlayersList=view.getPlayersList();
                    if(!oldPlayersList.equals(newPlayersList)){
                        Platform.runLater(()->updatePlayersList(newPlayersList));
                        oldPlayersList=newPlayersList;
                    }
                    //visible cards
                    List<Card> newVisibleCards = view.getVisibleCards();
                    if(!oldVisibleCards.equals(newVisibleCards)){
                        Platform.runLater(() -> updateVisibleCards(newVisibleCards));
                        oldVisibleCards = newVisibleCards;
                    }
                //decks

                    Reign newTopResource = view.getTopOfResourceCardDeck();
                    if(newTopResource!=oldTopResource){
                        Platform.runLater(() -> {
                            updateTopResource(newTopResource);
                        });
                        oldTopResource = newTopResource;
                    }
                    Reign newTopGold = view.getTopOfGoldCardDeck();
                    if(newTopGold!=oldTopGold){
                        Platform.runLater(() -> {
                            updateTopGold(newTopGold);
                        });
                        oldTopGold = newTopGold;
                    }
                //current player
                    String newCurrentPlayer=view.getCurrentPlayer();
                    if(!oldCurrentPlayer.equals(newCurrentPlayer)){
                        Platform.runLater(() -> updateCurrentPlayer(newCurrentPlayer));
                        oldCurrentPlayer = newCurrentPlayer;
                        Platform.runLater(this::updateScoreboard);
                    }
                    //hand
                    List<Card> newHand=getHand();
                    if (!oldHand.equals(newHand)){
                        Platform.runLater(()->updateHand(newHand));
                        oldHand=newHand;
                    }

                    //game finished
                    if(view.isGameEnded()){
                        Platform.runLater(this::onGameFinished);
                        break;
                    }
                } catch (RemoteException | InvalidUserId | IllegalOperationException ignore) {}

            }
        }).start();
    }

    /**
     * checks if the game is finished
     * @author Riccardo Lapi
     */
    private void onGameFinished()  {
        winModal.setVisible(true);
        showScoreboard();

        try {
            winnerTxt.setText("Winner: " + view.getWinner());

            Map<String, Integer> playerPoints = view.getPlayersPoints();

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(playerPoints.entrySet());
            sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            for (int i = 0; i < sortedEntries.size(); i++) {
                Map.Entry<String, Integer> entry = sortedEntries.get(i);
                String player = entry.getKey();
                Integer points = entry.getValue();
                addPlayerInWinModal(i, player, points);
            }

        } catch (RemoteException e) {
           showErrorMsg("Connection error");
           System.exit(1);
        }
    }

    /**
     * adds the player to winning window with relative points
     * @author Riccardo Lapi
     * @param index
     * @param username
     * @param points
     */
    private void addPlayerInWinModal(int index,String username, int points){
        Text text1 = new Text(Integer.toString(index + 1) + "°");
        text1.setFont(new Font(26));

        Text text2 = new Text(username);
        text2.setFont(new Font(18));

        Text text3 = new Text(Integer.toString(points));
        text3.setFont(new Font(22));

        HBox hbox = new HBox(12);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMaxWidth(400);
        hbox.setBackground(new Background(new BackgroundFill(Color.web("#e5a78a"), new CornerRadii(12), Insets.EMPTY)));

        // Create Regions to use for spacing
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(spacer2, javafx.scene.layout.Priority.ALWAYS);

        hbox.getChildren().addAll(text1, spacer1, text2, spacer2, text3);
        playerLeaderboardContainer.getChildren().add(hbox);
    }

    /**
     * updates the scoreboard
     * @author Giuseppe Laguardia
     */
    private void updateScoreboard(){
        try{
            //reset imageView list to handle disconnections
            imageChecks= new ArrayList<>();
            Map<String, Integer> playersPoints = view.getPlayersPoints();
            if(playersPoints!=null) {
                for (Map.Entry<String, Integer> scoreboard : playersPoints.entrySet()) {
                    String playerColor = view.getPlayerColor(scoreboard.getKey());
                    switch (playerColor) {
                        case "Red" -> {
                            redCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                            redCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                            redCheck.setVisible(true);
                            imageChecks.add(redCheck);
                        }
                        case "Blue" -> {
                            blueCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                            blueCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                            blueCheck.setVisible(true);
                            imageChecks.add(blueCheck);
                        }
                        case "Yellow" -> {
                            yellowCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                            yellowCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                            yellowCheck.setVisible(true);
                            imageChecks.add(yellowCheck);
                        }
                        case "Green" -> {
                            greenCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                            greenCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                            greenCheck.setVisible(true);
                            imageChecks.add(greenCheck);
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            showErrorMsg("connection error");
            System.exit(1);
        }

    }

    /**
     * updates the visible cards
     * @author Giuseppe Laguardia Riccardo Lapi Giorgio Mattina
     * @param newCards
     */
    private void updateVisibleCards(List<Card> newCards) {
        try {
            Image img1 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(0).getId() + ".png"));
            visibleCard1.setImage(img1);


            Image img2 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(1).getId() + ".png"));
            visibleCard2.setImage(img2);


            Image img3 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(2).getId() + ".png"));
            visibleCard3.setImage(img3);

            Image img4 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(3).getId() + ".png"));
            visibleCard4.setImage(img4);
        } catch (IndexOutOfBoundsException ignore) {

        }
    }

    /**
     * updates the current player color id
     * @author Riccardo Lapi
     * @param newCurrentPlayer
     */
    private void updateCurrentPlayer(String newCurrentPlayer){
        playerListBox.getChildren().forEach(p -> {
           Label label = (Label) ((StackPane)p).getChildren().getFirst();

           if(newCurrentPlayer.equals(label.getText())){
               label.setStyle("-fx-background-color: d9be4a");
           }else{
               label.setStyle("-fx-background-color: 0");
           }
        });
    }

    /**
     * draws the visible card
     * @author Giuseppe Laguardia
     * @param choice
     */
    public void drawVisibleCard(int choice)  {
        try {
            view.drawVisibleCard(myID,choice);
            updateHand(getHand());
        } catch (IOException | InvalidUserId | ClassNotFoundException e) {
           showErrorMsg("CONNECTION ERROR");
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException |
                 DeckEmptyException e) {
            showErrorMsg(e.getMessage());
        }
    }

    /**
     * updates the top resource card
     * @author Giuseppe Laguardia
     * @param newTop
     */
    private  void updateTopResource(Reign newTop){
        Image img = new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
        if (newTop!=null) {
            switch (newTop) {
                case MUSHROOM -> img = new Image(getClass().getResourceAsStream("/CardsBack/1.png"));
                case PLANTS -> img = new Image(getClass().getResourceAsStream("/CardsBack/17.png"));
                case ANIMAL -> img = new Image(getClass().getResourceAsStream("/CardsBack/23.png"));
                case BUG -> img = new Image(getClass().getResourceAsStream("/CardsBack/36.png"));
                default -> img = new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
            }
        }
        resourceCardDeck.setImage(img);
    }

    /**
     * updates the top gold card
     * @author Giuseppe Laguardia
     * @param newTop
     */
    private void updateTopGold(Reign newTop){
        Image img = new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
        if (newTop!=null) {
            switch (newTop) {
                case MUSHROOM -> img= new Image(getClass().getResourceAsStream("/CardsBack/41.png"));
                case PLANTS -> img= new Image(getClass().getResourceAsStream("/CardsBack/57.png"));
                case ANIMAL -> img= new Image(getClass().getResourceAsStream("/CardsBack/63.png"));
                case BUG -> img= new Image(getClass().getResourceAsStream("/CardsBack/76.png"));

            }
        }
        goldCardDeck.setImage(img);

    }

    /**
     * updates the players hand
     * @author Giuseppe Laguardia Maximilian Mangosi
     * @param newHand
     */
    private void updateHand(List<Card> newHand){
        if (newHand!=null) {
            for (Card card : newHand) {
                int id = card.getId();
                Image frontPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"), 350, 200, true, false);
                Image backPng = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"), 350, 200, true, false);
                StackPane cardStack;

                try {
                    cardStack = (StackPane) handBox.getChildren().get(newHand.indexOf(card));
                } catch (IndexOutOfBoundsException e) {
                    cardStack = removedStack;
                    handBox.getChildren().add(newHand.indexOf(card), cardStack);
                }
                ImageView backView = (ImageView) cardStack.getChildren().getFirst();
                ImageView frontView = (ImageView) cardStack.getChildren().get(1);
                backView.setImage(backPng);
                frontView.setImage(frontPng);
                frontView.setVisible(true);
                backView.setVisible(false);

            }
        }
    }

    /**
     * places the card
     * @author Giorgio Mattina
     * @param position
     */
    public void placeCard( Coordinates position){
         ImageView newCardImage = selectedCardToPlay;
         newCardImage.setFitWidth(200);
         newCardImage.setFitHeight(150);
         newCardImage.setTranslateX(position.x* 155.5);
         newCardImage.setTranslateY(position.y * 79.5);
         newCardImage.setOnMouseClicked(mouseEvent -> handleClickCard(mouseEvent,position));
         fieldPane.getChildren().add(newCardImage);
         handBox.getChildren().remove(selectedCardToPlay);
    }

    /**
     * shows the deck
     * @author Giuseppe Laguardia
     */
    public void showDeck(){
        deckButton.setVisible(false);
        deckBox.setVisible(true);
        deckBox.setLayoutX(1379);
        hideDeckButton.setVisible(true);

    }

    /**
     * shows the scoreboard
     * @author Giuseppe Laguardia Maximilian Mangosi
     */
    public void showScoreboard(){
        updateScoreboard();
        handleCheckOverlap();
        scoreboardButton.setVisible(false);
        chatButton.setVisible(false);
        scoreboardBox.setVisible(true);
        scoreboardBox.setLayoutX(0);
        hideScoreboardButton.setVisible(true);

    }

    /**
     * handles the overlap between cards
     * @author Giuseppe Laguardia Giorgio Mattina
     */
    private void handleCheckOverlap() {
        List<ImageView> checksOverlapping = imageChecks.stream().filter(this::overlaps).toList();
        try {
            List<Node> scoreboard= ((StackPane) checksOverlapping.getFirst().getParent()).getChildren();
            AtomicInteger i= new AtomicInteger();

            if(checksOverlapping.isEmpty())
                return;
            //Every second puts the checks that overlaps another one on the top of the stack pane
            overlapAnimation = new Timeline(new KeyFrame(Duration.seconds(2),actionEvent ->  {
                 ImageView check=checksOverlapping.get(i.get());
                 scoreboard.remove(check);
                 scoreboard.addLast(check);
                 i.set((i.get() + 1) % checksOverlapping.size());
            }));
            overlapAnimation.setCycleCount(Timeline.INDEFINITE);// animation cycles until stopped by pressing the button that closes the scoreboard
            overlapAnimation.play();
        } catch (NoSuchElementException ignore) {

        }

    }
    /**
     * handles the overlap between cards
     * @author Giuseppe Laguardia Giorgio Mattina
     */
    private boolean overlaps(ImageView image) {
        for (ImageView img: imageChecks){
            if(!image.equals(img)){
                if(image.getX()==img.getX() && image.getY()==img.getY())
                    return true;
            }
        }
        return false;
    }
    /**
     * hides the deck
     * @author Giuseppe Laguardia
     */
    public void hideDeck(){
        hideDeckButton.setVisible(false);
        deckBox.setLayoutX(-541);
        deckBox.setVisible(false);
        deckButton.setVisible(true);
    }
    /**
     * hides the scoreboard
     * @author Giuseppe Laguardia Maximilian Mangosi
     */
    public void hideScoreboard(){
        hideScoreboardButton.setVisible(false);
        imageChecks.forEach(i->i.setVisible(false));
        scoreboardBox.setLayoutX(-571);
        scoreboardBox.setVisible(false);
        scoreboardButton.setVisible(true);
        chatButton.setVisible(true);
        overlapAnimation.stop();
    }
    /**
     * flips a card
     * @author Giuseppe Laguardia
     */
    public void flipCard(MouseEvent e){
        StackPane cardPane= (StackPane) e.getSource();
        for( Node cardView: cardPane.getChildren()){
            cardView.setVisible(!cardView.isVisible());
        }
    }

    /**
     * handles the click of a card
     * @author Giuseppe Laguardia Giorgio Mattina
     * @param event
     * @param coordinates
     */
    private void handleClickCard (MouseEvent event,Coordinates coordinates){

        double clickX = event.getX();
        double clickY = event.getY();
        if(clickX<100 && clickY <75){
            placeCard(new Coordinates(coordinates.x-1,coordinates.y-1 ));
        } else if (clickX>=100 && clickY <75) {
            placeCard(new Coordinates(coordinates.x+1,coordinates.y-1 ));
        } else if (clickX<100 && clickY >=75) {
            placeCard(new Coordinates(coordinates.x-1,coordinates.y+1 ));
        } else {
            placeCard(new Coordinates(coordinates.x+1,coordinates.y+1 ));
        }

    }

    /**
     * quits the game
     * @author Giuseppe Laguardia
     * @param e
     */
    public void quitGame(ActionEvent e ){
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"If you quit you can't come back. Are you Sure?", ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> response= alert.showAndWait();
        response.ifPresent(r->{
            if(r==ButtonType.OK) {
                try {
                    view.closeGame(myID);
                    gameInfoStop.set(false);
                    changeScene("lobby-view.fxml",e);
                } catch (ClassNotFoundException | IOException ex) {
                   showErrorMsg("connection error");
                   System.exit(1);
                }
            }
        });

    }

    /**
     * shows the enemy field
     * @author Giorgio Mattina
     * @param event
     */
    private void showEnemyField(MouseEvent event)  {
        AnchorPane anchorParent = (AnchorPane) fieldPane.getParent();
        //fetch the other player's hand
        Label l = (Label) event.getSource();
        String username = l.getText();
        if(!username.equals(myName)){//if the top pane is not mine,
            List<Node> children = anchorParent.getChildren();
            if(children.size()>=2)
               children.remove(1);
            try {


                Map<Coordinates, Card> field = view.getPlayersField(username);
                List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);


                //saves the old field and sets it invisible
                fieldPane.setVisible(false);
                //build a new field
                StackPane newFieldPane = new StackPane();
                anchorParent.getChildren().add(newFieldPane);
                newFieldPane.setPrefWidth(5128);
                newFieldPane.setPrefHeight(2964);
                newFieldPane.setMinWidth(Region.USE_PREF_SIZE);
                newFieldPane.setMinHeight(Region.USE_PREF_SIZE);
                newFieldPane.setMaxHeight(Region.USE_PREF_SIZE);
                newFieldPane.setMaxWidth(Region.USE_PREF_SIZE);
                newFieldPane.setScaleX(1);
                newFieldPane.setScaleZ(1);


                //adds the enemy card
                int id;
                for (Coordinates c :fieldBuildingHelper){
                    //load the image
                    id = field.get(c).getId();
                    Image i;
                    if(field.get(c).isFront()) {
                        i = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
                    }else{
                        i = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"));
                    }

                    ImageView newImageView = new ImageView(i);
                    newFieldPane.getChildren().add(newImageView);
                    newImageView.setFitWidth(200);
                    newImageView.setFitHeight(150);
                    newImageView.setTranslateX(c.x*152.5);
                    newImageView.setTranslateY(c.y*-85);
                }
                if(!returnButtonPresent) {
                    returnToMyFieldButton.setVisible(true);
                    returnButtonPresent=true;
                }
            }catch (RemoteException e){
                showErrorMsg("Connection error");
                System.exit(1);
            } catch (IllegalOperationException ignore) {}
        }

    }

    /**
     * returns to the player field
     * @author Giorgio Mattina
     * @param event
     */
    @FXML
    private void returnToMyField(ActionEvent event){
        AnchorPane oldCenter = (AnchorPane) fieldPane.getParent();
        oldCenter.getChildren().removeLast();
        fieldPane.setVisible(true);
        returnToMyFieldButton.setVisible(false);
        returnButtonPresent=false;
    }

    /**
     * handles drag on hand
     * @author Giorgio Mattina
     * @param event
     */
    private void handleDragDetected(MouseEvent event){

        try {
            avlbPositions=getPlayersLegalPositions();
            Dragboard db=((Node) event.getSource()).startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cpc = new ClipboardContent();
            cpc.putImage(((ImageView)event.getSource()).getImage());
            db.setContent(cpc);
            selectedCardToPlay= (ImageView) event.getSource();
        } catch (RemoteException e) {
            showErrorMsg("Connection error");
        } catch (InvalidUserId e) {
            showErrorMsg(e.getMessage());
        } catch (IllegalOperationException ignore) {
        }

    }
    /**
     * handle drag Over field
     * @author Giorgio Mattina
     * @param e
     */
    private void handleDragOver(DragEvent e)  {

        double hover_x = e.getX()-2564;
        double hover_y = e.getY()-1482;

        Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/152.5), (int) -Math.round(hover_y/85));

        if(e.getDragboard().hasImage() && avlbPositions.contains(newCoordinate) ){
            e.acceptTransferModes(TransferMode.MOVE);
        }
        e.consume();


    }
    /**
     * handle drop on field
     * @author Giorgio Mattina
     * @param e
     */
    private void  handleDragDropped(DragEvent e){
        try{
            String p= view.getCurrentPlayer();
            Dragboard db = e.getDragboard();
            double hover_x = e.getX()-2564;
            double hover_y = e.getY()-1482;
            Coordinates newStandardCoordinate = new Coordinates((int) Math.round(hover_x/152.5), (int) -Math.round(hover_y/85));
            StackPane parent=(StackPane) selectedCardToPlay.getParent();
            try {
                if(parent.getChildren().getFirst().isVisible()){
                    view.playCardBack(getHand().get(handBox.getChildren().indexOf(parent)),newStandardCoordinate,myID);
                }else{
                    view.playCardFront(getHand().get(handBox.getChildren().indexOf(parent)),newStandardCoordinate,myID);
                }
                Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/152.5), (int) Math.round(hover_y/85));
                ImageView newCardImage =new ImageView(db.getImage());
                newCardImage.setFitWidth(200);
                newCardImage.setFitHeight(150);

                fieldPane.getChildren().add(newCardImage);
                newCardImage.setTranslateX(newCoordinate.x*152.5);
                newCardImage.setTranslateY(newCoordinate.y * 85);
                e.setDropCompleted(true);
            } catch (HandNotFullException | IsNotYourTurnException | RequirementsNotMetException |
                     IllegalPositionException | IllegalOperationException | InvalidCardException | IOException |
                     ClassNotFoundException ex) {
                showErrorMsg(ex.getMessage());

            }


        }catch (RemoteException | InvalidUserId ex){
            showErrorMsg(ex.getMessage());
            System.exit(1);
        }


    }
    /**
     * handel drag and drop
     * @author Giorgio Mattina
     * @param e
     */
    private void handleDragDone(DragEvent e ){
        if (e.getTransferMode() == TransferMode.MOVE) {
            removedStack = (StackPane) ((ImageView) e.getSource()).getParent();
            handBox.getChildren().remove(removedStack);
        }

        e.consume();
    }

    /**
     * shows an error message
     * @author Giuseppe Laguardia
     * @param message
     */
    private void showErrorMsg(String message) {
        errorMsg.setText(message.toUpperCase(Locale.ROOT));
        errorMsg.setVisible(true);
        hideError.play();

    }

    /**
     * Loads the chat for global and private usage
     * @author Maximilian Mangosi
     * @param user
     */
    public void loadChat(String user){
        messageBox.getChildren().clear();
        chatMenu.setText(user);
        List <String> chatList;
        try{
            if (user.equals("Global")){
                    chatList=view.getChatList();
            }else{
                chatList=getPrivateChat(user);
            }
            for (String message : chatList){
                Text text = new Text(message);
                text.setWrappingWidth(500);
                int i=message.indexOf(":");
                String sender= message.substring(0,i-1);
                String color=getHex(view.getPlayerColor(sender));
                text.setFont(new Font("Bodoni MT",25));
                text.setStyle("-fx-fill: "+color+";" +
                        "-fx-font-weight: Bold;");
                messageBox.getChildren().add(text);

            }
        } catch (RemoteException e) {
            showErrorMsg("Connection Error");
            System.exit(1);
        }
    }

    /**
     * updates the chat using threads
     * @author Maxiimilian Mangosi
     */
    public void updateChat(){
        Platform.runLater(()->loadChat(chatMenu.getText()));
    }

    /**
     * shows the chat
     * @author Maxiimilian Mangosi
     */
    public void showChat(){
        scoreboardButton.setVisible(false);
        hideChatButton.setVisible(true);
        chatButton.setVisible(false);
        chatBox.setVisible(true);
        chatBox.setLayoutX(0);
        for (MenuItem item : chatMenu.getItems()){
            item.setOnAction(actionEvent->loadChat(item.getText()));
        }

        updateChatTask = scheduler.scheduleAtFixedRate(this::updateChat, 0, 100, TimeUnit.MILLISECONDS);
    }
    /**
     * hides the chat
     * @author Maxiimilian Mangosi
     */
    public void hideChat(){
        hideScoreboardButton.setVisible(false);
        chatBox.setLayoutX(-1080);
        chatBox.setVisible(false);
        hideChatButton.setVisible(false);
        scoreboardButton.setVisible(true);
        chatButton.setVisible(true);

        updateChatTask.cancel(true);
    }
    /**
     * text prompt for the chat
     * @author Maxiimilian Mangosi
     */
    @FXML
    private void onTextChat(){
        chatMessage = inputChat.getText();
    }
    /**
     * handles the enter key in the chat box
     * @author Maxiimilian Mangosi
     */
    @FXML
    private void onKeyEnter(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER){
            sendMessage();
            updateChat();
        }
    }
    /**
     * sends the message
     * @author Maxiimilian Mangosi
     */
    public void sendMessage(){
        try {
            if (chatMenu.getText().equals("Global")) {
                view.sendChatMessage(myName + " : " + chatMessage);
            } else {
                view.sendPrivateMessage(chatMenu.getText(), myName + " : " + chatMessage, myID);
            }
        } catch (IOException | ClassNotFoundException e) {
            showErrorMsg("Connection error");
            System.exit(1);
        } catch (IllegalOperationException e) {
            showErrorMsg(e.getMessage());
        }
        inputChat.clear();
    }
}
