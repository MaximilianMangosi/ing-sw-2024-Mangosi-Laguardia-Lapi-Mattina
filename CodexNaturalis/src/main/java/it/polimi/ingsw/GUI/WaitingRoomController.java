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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
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

    /**
     * when selection of the start button
     * @author Maximilian Mangosi
     * @param event
     * @throws IOException
     */
    @FXML
    private void onButtonSelected(ActionEvent event) throws IOException{
        changeScene("choose-starter-card-side.fxml", event);
    }
    /**
     * initializes the scene
     * @author Maximilian Mangosi
     */
    @Override
    public void init() {
        try {
            File tempFile = File.createTempFile("tutorial", ".mp4");
            tempFile.deleteOnExit();
            InputStream inputStream = getClass().getResourceAsStream("/tutorial Codex Naturalis.mp4");
            Files.copy(inputStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Media media = new Media(tempFile.toURI().toString());
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
    /**
     * updates the players list
     * @author Maximilian Mangosi
     */
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

        }).start();

    }
    /**
     * inserts the players waiting in the list
     * @author Maximilian Mangosi
     */
    private void createPlayerLables(List<String> names) {
        if (names!=null) {
            playersContainer.getChildren().clear();
            for (String name : names) {
                Label label = new Label(name);
                label.setFont(new Font("Bodoni Mt",30));
                playersContainer.getChildren().add(label);
            }
        }
    }
}
