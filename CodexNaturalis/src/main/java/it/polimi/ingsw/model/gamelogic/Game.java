package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.*;

import java.util.ArrayList;
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
        Coordinates origin = new Coordinates(0,0);
        Player player;
        //Mi manca un attimo come funziona APP ,cioè devo fare una copia dei mazzi da APP e poi fare lo shuffle

        List<String> Colors= new ArrayList<String>();
        Colors.add("Red");
        Colors.add("Blue");
        Colors.add("Yellow");
        Colors.add("Green");


        shuffle(resourceCardDeck);
        shuffle(goldCardDeck);
        shuffle(listOfGoal);


        for (i = 0; i < numOfPlayers; i++) {

            player = ListOfPlayers.get(i);
            player.setname("Player"+i);
            player.setColor(Colors.get(i));
            player.setPoints(0);


            //two resource cards
            for (j = 0; j < 2; j++) {
                player.addCardToHand(resourceCardDeck.removeFirst());

            }

            //one gold card
            player.addCardToHand(goldCardDeck.removeFirst());


            //one starter card
            player.setStarterCard();
            player.setGoal(listOfGoal.removeFirst());



            //aggiungere posizione della starter card



        }
        //Builds a list of public goals

        //Builds the visiblecardsdeck
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(goldCardDeck.removeFirst());
        }
        for ( i=0 ; i< 2 ; i++) {
            visibleCards.add(resourceCardDeck.removeFirst());
        }



    }

}