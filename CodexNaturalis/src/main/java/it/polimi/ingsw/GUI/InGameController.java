package it.polimi.ingsw.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import javax.swing.text.html.ImageView;
import java.rmi.RemoteException;

public class InGameController extends GUIController {
    @FXML
    private HBox playerListBox=new HBox();
    private


    public void initialize() throws RemoteException {
        String[] temp={"pepo","Maxsdfkajflkjaldk","Giorgio"};
        for (String p :temp){
            StackPane sp= new StackPane();
            Label label=new Label(p);
            label.setFont(new Font("Bodoni MT Condensed",40));
            sp.getChildren().add(label);
            playerListBox.getChildren().add(sp);
        }
    }

    
}
