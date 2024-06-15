package it.polimi.ingsw.GUI;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class NewHelloController extends GUIController{
    @FXML
    private TextField username;
    @FXML
    private Button socketButton;
    @FXML
    private Button RMIButton;
    @FXML
    private Button createGameButton;
    @FXML
    private Button joinGameButton;
    @FXML
    private HBox selectNumPlayersOrGame;
    @FXML
    private HBox selectJoinOrCreate;
    @FXML
    private StackPane numPlayersStackPane;
    @FXML
    private StackPane joinableGamesStackPane;
    @FXML
    private Accordion listOfGames;
    @FXML
    private Button twoplayersbutton;
    @FXML
    private Button threeplayersbutton;
    @FXML
    private Button fourplayersbutton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button playButton;
    private Boolean isSocketSelected;
    private int numPlayers =0;
    private String myName;

    public void initialize(){
        joinableGamesStackPane.setVisible(false);
        numPlayersStackPane.setVisible(false);
        selectJoinOrCreate.setVisible(false);
        playButton.setVisible(false);

    }
    @FXML
    private void selectSocket(){
        isSocketSelected=true;
        socketButton.setStyle("-fx-border-color: #820933");
        RMIButton.setStyle("-fx-background-color:  #e5a78a");
        selectJoinOrCreate.setVisible(true);
    }
    @FXML
    private void selectRMI(){
        isSocketSelected=false;
        RMIButton.setStyle("-fx-border-color: #820933");
        socketButton.setStyle("-fx-background-color:  #e5a78a");
        selectJoinOrCreate.setVisible(true);
    }
    @FXML
    private void selectJoinGame(){
        playButton.setVisible(false);
        numPlayersStackPane.setVisible(false);
        joinableGamesStackPane.setVisible(true);
        MouseEvent mouseEvent=new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0,
                MouseButton.PRIMARY,
                1,
                false, false, false, false,
                true, false, false, true,
                false, false, null);
        refreshButton.fireEvent(mouseEvent);
    }
    @FXML
    private void selectCreateGame(){
        joinableGamesStackPane.setVisible(false);
        numPlayersStackPane.setVisible(true);
        playButton.setVisible(true);
    }
    @FXML
    private void select2Players(){
      numPlayers =2;
      twoplayersbutton.setStyle("-fx-background-color: #e5a78a");
      threeplayersbutton.setStyle("-fx-background-color: #820933");
      fourplayersbutton.setStyle("-fx-background-color: #820933");
    }
    @FXML
    private void select3Players(){
        numPlayers=3;
        threeplayersbutton.setStyle("-fx-background-color: #e5a78a");
        twoplayersbutton.setStyle("-fx-background-color: #820933");
        fourplayersbutton.setStyle("-fx-background-color: #820933");
    }
    @FXML
    private void select4Players(){
        numPlayers=4;
        fourplayersbutton.setStyle("-fx-background-color: #e5a78a");
        threeplayersbutton.setStyle("-fx-background-color: #820933");
        twoplayersbutton.setStyle("-fx-background-color: #820933");

    }
    @FXML
    private void changeUsername(){
        myName=username.getText();
    }
    @FXML
    private void refresh(){

    }
}
