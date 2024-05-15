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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class HelloApplication extends Application {

    static UUID myID;
    static View view;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void connectAndPlay(boolean isSocketSelected, String selectedUsername, int numOfPlayers) throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, InvalidUserId, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, UnacceptableNumOfPlayersException, OnlyOneGameException {

        try {
            GameData gameData = new GameData();
            if (isSocketSelected) {
                Socket server;
                server = new Socket("192.168.0.1", 2323);
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

            myID = view.joinGame(selectedUsername);

        } catch (PlayerNameNotUniqueException e) {
            // handleNameNotUnique();
        } catch (NoGameExistsException e) {
            while (error) {
                try {
                    myID = view.bootGame(numOfPlayers, selectedUsername);
                    error = false;
                } catch (UnacceptableNumOfPlayersException ex1) {
                    //
                } catch (OnlyOneGameException ex) {
                    //
                    try {
                        view.joinGame(selectedUsername);
                        error = false;
                    } catch (PlayerNameNotUniqueException e1) {
                        // error = handleNameNotUnique();
                    } catch (NoGameExistsException ignore) {
                    }
                }
            }
        }

    }
}