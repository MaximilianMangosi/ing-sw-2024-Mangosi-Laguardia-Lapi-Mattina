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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class HelloController {
    private boolean isSocketSelected = true;

    private String selectedUsername;

    private int numOfPlayers = 2;
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;

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
    }

}