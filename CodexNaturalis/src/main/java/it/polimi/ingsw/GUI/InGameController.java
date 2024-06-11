package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.beans.EventHandler;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
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
    private Button hideDeckButton;
    private Map<ImageView,Integer> handCardsId = new HashMap<>();
    private EventHandler playCardEvent;
    private ImageView selectedCardToPlay;
    private boolean returnButtonPresent= false;
    private final PauseTransition hideError = new PauseTransition(Duration.seconds(3));
    private final Map<Integer,Coordinates> scoreMap=new HashMap<>();

    private Timeline overlapAnimation;


    public void init() throws RemoteException, InvalidUserId {
        deckBox.setVisible(false);
        hideDeckButton.setVisible(false);
        scoreboardBox.setVisible(false);
        hideScoreboardButton.setVisible(false);
        errorMsg.setVisible(false);
        hideError.setOnFinished(event -> errorMsg.setVisible(false));

        initializeScoreMap();

        for (String p : view.getPlayersList()) {
            StackPane sp = new StackPane();
            Label label = new Label(p);
            label.setFont(new Font("Bodoni MT Condensed", 40));
            if (view.getCurrentPlayer().equals(p))
                label.setStyle("-fx-background-color: d9be4a");
            sp.getChildren().add(label);
            playerListBox.getChildren().add(sp);
            //makes label clickable
            label.setOnMouseClicked(this::showEnemyField);


        }
        fieldPane.setOnDragOver(this::handleDragOver);
        fieldPane.setOnDragDropped(this::handleDragDropped);
        updateHand(getHand());
        for(Node cardStack: handBox.getChildren() ){
            cardStack.setOnMouseClicked(this::flipCard);

            ((StackPane) cardStack).getChildren().getFirst().setOnDragDetected(this::handlePickUpCard);
            ((StackPane) cardStack).getChildren().get(1).setOnDragDetected(this::handlePickUpCard);
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

        id= getStarterCard().getId();
        Image scPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));


        ImageView scView=new ImageView(scPng);
        scView.setFitWidth(200);
        scView.setFitHeight(150);
        scView.setOnMouseClicked(mouseEvent -> handleClickCard(mouseEvent,new Coordinates(0,0)));
        fieldPane.getChildren().add(scView);




        checkGameInfo();


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
            List<Card> oldHand=new ArrayList<>();

            String oldCurrentPlayer = "";

            while(true){

                //visible cards
                try {
                    List<Card> newVisibleCards = view.getVisibleCards();
                    if(!newVisibleCards.equals(oldVisibleCards)){
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
                //hand
                    List<Card> newHand= getHand();
                    if(!newHand.equals(oldHand)){
                        Platform.runLater(()->updateHand(newHand));
                        System.out.println("hand update");
                    }

                    oldHand=newHand;

                //current player
                    String newCurrentPlayer=view.getCurrentPlayer();
                    if(!view.getCurrentPlayer().equals(oldCurrentPlayer)){
                        Platform.runLater(() -> updateCurrentPlayer(newCurrentPlayer));
                        oldCurrentPlayer = newCurrentPlayer;
                    }
                } catch (RemoteException | InvalidUserId e) {
                    showErrorMsg("CONNECTION ERROR");
                    System.exit(1);
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
        System.out.println("visible card clicked");
        try {
            view.drawVisibleCard(myID,choice);

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
        Image img;
        switch (newTop) {
            case MUSHROOM -> img= new Image(getClass().getResourceAsStream("/CardsBack/1.png"));
            case PLANTS -> img= new Image(getClass().getResourceAsStream("/CardsBack/17.png"));
            case ANIMAL -> img= new Image(getClass().getResourceAsStream("/CardsBack/23.png"));
            case BUG -> img= new Image(getClass().getResourceAsStream("/CardsBack/36.png"));
            default ->   img=new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
        }
        resourceCardDeck.setImage(img);
    }
    private void updateTopGold(Reign newTop){
        Image img;
        switch (newTop) {
            case MUSHROOM -> img= new Image(getClass().getResourceAsStream("/CardsBack/41.png"));
            case PLANTS -> img= new Image(getClass().getResourceAsStream("/CardsBack/57.png"));
            case ANIMAL -> img= new Image(getClass().getResourceAsStream("/CardsBack/63.png"));
            case BUG -> img= new Image(getClass().getResourceAsStream("/CardsBack/76.png"));
            default ->   img=new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
        }
        goldCardDeck.setImage(img);
    }
    private void updateHand(List<Card> newHand){
        for(Card card: newHand){
            int id = card.getId();
            Image frontPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            Image backPng = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"));
            StackPane cardStack = (StackPane) handBox.getChildren().get(newHand.indexOf(card));
            ImageView backView= (ImageView) cardStack.getChildren().getFirst();
            ImageView frontView= (ImageView) cardStack.getChildren().get(1);
            backView.setImage(backPng);
            frontView.setImage(frontPng);
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
        scoreboardBox.setVisible(true);
        scoreboardBox.setLayoutX(0);
        hideScoreboardButton.setVisible(true);

    }

    private void handleCheckOverlap() {
        List<ImageView> checksOverlapping = imageChecks.stream().filter(this::overlaps).toList();
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
    //
    private void showEnemyField(MouseEvent event)  {
        //fetch the other player's hand
        try {
            Label l = (Label) event.getSource();
            String username = l.getText();

            Map<Coordinates, Card> field = view.getPlayersField(username);
            List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);


            //saves the old field and sets it invisible
            Pane oldCenter = (Pane) borderPane.getCenter();
            Node oldFirstChild = oldCenter.getChildren().getFirst();
            oldFirstChild.setVisible(false);
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
                Image i = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
                ImageView newImageView = new ImageView(i);
                newFieldPane.getChildren().add(newImageView);
                newImageView.setFitWidth(200);
                newImageView.setFitHeight(150);
                newImageView.setTranslateX(c.x*155.5);
                newImageView.setTranslateY(c.y*79.5);
            }
            if(!returnButtonPresent) {
                Button returnToMyFieldButton = new Button("Return");
                playerListBox.getChildren().add(returnToMyFieldButton);
                returnToMyFieldButton.setOnMouseClicked(MouseEvent -> returnToMyField(MouseEvent, oldFirstChild, newHugeStackPane));
                returnButtonPresent=true;
            }
            ((Pane) borderPane.getCenter()).getChildren().add(newHugeStackPane);



        }catch (RemoteException e){
            //TODO ERROR MESSAGE
        }
    }
    private void returnToMyField(MouseEvent event,Node oldField,Node newField){
        Pane oldCenter = (Pane) borderPane.getCenter();
        oldCenter.getChildren().remove(newField);
        oldField.setVisible(true);
        playerListBox.getChildren().remove(event.getSource());
        returnButtonPresent=false;
    }
    private void handlePickUpCard(MouseEvent event){
        Dragboard db=((Node) event.getSource()).startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cpc = new ClipboardContent();
        cpc.putImage(((ImageView)event.getSource()).getImage());
        db.setContent(cpc);
        event.consume();
    }
    private void handleDragOver(DragEvent e)  {
        try{
            double hover_x = e.getX()-1204;
            double hover_y = e.getY()-805;
            Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/155.5), (int) Math.round(hover_y/79.5));
            List<Coordinates> avlbPositions = view.showPlayersLegalPositions(myID);

            if(e.getDragboard().hasImage() && avlbPositions.contains(newCoordinate) ){
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        }catch (InvalidUserId | RemoteException invalidUserId){

        }

    }
    private void  handleDragDropped(DragEvent e){
        try{
            String p= view.getCurrentPlayer();
            Dragboard db = e.getDragboard();
            double hover_x = e.getX()-1204;
            double hover_y = e.getY()-805;

            Coordinates newCoordinate = new Coordinates((int) Math.round(hover_x/155.5), (int) Math.round(hover_y/79.5));
            ImageView newCardImage =new ImageView(db.getImage());
            newCardImage.setFitWidth(200);
            newCardImage.setFitHeight(150);
            newCardImage.setTranslateX(newCoordinate.x*155.5);
            newCardImage.setTranslateY(newCoordinate.y * 79.5);

            fieldPane.getChildren().add(newCardImage);
            e.setDropCompleted(true);
        }catch (RemoteException ex){

        }


    }
    private void handleDragDone(DragEvent e ){
        if (e.getTransferMode() == TransferMode.MOVE) {
            handBox.getChildren().remove(((ImageView)e.getSource()).getParent());
        }
        e.consume();
    }
    private void showErrorMsg(String message) {
        errorMsg.setText(message.toUpperCase(Locale.ROOT));
        errorMsg.setVisible(true);
        hideError.play();

    }
}
