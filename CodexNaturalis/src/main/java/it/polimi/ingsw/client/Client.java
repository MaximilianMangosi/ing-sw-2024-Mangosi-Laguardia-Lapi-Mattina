package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {
    private final String[] commands = {"start-game", "play-card", "choose-goal","choose-starter-card-side", "draw-card-from-deck", "draw-card-visible", "disconnect" };
    private UUID myUid;

    /**
     * @author Riccardo Lapi, Giuseppe Laguardia
     * main function of the Client, connect to the server and listen to users commands
     * @param args
     */
    public static void main(String[] args) {
        Client client=new Client();
        /*
        * startGame
        * playCard
        * chooseGoal
        * chooseStarterCardSide
        * drawCardFromDeck
        * drawCardVisible
        * disconnect
        * */

        try{
            Registry registry = LocateRegistry.getRegistry( 1099);
            ViewInterface view = (ViewInterface) registry.lookup("ViewRMI");

            Scanner s=new Scanner(System.in);
            //System.out.println("\033c");
            System.out.println("Welcome to\n");
            System.out.println("      ...                  ....                ....                ..      .                  ..                                                                                                 \n" +
                    "   xH88\"`~ .x8X        .x~X88888Hx.        .xH888888Hx.         x88f` `..x88. .>    .H88x.  :~)88:                                                                                               \n" +
                    " :8888   .f\"8888Hf    H8X 888888888h.    .H8888888888888:     :8888   xf`*8888%    x888888X ~:8888                                                                                               \n" +
                    ":8888>  X8L  ^\"\"`    8888:`*888888888:   888*\"\"\"?\"\"*88888X   :8888f .888  `\"`     ~   \"8888X  %88\"                                                                                               \n" +
                    "X8888  X888h         88888:        `%8  'f     d8x.   ^%88k  88888' X8888. >\"8x        X8888                                                                                                     \n" +
                    "88888  !88888.     . `88888          ?> '>    <88888X   '?8  88888  ?88888< 888>    .xxX8888xxxd>                                                                                                \n" +
                    "88888   %88888     `. ?888%           X  `:..:`888888>    8> 88888   \"88888 \"8%    :88888888888\"                                                                                                 \n" +
                    "88888 '> `8888>      ~*??.            >         `\"*88     X  88888 '  `8888>       ~   '8888                                                                                                     \n" +
                    "`8888L %  ?888   !  .x88888h.        <     .xHHhx..\"      !  `8888> %  X88!       xx.  X8888:    .                                                                                               \n" +
                    " `8888  `-*\"\"   /  :\"\"\"8888888x..  .x     X88888888hx. ..!    `888X  `~\"\"`   :   X888  X88888x.x\"                                                                                                \n" +
                    "   \"888.      :\"   `    `*888888888\"     !   \"*888888888\"       \"88k.      .~    X88% : '%8888\"                                                                                                  \n" +
                    "     `\"\"***~\"`             \"\"***\"\"              ^\"***\"`           `\"\"*==~~`       \"*=~    `\"\"                                                                                                    \n" +
                    "                       ...     ...          ..                  .....                                 ..      ...          ..                    ...          .....     .         ...            \n" +
                    "                    .=*8888n..\"%888:     :**888H: `: .xH\"\"   .H8888888h.  ~-.    x8h.     x8.      :~\"8888x :\"%888x     :**888H: `: .xH\"\"    .zf\"` `\"tu     .d88888Neu. 'L    .x888888hx    :    \n" +
                    "                   X    ?8888f '8888    X   `8888k XX888     888888888888x  `> :88888> .x8888x.   8    8888Xf  8888>   X   `8888k XX888     x88      '8N.   F\"\"\"\"*8888888F   d88888888888hxx     \n" +
                    "                   88x. '8888X  8888>  '8hx  48888 ?8888    X~     `?888888hx~  `8888   `8888f   X88x. ?8888k  8888X  '8hx  48888 ?8888     888k     d88&  *      `\"*88*\"   8\" ... `\"*8888%`     \n" +
                    "                  '8888k 8888X  '\"*8h. '8888 '8888 `8888    '      x8.^\"*88*\"    8888    8888'   '8888L'8888X  '%88X  '8888 '8888 `8888     8888N.  @888F   -....    ue=:. !  \"   ` .xnxx.       \n" +
                    "                   \"8888 X888X .xH8     %888>'8888  8888     `-:- X8888x         8888    8888     \"888X 8888X:xnHH(``  %888>'8888  8888     `88888 9888%           :88N  ` X X   .H8888888%:     \n" +
                    "                     `8\" X888!:888X       \"8 '888\"  8888          488888>        8888    8888       ?8~ 8888X X8888      \"8 '888\"  8888       %888 \"88F            9888L   X 'hn8888888*\"   >    \n" +
                    "                    =~`  X888 X888X      .-` X*\"    8888        .. `\"88*         8888    8888     -~`   8888> X8888     .-` X*\"    8888        8\"   \"*h=~   uzu.   `8888L  X: `*88888%`     !    \n" +
                    "                     :h. X8*` !888X        .xhx.    8888      x88888nX\"      .   8888    8888     :H8x  8888  X8888       .xhx.    8888      z8Weu        ,\"\"888i   ?8888  '8h.. ``     ..x8>    \n" +
                    "                    X888xX\"   '8888..:   .H88888h.~`8888.>   !\"*8888888n..  :  -n88888x>\"88888x-  8888> 888~  X8888     .H88888h.~`8888.>   \"\"88888i.   Z 4  9888L   %888>  `88888888888888f     \n" +
                    "                  :~`888f     '*888*\"   .~  `%88!` '888*~   '    \"*88888888*     `%888\"  4888!`   48\"` '8*~   `8888!`  .~  `%88!` '888*~   \"   \"8888888*  '  '8888   '88%    '%8888888888*\"      \n" +
                    "                      \"\"        `\"`           `\"     \"\"             ^\"***\"`        `\"      \"\"      ^-==\"\"      `\"\"           `\"     \"\"           ^\"**\"\"        \"*8Nu.z*\"        ^\"****\"\"`        \n" +
                    "                                                                                                                                                                                                 \n" +
                    "                                                                                                                                                                                                 \n" +
                    "                                                                                                                                                                                                 ");
            System.out.println("\n\n");
            s.nextLine();
            System.out.println("Lets' start! Type 'start-game' to start a game\n");

            TextUserInterface tui= new TextUserInterface(view);


            while (true) {
                    try {
                        tui.execCmd(s.nextLine().toLowerCase(Locale.ROOT));

                    } catch (InvalidUserId | InvalidGoalException |
                             IllegalOperationException | HandNotFullException |
                             IsNotYourTurnException | RequirementsNotMetException | IllegalPositionException |
                             InvalidCardException | HandFullException | InvalidChoiceException | DeckEmptyException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }catch (RemoteException | NotBoundException e){
            System.out.println("Connection error");
        }

    }



}
