package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.UserInterface;
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

    /**
     * sets the user id
     * @author Maximilian Mangosi
     * @param myId
     */
    public void setMyId(UUID myId){
        this.myID = myId;
    }

    /**
     * sets the name
     * @author Maximilian Mangosi
     * @param myName the username
     */
    public void setMyName(String myName){
        this.myName = myName;
    }

    /**
     * changes the scene
     * @author Maximilian Mangosi
     * @param pathXML the path to fxml file of the scene
     * @param event the event used to get the  stage
     * @throws IOException when a connection error occurs
     */
    public void changeScene(String pathXML, ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathXML));
        root = loader.load();
        GUIController c = loader.getController();
        if (pathXML.equals("lobby-view.fxml")) {
            LobbyController controller=(LobbyController) c;
            if(view.isRMI()){
                controller.selectRMI();
            }
            else
                controller.selectSocket();
        }else{
            c.setView(view);
            c.setMyId(myID);
            c.setMyName(myName);
            c.init();
        }
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        stage.setHeight(screenBounds.getHeight());
        stage.setWidth(screenBounds.getWidth());
        stage.centerOnScreen();
        stage.show();
    }

    public void init() throws RemoteException{
    }
}
