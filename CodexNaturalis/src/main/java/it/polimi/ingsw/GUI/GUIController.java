package it.polimi.ingsw.GUI;

import it.polimi.ingsw.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.UUID;

public class GUIController {
    protected View view;
    protected UUID myID;
    protected String myName;
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
    public void changeScene(String pathXML, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathXML));
        Parent root = loader.load();
        GUIController c = loader.getController();
        c.setView(view);
        c.setMyId(myID);
        c.setMyName(myName);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
