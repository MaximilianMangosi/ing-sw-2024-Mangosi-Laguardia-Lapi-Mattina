package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WaitingRoomController extends GUIController{
    @FXML
    private HBox playersContainer;

    @FXML
    private Button button;

    @FXML
    private VBox vBox;

    @FXML
    private void onButtonSelected(ActionEvent event) throws IOException, InvalidUserId {
        changeScene("choose-starter-card-side.fxml", event);
    }

    @Override
    public void init() {
        button.setVisible(false);
        updatePlayerList();
    }

    private void updatePlayerList() {
        AtomicBoolean flag = new AtomicBoolean(false);
        new Thread(()->{
            while (true) {
                Platform.runLater(()->{
                    try {
                        List<String> playersList = view.getPlayersList();
                        createPlayerLables(playersList);
                        if (view.isGameStarted()){
                            flag.set(true);
                        }
                    } catch (RemoteException e) {
                        //TODO error msg
                    }
                });
                if (flag.get()){
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}
            }

            button.setVisible(true);
            //vBox.setVisible(false);

        }).start();

//        try {
//            System.out.println(view.getPlayersList());
//            createPlayerLables(view.getPlayersList());
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void createPlayerLables(List<String> names) {
        if (names!=null) {
            playersContainer.getChildren().clear();
            for (String name : names) {
                Label label = new Label(name);
                label.setStyle("-fx-font-size: 20px");
                playersContainer.getChildren().add(label);
            }
        }
    }
}
