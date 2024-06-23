package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WaitingRoomController extends GUIController{
    @FXML
    private HBox playersContainer;
    @FXML
    private MediaView tutorialVideo;

    @FXML
    private Button button;

    @FXML
    private VBox vBox;

    @FXML
    private void onButtonSelected(ActionEvent event) throws IOException{
        changeScene("choose-starter-card-side.fxml", event);
    }

    @Override
    public void init() {
        try {
            File file=new File(getClass().getResource("/tutorial Codex Naturalis.mp4").toURI());
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer=new MediaPlayer(media);
            tutorialVideo.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(()->{
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Tutorial video not found");
            alert.showAndWait();
        }

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
                            Text label= (Text) vBox.getChildren().getFirst();
                            label.setText("Game's READY");
                            flag.set(true);
                        }
                    } catch (RemoteException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Connection error");
                        alert.showAndWait();
                        System.exit(1);
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
