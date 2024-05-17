package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class ChooseSideController {

    UUID myID;
    View view;
    @FXML
    ImageView frontImage;
    @FXML
    ImageView backImage;
    @FXML
    HBox handBox;
    @FXML
    ImageView privateGoal1;
    @FXML
    ImageView privateGoal2;
    @FXML
    ImageView publicGoal1;
    @FXML
    ImageView publicGoal2;

    ImageView[] handImage=new ImageView[3];
    public void initialize() throws InvalidUserId, RemoteException {
        int id=view.showPlayerGoalOptions()[0].getId();
        Image privateOption1 = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/Goal/0"+id));
        privateGoal1.setImage(privateOption1);

         id=view.showPlayerGoalOptions()[1].getId();
        Image privateOption2 = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/Goal/0"+id));
        privateGoal1.setImage(privateOption2);

        id=view.getPublicGoals()[0].getId();
        Image publicOption1 = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/Goal/0"+id));
        privateGoal1.setImage(publicOption1);
        //TODO fix index of goal cards
        id=view.getPublicGoals()[1].getId();
        Image publicOption2  = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/Goal/0"+id));
        privateGoal1.setImage(publicOption2);

        id=view.getStarterCard(myID).getId();
        Image starterCardFr  = new Image(getClass().getResourceAsStream("src/main/resources/CardsFront/StarterCards/0"+id));
        frontImage.setImage(starterCardFr);

        Image starterCardBk  = new Image(getClass().getResourceAsStream("src/main/resources/CardsBack/StarterCards/0"+id));
        backImage.setImage(starterCardBk);
    }
    public void setImageView(Image front,Image back){
        frontImage.setImage(front);
        backImage.setImage(back);
    }
    public void chooseFront() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
       // view.chooseStarterCardSide(true,myID);
        backImage.setVisible(false);
        frontImage.setLayoutX(500);
        frontImage.setLayoutY(250);
    }
    public void chooseBack() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        //view.chooseStarterCardSide(false,myID);
        frontImage.setVisible(false);
        backImage.setLayoutX(500);
        backImage.setLayoutY(250);

    }
    @FXML
    public void choosePrivateGoal1() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseGoal(myID,view.showPlayerGoalOptions(myID)[0]);
    }
    public void choosePrivateGoal2() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseGoal(myID,view.showPlayerGoalOptions(myID)[1]);
    }
    public void slideUpHand(){
        handBox.setLayoutY(590);
    }
    public void slideDownHand(){
        handBox.setLayoutY(680);
    }




}
