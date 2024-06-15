package it.polimi.ingsw.GUI;

import it.polimi.ingsw.view.ViewRMIContainer;
import it.polimi.ingsw.view.ViewRMIContainerInterface;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @FXML
    private Text noGamesHosted;
    private Boolean isSocketSelected;
    private int numPlayers =0;
    private String myName;
    private Registry registryIDLE;
    private ViewRMIContainerInterface viewContainerIDLE;
    private UUID chosenGame;

    public void initialize(){
        joinableGamesStackPane.setVisible(false);
        numPlayersStackPane.setVisible(false);
        selectJoinOrCreate.setVisible(false);
        playButton.setVisible(false);
        noGamesHosted.setVisible(false);
        try {
            registryIDLE= LocateRegistry.getRegistry(1099);
            viewContainerIDLE = (ViewRMIContainerInterface) registryIDLE.lookup("ViewRMI");
        } catch (RemoteException | NotBoundException e) {

        }

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
      playButton.setVisible(true);
    }
    @FXML
    private void select3Players(){
        numPlayers=3;
        threeplayersbutton.setStyle("-fx-background-color: #e5a78a");
        twoplayersbutton.setStyle("-fx-background-color: #820933");
        fourplayersbutton.setStyle("-fx-background-color: #820933");
        playButton.setVisible(true);
    }
    @FXML
    private void select4Players(){
        numPlayers=4;
        fourplayersbutton.setStyle("-fx-background-color: #e5a78a");
        threeplayersbutton.setStyle("-fx-background-color: #820933");
        twoplayersbutton.setStyle("-fx-background-color: #820933");
        playButton.setVisible(true);

    }
    @FXML
    private void changeUsername(){
        myName=username.getText();
    }
    @FXML
    private void refresh(){
        noGamesHosted.setVisible(false);
        try {
            listOfGames.getPanes().removeAll();
            //looks for an RMI view just to get the games waiting, the view will not be used for anything else
            Map<UUID, List<String>> joinableGames = viewContainerIDLE.getJoinableGames();
            if(joinableGames.isEmpty()){
                noGamesHosted.setVisible(true);
                return;
            }else{

                int i=1;
                for(Map.Entry<UUID, List<String>> entry : joinableGames.entrySet()){
                    VBox playersList = new VBox();
                    StackPane playersStackPane = new StackPane(playersList);

                   // playersList.setAlignment(Pos.CENTER_LEFT);
                    List<String> players = entry.getValue();
                    for(String s: players){
                        Text name = new Text();
                        name.setText(s);
                        name.setTabSize(18);
                        name.setStyle("-fx-font: Bodoni MT Condensed");
                        playersList.getChildren().add(name);

                    }
                    TitledPane newGame = new TitledPane(String.valueOf(i),playersStackPane);

                    newGame.setOnMouseClicked(mouseEvent -> chooseThisGame(mouseEvent,entry.getKey()));
                    listOfGames.getPanes().add(newGame);
                    i++;
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void onPlay(){

    }
    @FXML
    private void chooseThisGame(MouseEvent e,UUID id){
        this.chosenGame=id;
    }
}
