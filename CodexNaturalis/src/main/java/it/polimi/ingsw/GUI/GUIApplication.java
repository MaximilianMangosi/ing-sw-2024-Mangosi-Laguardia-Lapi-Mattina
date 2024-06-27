package it.polimi.ingsw.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {
    /**
     * starts the application
     * @author Maximilian Mangosi
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("lobby-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 600);
        String serverAddress=getParameters().getRaw().getFirst();
        LobbyController controller =fxmlLoader.getController();
        controller.setServerAddress(serverAddress);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon/codex logo.png")));
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);

        stage.show();
    }




}