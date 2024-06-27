package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import it.polimi.ingsw.view.ViewRMIInterface;
import it.polimi.ingsw.view.ViewSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LobbyController extends GUIController {
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

    /**
     * button for selection of soket
     * @author Maximilian Mangosi
     */
    @FXML
    private void onSocketSelected(){
        isSocketSelected = true;
        selectSocketButton.setStyle("-fx-background-color: #820933");
        selectRMIButton.setStyle("-fx-background-color: #e5a78a");
    }

    /**
     * button for selection of rmi
     * @author Maximilian Mangosi
     */
    @FXML
    private void onRmiSelected(){
        isSocketSelected = false;
        selectSocketButton.setStyle("-fx-background-color: #e5a78a");
        selectRMIButton.setStyle("-fx-background-color: #820933");
    }

    /**
     * update thr username
     * @author Maximilian Mangosi
     */
    @FXML
    private void onUsernameChange(){
        myName = usernameField.getText();
    }

    /**
     * on selection of two players
     * @author Maximilian Mangosi
     * @param event
     * @throws IOException
     * @throws InvalidUserId
     */
    @FXML
    private void onSelect2Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(2, event);
    }
    /**
     * on selection of three players
     * @author Maximilian Mangosi
     * @param event
     * @throws IOException
     * @throws InvalidUserId
     */
    @FXML
    private void onSelect3Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(3, event);
    }
    /**
     * on selection of four players
     * @author Maximilian Mangosi
     * @param event
     * @throws IOException
     * @throws InvalidUserId
     */
    @FXML
    private void onSelect4Player(ActionEvent event) throws IOException, InvalidUserId {
        createNewGame(4, event);
    }

    /**
     * initializes the scene
     * @author Maximilian Mangosi
     */
    public void initialize(){
        playerNum.setVisible(false);
    }

    /**
     * switches scene when the player selects the play button
     * @author Maximilian Mangosi
     * @param event
     * @throws IOException
     * @throws IllegalOperationException
     * @throws ClassNotFoundException
     */
    @FXML
    private void onPlay(ActionEvent event) throws  IOException, IllegalOperationException, ClassNotFoundException {
        try {
            GameData gameData = new GameData();
            if (isSocketSelected) {
                Socket server;
                server = new Socket("localhost", 2323);
                view = new ViewSocket(server, gameData);
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

            myID = view.joinGame(null,myName );
            view.initializeFieldBuildingHelper(myName);
            changeScene("waiting-room.fxml", event);

        } catch (PlayerNameNotUniqueException e) {
            // handleNameNotUnique();
        } catch (InvalidGameID e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * creates a new game
     * @author Maximilian Mangosi
     * @param numOfPlayers
     * @param event
     * @throws IOException
     */
    private void createNewGame(int numOfPlayers, ActionEvent event) throws IOException {

            try {
               GameKey gameKey= view.bootGame(numOfPlayers, myName);
               myID=gameKey.userID();
               gameID=gameKey.gameID();
                view.initializeFieldBuildingHelper(myName);
            } catch (UnacceptableNumOfPlayersException ignore) {
            } catch (ClassNotFoundException | IllegalOperationException | IOException |
                     PlayerNameNotUniqueException h){
                return;
            }

            changeScene("waiting-room.fxml", event);

    }

}