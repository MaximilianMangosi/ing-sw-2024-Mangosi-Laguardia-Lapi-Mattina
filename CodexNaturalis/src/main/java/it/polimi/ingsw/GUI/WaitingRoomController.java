package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.gamelogic.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class WaitingRoomController extends GUIController{
    @FXML
    private HBox playersContainer;

    @FXML
    public void initialize() throws RemoteException {
        List<String> already = new ArrayList<>();

        while (!view.isGameStarted()) {
            if (!already.equals(view.getPlayersList())) {
                createPlayerLables(view.getPlayersList());
            }
            already = view.getPlayersList();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createPlayerLables(List<String> names) {
        for (String name : names) {
            Label label = new Label(name);
            label.setStyle("-fx-font-size: 20px");
            playersContainer.getChildren().add(label);
        }
    }
}
