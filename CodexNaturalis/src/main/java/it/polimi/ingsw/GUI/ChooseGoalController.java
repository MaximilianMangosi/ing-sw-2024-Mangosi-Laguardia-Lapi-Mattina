package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.UUID;

public class ChooseGoalController {
    UUID myid;
    View view;

    @FXML
    ImageView privateGoal1;
    @FXML
    ImageView privateGoal2;
    @FXML
    ImageView publicGoal1;
    @FXML
    ImageView publicGoal2;

    public void setPrivateGoal1(Image pg1){
        privateGoal1.setImage(pg1);
    }
    public void setPrivateGoal2(Image pg2){
        privateGoal2.setImage(pg2);
    }
    public void setPublicGoal1(Image pg1){
        publicGoal1.setImage(pg1);
    }
    public void setPublicGoal2(Image pg2){
        publicGoal2.setImage(pg2);
    }

}
