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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.UUID;

public class ChooseSideController {

    UUID myID;
    View view;
    @FXML
    ImageView frontImage;
    @FXML
    ImageView backImage;

    public void setImageView(Image front,Image back){
        frontImage.setImage(front);
        backImage.setImage(back);
    }
    public void chooseFront() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseStarterCardSide(true,myID);
    }
    public void chooseBack() throws InvalidGoalException, HandFullException, InvalidChoiceException, IsNotYourTurnException, UnacceptableNumOfPlayersException, OnlyOneGameException, PlayerNameNotUniqueException, IOException, IllegalOperationException, InvalidCardException, DeckEmptyException, HandNotFullException, InvalidUserId, NoGameExistsException, RequirementsNotMetException, IllegalPositionException, ClassNotFoundException {
        view.chooseStarterCardSide(false,myID);
    }




}
