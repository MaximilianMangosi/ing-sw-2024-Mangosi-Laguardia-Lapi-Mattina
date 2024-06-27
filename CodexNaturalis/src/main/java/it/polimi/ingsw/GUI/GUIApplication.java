package it.polimi.ingsw.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.NoSuchElementException;

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
        String serverAddress= null;
        try {
            serverAddress = getParameters().getRaw().getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("ERROR server address not found");
            System.exit(1);
        }
        LobbyController controller =fxmlLoader.getController();
        controller.setServerAddress(serverAddress);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon/codex logo.png")));
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);
        stage.setOnCloseRequest(WindowEvent->{
                Platform.exit();
                System.exit(1);
        });

        stage.show();
    }




}