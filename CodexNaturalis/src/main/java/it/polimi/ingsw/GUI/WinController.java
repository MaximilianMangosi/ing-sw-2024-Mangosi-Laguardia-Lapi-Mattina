package it.polimi.ingsw.GUI;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.rmi.RemoteException;
import java.util.Map;

public class WinController extends GUIController{
    @FXML
    private VBox playersContainer;

    @FXML
    private Text winnerTxt;

    @Override
    public void init() throws RemoteException {

        winnerTxt.setText("Winner: " + view.getWinner());

       Map<String, Integer> playerPoints = view.getPlayersPoints();

        playerPoints.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    String player = entry.getKey();
                    Integer points = entry.getValue();
                   addPlayer(player, points);
                });
    }

    private void addPlayer(String username, int points){
        Text text1 = new Text("First Text");
        text1.setFont(new Font(26));

        Text text2 = new Text("Second Text");
        text2.setFont(new Font(18));

        Text text3 = new Text("Third Text");
        text3.setFont(new Font(22));

        HBox hbox = new HBox(12);
        hbox.setMaxWidth(400);
        hbox.setBackground(new Background(new BackgroundFill(Color.web("#e5a78a"), CornerRadii.EMPTY, Insets.EMPTY)));

        hbox.getChildren().addAll(text1, text2, text3);

        playersContainer.getChildren().add(hbox);
    }
}
