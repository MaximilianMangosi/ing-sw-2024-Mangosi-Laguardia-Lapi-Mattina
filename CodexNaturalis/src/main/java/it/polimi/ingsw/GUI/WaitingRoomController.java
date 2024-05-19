package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.gamelogic.Player;
import javafx.application.Platform;
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

    @Override
    public void init() {
        updatePlayerList();
    }

    private void updatePlayerList() {
        new Thread(()->{
            List<String> already = new ArrayList<>();

            while (true) {
                try {
                    if (view.isGameStarted()) break;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (!already.equals(view.getPlayersList())) {
                        System.out.println(view.getPlayersList());
                        Platform.runLater(()->{
                            try {
                                createPlayerLables(view.getPlayersList());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    already = view.getPlayersList();
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

    private void createPlayerLables(List<String> names) {
        playersContainer.getChildren().clear();
        for (String name : names) {
            Label label = new Label(name);
            label.setStyle("-fx-font-size: 20px");
            playersContainer.getChildren().add(label);
        }
    }
}
