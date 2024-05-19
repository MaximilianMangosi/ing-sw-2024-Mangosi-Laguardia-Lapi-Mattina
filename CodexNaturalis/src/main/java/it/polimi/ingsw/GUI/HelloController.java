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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class HelloController extends GUIController {
    private boolean isSocketSelected = true;
    
    @FXML
    private Button playID;

    @FXML
    private TextField usernameField;
    @FXML
    private VBox playerNum;

    @FXML
    private Button selectSocketButton;
    @FXML
    private Button selectRMIButton;
    @FXML
    private void onSocketSelected(){
        isSocketSelected = true;
        selectSocketButton.setStyle("-fx-background-color: #820933");
        selectRMIButton.setStyle("-fx-background-color: #e5a78a");
    }


    @FXML
    private void onRmiSelected(){
        isSocketSelected = false;
        selectSocketButton.setStyle("-fx-background-color: #e5a78a");
        selectRMIButton.setStyle("-fx-background-color: #820933");
    }

    @FXML
    private void onUsernameChange(){
        myName = usernameField.getText();
    }

    @FXML
    private void onSelect2Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(2, event);
    }
    @FXML
    private void onSelect3Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(3, event);
    }
    @FXML
    private void onSelect4Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(4, event);
    }

    public void initialize(){
        playerNum.setVisible(false);
    }
    @FXML
    private void onPlay(ActionEvent event) throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, InvalidUserId, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, UnacceptableNumOfPlayersException, OnlyOneGameException {
        try {
            GameData gameData = new GameData();
            if (isSocketSelected) {
                Socket server;
                server = new Socket("local-host", 2323);
                view = new ViewSocket(server.getOutputStream(), server.getInputStream(), gameData);
                //ServerHandler t1 = new ServerHandler((ViewSocket) view,);
            } else {
                Registry registry = LocateRegistry.getRegistry(1099);
                view = (ViewRMIInterface) registry.lookup("ViewRMI");
            }
        } catch (Exception e) {
            //
            return;
        }

        boolean error = true;

        try {

            myID = view.joinGame(myName);
            view.initializeFieldBuildingHelper(myName);
            changeScene("waiting-room.fxml", event);

        } catch (PlayerNameNotUniqueException e) {
            // handleNameNotUnique();
        } catch (NoGameExistsException e) {
            playerNum.setVisible(true);
        }

    }

    private void createNewGame(int numOfPlayers, ActionEvent event) throws IOException, InvalidUserId {

            try {
                myID = view.bootGame(numOfPlayers, myName);
                view.initializeFieldBuildingHelper(myName);
            } catch (UnacceptableNumOfPlayersException ex1) {
            } catch (OnlyOneGameException ex) {
                try {
                    view.joinGame(myName);
                    view.initializeFieldBuildingHelper(myName);
                } catch (PlayerNameNotUniqueException e1) {
                    return;
                } catch (NoGameExistsException | IOException | IllegalOperationException | ClassNotFoundException |
                         InvalidGoalException | HandNotFullException | HandFullException | InvalidChoiceException |
                         IsNotYourTurnException | InvalidUserId | RequirementsNotMetException |
                         UnacceptableNumOfPlayersException | OnlyOneGameException | IllegalPositionException |
                         InvalidCardException | DeckEmptyException ignore) {

                }
            }catch (HandFullException | ClassNotFoundException | IllegalOperationException | IOException |
                    InvalidGoalException | HandNotFullException | InvalidChoiceException | NoGameExistsException |
                    IsNotYourTurnException | InvalidUserId | RequirementsNotMetException |
                    PlayerNameNotUniqueException | IllegalPositionException | InvalidCardException | DeckEmptyException h){
                return;
            }

            changeScene("waiting-room.fxml", event);

    }

}