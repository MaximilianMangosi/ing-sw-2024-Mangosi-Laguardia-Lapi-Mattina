package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.beans.EventHandler;
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
    private final AtomicBoolean stopHandleOverlap = new AtomicBoolean(false);
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
    private Map<ImageView,Integer> handCardsId = new HashMap<>();
    private EventHandler playCardEvent;
    private ImageView selectedCardToPlay;
    private boolean returnButtonPresent= false;
    private final PauseTransition hideError = new PauseTransition(Duration.seconds(3));
    private final Map<Integer,Coordinates> scoreMap=new HashMap<>();
    private StackPane myField;
    private Timeline overlapAnimation;
    private StackPane removedStack;
    private String chatMessage;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture updateChatTask;
    private List<Coordinates> avlbPositions;


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
        } catch (InvalidUserId ignore) {}
    }

    public void updatePlayersList(List<String> players)  {
        playerListBox.getChildren().clear();
        try {
            int maxWidth= players.stream().max(Comparator.comparingInt(String::length)).get().length();
            for (String p : players) {
                StackPane sp = new StackPane();
                sp.setPrefWidth(maxWidth*20);
                String color=view.getPlayerColor(p);
                sp.setStyle("-fx-background-color: "+color);
                Label label = new Label(p);
                label.setFont(new Font("Bodoni MT Condensed", 40));
                if (view.getCurrentPlayer().equals(p))
                    label.setStyle("-fx-background-color: d9be4a");
                sp.getChildren().add(label);
                playerListBox.getChildren().add(sp);
                //makes label clickable
                label.setOnMouseClicked(this::showEnemyField);
                label.setCursor(Cursor.HAND);


            }
        } catch (RemoteException e) {
            showErrorMsg("connectior error");
            System.exit(1);
        }
    }

    private void initializeScoreMap() {

        Scanner file= new Scanner(getClass().getResourceAsStream("/scoreboard-coordinates.txt"));
        while(file.hasNext()){
            String[] line=file.nextLine().split(" ");
            int score=parseInt(line[0]);
            String[] cord=line[1].split(",");
            scoreMap.put(score,new Coordinates(parseInt(cord[0]),parseInt(cord[1])));
        }
    }



    private void drawFromDeck(int i) {
        System.out.println("deck clicked");
        try {
            view.drawFromDeck(myID,i);
            List<Card> newHand= getHand();
            updateHand(newHand);
        } catch (IOException e) {
           showErrorMsg("CONNECTION ERROR");
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException |DeckEmptyException e) {
            showErrorMsg(e.getMessage());

        }catch (InvalidGoalException | InvalidUserId | HandNotFullException | NoGameExistsException |
                RequirementsNotMetException | UnacceptableNumOfPlayersException | OnlyOneGameException |
                PlayerNameNotUniqueException | IllegalPositionException | InvalidCardException |
                ClassNotFoundException | InvalidGameID ignore){}
    }
