package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class ChooseSideController extends GUIController {


    @FXML
    ImageView frontImage;
    @FXML
    ImageView backImage;
    @FXML
    HBox starterCardBox;
    @FXML
    HBox handBox;
    @FXML
    HBox privateGoalBox;
    @FXML
    StackPane handPane;
    @FXML
    ImageView privateGoal1;
    @FXML
    ImageView privateGoal2;
    @FXML
    ImageView publicGoal1;
    @FXML
    ImageView publicGoal2;


    public void init() throws InvalidUserId, RemoteException {
        int id = view.showPlayerGoalOptions(myID)[0].getId();
        Image privateOption1 = new Image(String.valueOf(getClass().getResource("/CardsFront/"+id+".png")));
        privateGoal1.setImage(privateOption1);

        id = view.showPlayerGoalOptions(myID)[1].getId();
        Image privateOption2 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        privateGoal2.setImage(privateOption2);

        id = view.getPublicGoals()[0].getId();
        Image publicOption1 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        publicGoal1.setImage(publicOption1);
        id = view.getPublicGoals()[1].getId();
        Image publicOption2 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        publicGoal2.setImage(publicOption2);

        id = view.getStarterCard(myID).getId();
        Image starterCardFr = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        frontImage.setImage(starterCardFr);

        Image starterCardBk = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"));
        backImage.setImage(starterCardBk);
        handBox.getChildren().clear();
        for (Card card : view.showPlayerHand(myID)) {
            id = card.getId();
            Image cardPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView cardView = new ImageView(cardPng);
            cardView.setFitWidth(275);
            cardView.setFitHeight(193);
            handBox.getChildren().add(cardView);
        }

    }
    public void setImageView(Image front,Image back){
        frontImage.setImage(front);
        backImage.setImage(back);
    }
    public void chooseFront() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
       view.chooseStarterCardSide(true,myID);
       starterCardBox.getChildren().remove(1);

    }
    public void chooseBack() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseStarterCardSide(false,myID);
        starterCardBox.getChildren().removeFirst();
    }
    @FXML
    public void choosePrivateGoal1() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseGoal(myID,view.showPlayerGoalOptions(myID)[0]);
        privateGoalBox.getChildren().removeFirst();
    }
    public void choosePrivateGoal2() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseGoal(myID,view.showPlayerGoalOptions(myID)[1]);
        privateGoalBox.getChildren().remove(1);
    }
    public void slideUpHand(){
        handPane.setLayoutY(840);
    }
    public void slideDownHand(){handPane.setLayoutY(1023);
    }

    private void switchToMainStage(ActionEvent event) throws InvalidUserId, IOException {
        if(view.showPrivateGoal(myID)!=null && view.getPlayersField(myName).containsValue(view.getStarterCard(myID))){
            changeScene("in-game.fxml",event);
        }
    }




}
