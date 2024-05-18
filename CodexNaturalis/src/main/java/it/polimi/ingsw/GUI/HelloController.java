package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewRMIInterface;
import it.polimi.ingsw.view.ViewSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isSocketSelected = true;

    private String selectedUsername;

    private int numOfPlayers = 2;
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;
    @FXML
    private VBox playerNum;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onSocketSelected(){
        isSocketSelected = true;
    }

    @FXML
    private void onRmiSelected(){
        isSocketSelected = false;
    }

    @FXML
    private void onUsernameChange(){
        selectedUsername = usernameField.getText();
    }

    @FXML
    private void onSelect2Player(){
        numOfPlayers = 2;
    }
    @FXML
    private void onSelect3Player(){
        numOfPlayers = 3;
    }
    @FXML
    private void onSelect4Player(){
        numOfPlayers = 4;
    }
    @FXML
    private void onPlay() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, InvalidUserId, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        HelloApplication.connectAndPlay(isSocketSelected, selectedUsername, numOfPlayers);
        FXMLLoader loader= new FXMLLoader(getClass().getResource("choose-starter-card-side.fxml"));
        root= loader.load();

        ChooseSideController nextController= loader.getController();
        //pass view to the controller
    }

    public void initialize(){
        playerNum.setVisible(false);
    }

    public class SceneController {

        private Stage stage;
        private Scene scene;
        private Parent root;

        public void switchToScene1(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("in-game"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        public void switchToScene2(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}