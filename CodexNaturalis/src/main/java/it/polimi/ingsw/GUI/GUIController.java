package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.UUID;

public class GUIController extends UserInterface {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    public void setView(View view){
        this.view = view;
    }
    public void setMyId(UUID myId){
        this.myID = myId;
    }
    public void setMyName(String myName){
        this.myName = myName;
    }
    public void changeScene(String pathXML, ActionEvent event) throws IOException, InvalidUserId {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathXML));
        root = loader.load();
        GUIController c = loader.getController();
        c.setView(view);
        c.setMyId(myID);
        c.setMyName(myName);
        c.init();
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
//        stage.setHeight(screenBounds.getHeight());
//        stage.setWidth(screenBounds.getWidth());
        stage.centerOnScreen();
        stage.show();
    }

    public void init() throws RemoteException, InvalidUserId {
    }
}
