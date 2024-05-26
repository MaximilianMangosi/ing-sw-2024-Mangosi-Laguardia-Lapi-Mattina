package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import javax.management.monitor.MonitorSettingException;
import java.beans.EventHandler;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void init() throws RemoteException, InvalidUserId {
        deckBox.setVisible(false);
        hideDeckButton.setVisible(false);
        scoreboardBox.setVisible(false);
        hideScoreboardButton.setVisible(false);
        errorMsg.setVisible(false);
        for (String p : view.getPlayersList()) {
            StackPane sp = new StackPane();
            Label label = new Label(p);
            label.setFont(new Font("Bodoni MT Condensed", 40));
            if (view.getCurrentPlayer().equals(p))
                label.setStyle("-fx-background-color: d9be4a");
            sp.getChildren().add(label);
            playerListBox.getChildren().add(sp);
            //makes label clickable
            //label.setOnMouseClicked(this::showEnemyField);


        }
        updateHand(view.showPlayerHand(myID));
        for(Node cardStack: handBox.getChildren() ){
            cardStack.setOnMouseClicked(this::flipCard);
        }
        int i=0;
        for (Goal g: view.getPublicGoals()){
            int id= g.getId();
            Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView goalView= (ImageView) goalsBox.getChildren().get(i);
            goalView.setImage(goalPng);
            i++;
        }
        int id = view.showPrivateGoal(myID).getId();
        Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        ImageView goalView= (ImageView) privateGoalBox.getChildren().getFirst();
        goalView.setImage(goalPng);

        resourceCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(0));
        goldCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(1));
        visibleCard1.setOnMouseClicked(mouseEvent -> drawVisibleCard(0));
        visibleCard2.setOnMouseClicked(mouseEvent -> drawVisibleCard(1));
        visibleCard3.setOnMouseClicked(mouseEvent -> drawVisibleCard(2));
        visibleCard4.setOnMouseClicked(mouseEvent -> drawVisibleCard(3));

        id=view.getStarterCard(myID).getId();
        Image scPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));


        ImageView scView=new ImageView(scPng);
        scView.setFitWidth(200);
        scView.setFitHeight(150);
        scView.setOnMouseClicked(mouseEvent -> handleClickCard(mouseEvent,new Coordinates(0,0)));
        fieldPane.getChildren().add(scView);


        checkGameInfo();


    }


    private void drawFromDeck(int i) {
        try {
            view.drawFromDeck(myID,i);
        } catch (IOException e) {
            errorMsg.setVisible(true);
            errorMsg.setText("Connection Error");
            try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException |DeckEmptyException e) {
            errorMsg.setVisible(true);
            errorMsg.setText(e.getMessage());

            try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
            errorMsg.setVisible(false);

        }catch (InvalidGoalException | InvalidUserId | HandNotFullException | NoGameExistsException |
                RequirementsNotMetException | UnacceptableNumOfPlayersException | OnlyOneGameException |
                PlayerNameNotUniqueException | IllegalPositionException | InvalidCardException  |
                ClassNotFoundException ignore){}
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
                    if(!newTopResource.equals(oldTopResource)){
                        Platform.runLater(() -> {
                            updateTopResource(newTopResource);
                        });

                        oldTopResource = newTopResource;
                    }
                    Reign newTopGold = view.getTopOfGoldCardDeck();
                    if(!newTopGold.equals(oldTopGold)){
                        Platform.runLater(() -> {
                            updateTopGold(newTopGold);
                        });

                        oldTopGold = newTopGold;
                    }
                //hand
                    List<Card> newHand= view.showPlayerHand(myID);
                    if(!newHand.equals(oldHand))
                        Platform.runLater(()->updateHand(newHand));
                    oldHand=newHand;

                //current player
                    String newCurrentPlayer=view.getCurrentPlayer();
                    if(!view.getCurrentPlayer().equals(oldCurrentPlayer)){
                        Platform.runLater(() -> updateCurrentPlayer(newCurrentPlayer));
                        oldCurrentPlayer = newCurrentPlayer;
                    }
                } catch (RemoteException | InvalidUserId e) {
                    errorMsg.setVisible(true);
                    errorMsg.setText("Connection Error");
                    try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
                    System.exit(1);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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

        } catch (IOException e) {
            errorMsg.setVisible(true);
            errorMsg.setText("Connection Error");
            try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
            System.exit(1);
        } catch (IsNotYourTurnException | HandFullException | IllegalOperationException | InvalidChoiceException e) {
            errorMsg.setVisible(true);
            errorMsg.setText(e.getMessage());

            try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
            errorMsg.setVisible(false);
        }catch (InvalidGoalException | InvalidUserId | HandNotFullException | NoGameExistsException |
                RequirementsNotMetException | UnacceptableNumOfPlayersException | OnlyOneGameException |
                PlayerNameNotUniqueException | IllegalPositionException | InvalidCardException | DeckEmptyException |
                ClassNotFoundException ignore){}
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
        scoreboardButton.setVisible(false);
        scoreboardBox.setVisible(true);
        scoreboardBox.setLayoutX(0);
        hideScoreboardButton.setVisible(true);
    }
    public void hideDeck(){
        hideDeckButton.setVisible(false);
        deckBox.setLayoutX(-541);
        deckBox.setVisible(false);
        deckButton.setVisible(true);
    }
    public void hideScoreboard(){
        hideScoreboardButton.setVisible(false);
        scoreboardBox.setLayoutX(1920);
        scoreboardBox.setVisible(false);
        scoreboardButton.setVisible(true);
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
    private void showEnemyField(MouseEvent event) throws RemoteException {
        //fetch the other player's hand
        Label l =(Label) event.getSource();
        String username = l.getText();

        Map <Coordinates, Card> field=  view.getPlayersField(username);
        List<Coordinates> fieldBuildingHelper = view.getFieldBuildingHelper(username);

        Button returnToMyFieldButton = new Button("Return");

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

        playerListBox.getChildren().add(returnToMyFieldButton);
        ((Pane) borderPane.getCenter()).getChildren().add(newHugeStackPane);

        returnToMyFieldButton.setOnMouseClicked(MouseEvent->returnToMyField(MouseEvent,oldFirstChild,newHugeStackPane));

    }
    private void returnToMyField(MouseEvent event,Node oldField,Node newField){

    }

}