//JL
    private void checkGameInfo(){

        new Thread(() -> {

            List<Card> oldVisibleCards = new ArrayList<>();
            Reign oldTopResource=null;
            Reign oldTopGold=null;
            List<Card> oldHand= new ArrayList<>();
            List<String> oldPlayersList= new ArrayList<>();
            String oldCurrentPlayer = "";

            while(true){
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
                    }
                    //hand
                    List<Card> newHand=getHand();
                    if (!oldHand.equals(newHand)){
                        Platform.runLater(()->updateHand(newHand));
                        oldHand=newHand;
                    }

                    //game finished
                    if(view.isGameEnded()){
                     //   changeScene("victory.xml", );
                    }
                } catch (RemoteException e) {
                    showErrorMsg("CONNECTION ERROR");
                    System.exit(1);
                } catch (InvalidUserId ignore) {
                }

            }
        }).start();
    }
    private void updateScoreboard(){
        try{
            //reset imageView list to handle disconnections
            imageChecks= new ArrayList<>();
            for (Map.Entry<String,Integer> scoreboard:view.getPlayersPoints().entrySet()){
                String playerColor = view.getPlayerColor(scoreboard.getKey());
                switch (playerColor){
                    case "Red"->{
                        System.out.println("Red");
                        redCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                        redCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                        redCheck.setVisible(true);
                        imageChecks.add(redCheck);
                    }
                    case "Blue"->{
                        System.out.println("B");
                        blueCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                        blueCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                        blueCheck.setVisible(true);
                        imageChecks.add(blueCheck);
                    }
                    case "Yellow"->{
                        System.out.println("Y");
                       yellowCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                        yellowCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                        yellowCheck.setVisible(true);
                        imageChecks.add(yellowCheck);
                    }
                    case"Green"->{
                        System.out.println("G");
                        greenCheck.setTranslateX(scoreMap.get(scoreboard.getValue()).x);
                        greenCheck.setTranslateY(scoreMap.get(scoreboard.getValue()).y);
                        greenCheck.setVisible(true);
                        imageChecks.add(greenCheck);
                    }
                }
            }
        } catch (RemoteException e) {
            showErrorMsg("connection error");
            System.exit(1);
        }

    }

    private void updateVisibleCards(List<Card> newCards) {
        Image img1 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(0).getId() + ".png"));
        visibleCard1.setImage(img1);


        Image img2 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(1).getId() + ".png"));
        visibleCard2.setImage(img2);


        Image img3 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(2).getId() + ".png"));
        visibleCard3.setImage(img3);

        Image img4 = new Image(getClass().getResourceAsStream("/CardsFront/" + newCards.get(3).getId() + ".png"));
        visibleCard4.setImage(img4);
    }

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
    public void drawVisibleCard(int choice)  {
        try {
            view.drawVisibleCard(myID,choice);
            updateHand(getHand());
        } catch (IOException e) {
           showErrorMsg("CONNECTION ERROR");
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException e) {
           showErrorMsg(e.getMessage());
        }catch (InvalidGoalException | InvalidUserId | HandNotFullException | NoGameExistsException |
                RequirementsNotMetException | UnacceptableNumOfPlayersException | OnlyOneGameException |
                PlayerNameNotUniqueException | IllegalPositionException | InvalidCardException | DeckEmptyException |
                ClassNotFoundException | InvalidGameID ignore){
        }
    }
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
    private void updateHand(List<Card> newHand){
        for(Card card: newHand){
            int id = card.getId();
            Image frontPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"),275,193,false,false);
            Image backPng = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"),275,193,false,false);
            StackPane cardStack;

            try {
                cardStack = (StackPane) handBox.getChildren().get(newHand.indexOf(card));
            } catch (IndexOutOfBoundsException e) {
                cardStack=removedStack;
                handBox.getChildren().add(newHand.indexOf(card),cardStack);
            }
            ImageView backView = (ImageView) cardStack.getChildren().getFirst();
            ImageView frontView = (ImageView) cardStack.getChildren().get(1);
            backView.setImage(backPng);
            frontView.setImage(frontPng);
            frontView.setVisible(true);
            backView.setVisible(false);

        }
    }
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
    public void showDeck(){
        deckButton.setVisible(false);
        deckBox.setVisible(true);
        deckBox.setLayoutX(1379);
        hideDeckButton.setVisible(true);

    }
    public void showScoreboard(){
        updateScoreboard();
        handleCheckOverlap();
        scoreboardButton.setVisible(false);
        chatButton.setVisible(false);
        scoreboardBox.setVisible(true);
        scoreboardBox.setLayoutX(0);
        hideScoreboardButton.setVisible(true);

    }

    private void handleCheckOverlap() {
        List<ImageView> checksOverlapping = imageChecks.stream().filter(this::overlaps).toList();
        System.out.println(checksOverlapping.size());
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

    private boolean overlaps(ImageView image) {
        for (ImageView img: imageChecks){
            if(!image.equals(img)){
                if(image.getX()==img.getX() && image.getY()==img.getY())
                    return true;
            }
        }
        return false;
        //return imageChecks.stream().filter(i->!i.equals(image)).map(i->new Coordinates((int) i.getX(), (int) i.getY())).noneMatch(c->c.x==image.getX() && c.y==image.getX());

    }

    public void hideDeck(){
        hideDeckButton.setVisible(false);
        deckBox.setLayoutX(-541);
        deckBox.setVisible(false);
        deckButton.setVisible(true);
    }
    public void hideScoreboard(){
        hideScoreboardButton.setVisible(false);
        imageChecks.forEach(i->i.setVisible(false));
        scoreboardBox.setLayoutX(1920);
        scoreboardBox.setVisible(false);
        scoreboardButton.setVisible(true);
        chatButton.setVisible(true);
        overlapAnimation.stop();
    }
    public void flipCard(MouseEvent e){
        StackPane cardPane= (StackPane) e.getSource();
        for( Node cardView: cardPane.getChildren()){
            cardView.setVisible(!cardView.isVisible());
        }
    }
    private void handleClickCard (MouseEvent event,Coordinates coordinates){
        ImageView cardImage = (ImageView) event.getSource();
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
    public void quitGame(ActionEvent e ){
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"If you quit you can't come back. Are you Sure?", ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> response= alert.showAndWait();
        response.ifPresent(r->{
            if(r==ButtonType.OK) {
                try {
                    view.closeGame(myID);
                    changeScene("New-hello-view.fxml",e);
                } catch (ClassNotFoundException | IOException ex) {
                   showErrorMsg("connection error");
                   System.exit(1);
                }
            }
        });

    }
    //
    private void showEnemyField(MouseEvent event)  {
        Pane oldCenter = (Pane) borderPane.getCenter();
        //fetch the other player's hand
        Label l = (Label) event.getSource();
        String username = l.getText();
        if(!username.equals(myName)){//if the top pane is not mine,
            List<Node> children = oldCenter.getChildren();
            if(oldCenter.getChildren().size()>=2)
                oldCenter.getChildren().remove(1);
            try {


                Map<Coordinates, Card> field = view.getPlayersField(username);
                List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);


                //saves the old field and sets it invisible
                fieldPane.setVisible(false);

                //build a new field
                StackPane newFieldPane = new StackPane();
                newFieldPane.setPrefWidth(2408);
                newFieldPane.setPrefHeight(1610);
                newFieldPane.setLayoutX(-240);
                newFieldPane.setLayoutY(-390);

                AnchorPane newAnchor = new AnchorPane(newFieldPane);
                newAnchor.setPrefWidth(2400);
                newAnchor.setPrefHeight(1566);
                ScrollPane newScrollPane = new ScrollPane(newAnchor);
                newScrollPane.setPrefWidth(200);
                newScrollPane.setPrefHeight(200);

                StackPane newHugeStackPane = new StackPane(newScrollPane);
                newHugeStackPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                newHugeStackPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
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
                    newImageView.setTranslateX(c.x*155.5);
                    newImageView.setTranslateY(c.y*-79.5);
                }
                if(!returnButtonPresent) {
                    Button returnToMyFieldButton = new Button("Return");
                    playerListBox.getChildren().add(returnToMyFieldButton);
                    returnToMyFieldButton.setOnMouseClicked(MouseEvent -> returnToMyField(MouseEvent));
                    returnButtonPresent=true;
                }
                ((Pane) borderPane.getCenter()).getChildren().add(newHugeStackPane);



            }catch (RemoteException e){
                //TODO ERROR MESSAGE
            }
        }

    }
    private void returnToMyField(MouseEvent event){
        Pane oldCenter = (Pane) borderPane.getCenter();
        oldCenter.getChildren().removeLast();
        fieldPane.setVisible(true);
        playerListBox.getChildren().remove(event.getSource());
        returnButtonPresent=false;
    }
    private void handleDragDetected(MouseEvent event){

        try {
            avlbPositions=getPlayersLegalPositions();
            Dragboard db=((Node) event.getSource()).startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cpc = new ClipboardContent();
            cpc.putImage(((ImageView)event.getSource()).getImage());
            db.setContent(cpc);
            selectedCardToPlay= (ImageView) event.getSource();
        } catch (RemoteException e) {
            showErrorMsg(e.getMessage());
        } catch (InvalidUserId e) {
            showErrorMsg(e.getMessage());
        }

    }
    private void handleDragOver(DragEvent e)  {
            double hover_x = e.getX()-1204;
            double hover_y = e.getY()-805;
            Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/155.5), (int) -Math.round(hover_y/79.5));
