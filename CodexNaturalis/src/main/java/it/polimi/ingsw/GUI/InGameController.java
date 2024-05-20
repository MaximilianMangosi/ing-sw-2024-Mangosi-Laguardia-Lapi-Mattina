package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
    private HBox handBox= new HBox();
    public void init() throws RemoteException, InvalidUserId {
        for (String p : view.getPlayersList()) {
            StackPane sp = new StackPane();
            Label label = new Label(p);
            label.setFont(new Font("Bodoni MT Condensed", 40));
            if (view.getCurrentPlayer().equals(p))
                label.setStyle("-fx-background-color: d9be4a");
            sp.getChildren().add(label);
            playerListBox.getChildren().add(sp);

        }
        for (Card card : view.showPlayerHand(myID)){
            int id = card.getId();
            Image cardPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView cardView = new ImageView(cardPng);
            handBox.getChildren().add(cardView);
        }
        for (Goal g: view.getPublicGoals()){
            int id= g.getId();
            Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView goalView= new ImageView(goalPng);
            goalsBox.getChildren().add(goalView);
        }
        int id = view.showPrivateGoal(myID).getId();
        Image goalPng= new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        ImageView goalView= new ImageView(goalPng);
        privateGoalBox.getChildren().add(goalView);

        resourceCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(0));
        goldCardDeck.setOnMouseClicked(mouseEvent -> drawFromDeck(1));
        visibleCard1.setOnMouseClicked(mouseEvent -> drawVisibleCard(0));
        visibleCard2.setOnMouseClicked(mouseEvent -> drawVisibleCard(1));
        visibleCard3.setOnMouseClicked(mouseEvent -> drawVisibleCard(2));
        visibleCard4.setOnMouseClicked(mouseEvent -> drawVisibleCard(3));
        errorMsg.setVisible(false);

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

    private void checkGameInfo(){

        new Thread(() -> {

            List<Card> oldVisibleCards = new ArrayList<>();
            Reign oldTopResource=null;
            Reign oldTopGold=null;

            String oldCurrentPlayer = "";

            while(true){

                //visible cards
                try {
                    if(!view.getVisibleCards().equals(oldVisibleCards)){
                        Platform.runLater(() -> {
                            try {
                                updateVisibleCards(view.getVisibleCards());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        oldVisibleCards = view.getVisibleCards();
                    }
                //decks

                    Reign newTopResource = view.getTopOfResourceCardDeck();
                    if(!newTopResource.equals(oldTopResource)){
                        Platform.runLater(() -> {
                            updateTopResource(newTopResource);
                        });

                        oldTopResource = newTopResource;
                    }
                    Reign newTopGold = view.getTopOfResourceCardDeck();
                    if(!newTopGold.equals(oldTopGold)){
                        Platform.runLater(() -> {
                            updateTopGold(newTopGold);
                        });

                        oldTopGold = newTopGold;
                    }

                //current player
                    if(!view.getCurrentPlayer().equals(oldCurrentPlayer)){
                        Platform.runLater(() -> {
                            try {
                                updateCurrentPlayer(view.getCurrentPlayer());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        oldCurrentPlayer = view.getCurrentPlayer();
                    }
                } catch (RemoteException e) {
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
    private  void updateTopGold(Reign newTop){
        Image img;
        switch (newTop) {
            case MUSHROOM -> img= new Image(getClass().getResourceAsStream("/CardsBack/41.png"));
            case PLANTS -> img= new Image(getClass().getResourceAsStream("/CardsBack/57.png"));
            case ANIMAL -> img= new Image(getClass().getResourceAsStream("/CardsBack/63.png"));
            case BUG -> img= new Image(getClass().getResourceAsStream("/CardsBack/76.png"));
            default ->   img=new Image(getClass().getResourceAsStream("/Icon/codex_nat_icon.png"));
        }
        resourceCardDeck.setImage(img);
    }
}
