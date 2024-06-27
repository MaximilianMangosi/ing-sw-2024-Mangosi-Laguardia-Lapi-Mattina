package it.polimi.ingsw.GUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.rmi.RemoteException;

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
    @FXML
    private Button startButton;

    private int sealedCards=0;

    public void init() throws  RemoteException {
        startButton.setVisible(false);
        Goal[] goalOptions = new Goal[0];
        try {
            goalOptions = getGoalOptions();

        int id = goalOptions[0].getId();
        Image privateOption1 = new Image(String.valueOf(getClass().getResource("/CardsFront/"+id+".png")));
        privateGoal1.setImage(privateOption1);

        id = goalOptions[1].getId();
        System.out.println(id);
        Image privateOption2 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        privateGoal2.setImage(privateOption2);

        id = view.getPublicGoals()[0].getId();
        Image publicOption1 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        publicGoal1.setImage(publicOption1);
        id = view.getPublicGoals()[1].getId();
        Image publicOption2 = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        publicGoal2.setImage(publicOption2);

        id =getStarterCard().getId();
        Image starterCardFr = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
        frontImage.setImage(starterCardFr);

        Image starterCardBk = new Image(getClass().getResourceAsStream("/CardsBack/" + id + ".png"));
        backImage.setImage(starterCardBk);
        handBox.getChildren().clear();
        for (Card card : getHand()) {
            id = card.getId();
            Image cardPng = new Image(getClass().getResourceAsStream("/CardsFront/" + id + ".png"));
            ImageView cardView = new ImageView(cardPng);
            cardView.setFitWidth(275);
            cardView.setFitHeight(193);
            handBox.getChildren().add(cardView);
        }
        } catch (InvalidUserId | IllegalOperationException ignore) {}
    }
    public void chooseFront() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, InvalidGameID {
        view.chooseStarterCardSide(true,myID);
        starterCardBox.getChildren().remove(1);
        sealedCards++;
        if (sealedCards==2){
            startButton.setVisible(true);
        }

    }
    public void chooseBack() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, InvalidGameID {
        view.chooseStarterCardSide(false,myID);
        starterCardBox.getChildren().removeFirst();
        sealedCards++;
        if (sealedCards==2){
            startButton.setVisible(true);
        }
    }

    public void choosePrivateGoal1() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, InvalidGameID {
        view.chooseGoal(myID,getGoalOptions()[0]);
        privateGoalBox.getChildren().remove(1);
        sealedCards++;
        if (sealedCards==2){
            startButton.setVisible(true);
        }
    }

    public void choosePrivateGoal2() throws InvalidUserId, IOException, InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException, InvalidGameID {
        view.chooseGoal(myID,getGoalOptions()[1]);
        privateGoalBox.getChildren().removeFirst();
        sealedCards++;
        if (sealedCards==2){
            startButton.setVisible(true);
        }
    }
    public void slideUpHand(){
        handPane.setLayoutY(830);
    }
    public void slideDownHand(){handPane.setLayoutY(1020);}
    @FXML
    private void switchToMainStage(ActionEvent event) throws InvalidUserId, IOException {
        changeScene("in-game.fxml",event);
        stage.setFullScreen(true);
    }



}