//            System.out.println((int) Math.round(hover_x/155.5));
//            System.out.println( (int) Math.round(hover_y/79.5));


            if(e.getDragboard().hasImage() && avlbPositions.contains(newCoordinate) ){
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();


    }
    private void  handleDragDropped(DragEvent e){
        try{
            String p= view.getCurrentPlayer();
            Dragboard db = e.getDragboard();
            double hover_x = e.getX()-1204;
            double hover_y = e.getY()-805;
            Coordinates newStandardCoordinate = new Coordinates((int) Math.round(hover_x/155.5), (int) -Math.round(hover_y/79.5));
            StackPane parent=(StackPane) selectedCardToPlay.getParent();
            if(parent.getChildren().getFirst().isVisible()){
                view.playCardBack(getHand().get(handBox.getChildren().indexOf(parent)),newStandardCoordinate,myID);
            }else{
                view.playCardFront(getHand().get(handBox.getChildren().indexOf(parent)),newStandardCoordinate,myID);
            }

            Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/155.5), (int) Math.round(hover_y/79.5));
            ImageView newCardImage =new ImageView(db.getImage());
            newCardImage.setFitWidth(200);
            newCardImage.setFitHeight(150);

            fieldPane.getChildren().add(newCardImage);
            newCardImage.setTranslateX(newCoordinate.x*155.5);
            newCardImage.setTranslateY(newCoordinate.y * 79.5);
            e.setDropCompleted(true);
        }catch (RemoteException ex){
            showErrorMsg(ex.getMessage());
            System.exit(1);
        } catch (InvalidGoalException | HandFullException | InvalidChoiceException | IsNotYourTurnException |
                 UnacceptableNumOfPlayersException | OnlyOneGameException | PlayerNameNotUniqueException | IOException |
                 IllegalOperationException | InvalidCardException | DeckEmptyException | HandNotFullException |
                 InvalidUserId | NoGameExistsException | RequirementsNotMetException | IllegalPositionException |
                 ClassNotFoundException | InvalidGameID ex) {
            showErrorMsg(ex.getMessage());
        }


    }
    private void handleDragDone(DragEvent e ){
        if (e.getTransferMode() == TransferMode.MOVE) {
            removedStack = (StackPane) ((ImageView) e.getSource()).getParent();
            handBox.getChildren().remove(removedStack);
        }

        e.consume();
    }
    private void showErrorMsg(String message) {
        errorMsg.setText(message.toUpperCase(Locale.ROOT));
        errorMsg.setVisible(true);
        hideError.play();

    }

    public void loadChat(String user){
        messageBox.getChildren().clear();
        chatMenu.setText(user);
        List <String> chatList = new ArrayList<>();
        //TODO: Fix the fact that the username Global could exist
        try{
            if (user.equals("Global")){
                    chatList.addAll(view.getChatList());
            }else{
                chatList.addAll(getPrivateChat(user));
            }
        } catch (RemoteException e) {
            showErrorMsg("Connection Error");
            System.exit(1);
        }
        for (String message : chatList){
            Text text = new Text(message);
            text.setWrappingWidth(500);
            messageBox.getChildren().add(text);
        }



    }

    public void updateChat(){

        Platform.runLater(()->loadChat(chatMenu.getText()));

    }

    public void showChat(){
        scoreboardButton.setVisible(false);
        hideChatButton.setVisible(true);
        chatButton.setVisible(false);
        chatBox.setVisible(true);
        chatBox.setLayoutX(0);

        for (MenuItem item : chatMenu.getItems()){
            item.setOnAction(actionEvent->loadChat(item.getText()));
        }



        updateChatTask = scheduler.scheduleAtFixedRate(this::updateChat, 0, 1, TimeUnit.SECONDS);
    }

    public void hideChat(){
        hideScoreboardButton.setVisible(false);
        chatBox.setLayoutX(-1080);
        chatBox.setVisible(false);
        hideChatButton.setVisible(false);
        scoreboardButton.setVisible(true);
        chatButton.setVisible(true);

        updateChatTask.cancel(true);
    }

    @FXML
    private void onTextChat(){
        chatMessage = inputChat.getText();
    }
    @FXML
    private void onKeyEnter(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER){
            sendMessage();
        }
    }

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
