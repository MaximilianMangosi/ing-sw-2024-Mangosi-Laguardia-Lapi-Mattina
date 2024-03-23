package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.*;

import java.util.List;

import static java.util.Collections.shuffle;

public class Game{
    private List<Player> ListOfPlayers;
    private int numOfPlayers;
    private List<ResourceCard> resourceCardDeck;
    private List<GoldCard> goldCardDeck;
    private List<Card> visibleCards;
    private List<Goal> listOfGoal;
    private Player currentPlayer;

    //Takes an App Class (static), and shuffles the lists.

    private void startGame() {
        //building players' hands
        int i = 0;
        int j = 0;
        Player player;
        List<Card> hand;

        shuffle(resourceCardDeck);
        shuffle(goldCardDeck);
        shuffle(listOfGoal);


        for (i = 0; i < numOfPlayers; i++) {
            player = ListOfPlayers.get(i);


            //two resource cards
            for (j = 0; j < 2; j++) {
                hand.add(resourceCardDeck.getFirst());
                resourceCardDeck.removeFirst();
            }

            //one gold card
            hand.add(goldCardDeck.getFirst());
            goldCardDeck.removeFirst();

            //one starter card
            player.setStarterCard();
            player.setGoal(listOfGoal.getFirst());
            listOfGoal.removeFirst();

            //counter a 0


            //aggiungere posizione della starter card


        }



    }

}