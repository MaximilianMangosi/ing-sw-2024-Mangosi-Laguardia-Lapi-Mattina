package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.gamecards.cards.Card;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class InGameController extends GUIController {
    @FXML
    private HBox playerListBox=new HBox();

    @FXML
    private ImageView visibleCard1;
    @FXML
    private ImageView visibleCard2;
    @FXML
    private ImageView visibleCard3;
    @FXML
    private ImageView visibleCard4;

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
            Image cardPng = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/" + id + ".png"));
            ImageView cardView = new ImageView(cardPng);
            handBox.getChildren().add(cardView);
        }

    }

    private void checkGameInfo(){

        new Thread(() -> {

            List<Card> oldCards = new ArrayList<>();
            while(true){

                //visible cards
                try {
                    if(!view.getVisibleCards().equals(oldCards)){
                        Platform.runLater(() -> {
                            try {
                                updateVisibleCards(view.getVisibleCards());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        oldCards = view.getVisibleCards();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void updateVisibleCards(List<Card> newCards){

    }
}
