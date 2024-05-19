package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.gamecards.cards.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.rmi.RemoteException;

public class InGameController extends GUIController {
    @FXML
    private HBox playerListBox=new HBox();
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
}
