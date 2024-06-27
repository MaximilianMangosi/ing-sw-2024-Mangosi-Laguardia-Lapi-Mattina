package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ViewRMIContainer extends UnicastRemoteObject implements ViewRMIContainerInterface {
    private ConcurrentHashMap<UUID,ViewRMI> views= new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID,List<String >> joinableGames= new ConcurrentHashMap<>();
    private GameManager gameManager;

    /**
     * @author Giuseppe Laguardia
     * @return all views
     * @throws RemoteException
     */
    public Set<Map.Entry<UUID, ViewRMI>> getAllViews()throws RemoteException{
        return views.entrySet();
    }

    /**
     * @author Giuseppe Laguardia
     * @return joinable games
     * @throws RemoteException
     */
    public Map<UUID,List<String>> getJoinableGames() throws RemoteException{
       return joinableGames;
    }

    /**
     * sets the game manager
     * @author Giuseppe Laguardia
     * @param gameManager
     * @throws RemoteException
     */
    public ViewRMIContainer(GameManager gameManager) throws RemoteException {
        this.gameManager=gameManager;
    }

    /**
     * @author Giuseppe Laguardia
     * @param gameId
     * @return view
     * @throws InvalidGameID
     */
    public ViewRMIInterface getView(UUID gameId) throws InvalidGameID {
        return Optional.ofNullable(views.get(gameId)).orElseThrow(InvalidGameID::new);
    }

    /**
     * @author Giuseppe Laguardia
     * @param gameID
     * @return controller
     * @throws RemoteException
     * @throws InvalidGameID
     */
    @Override
    public Controller getController(UUID gameID) throws RemoteException, InvalidGameID {
        return getView(gameID).getController();
    }

    /**
     * boots the game
     * @author Giuseppe Laguardia
     * @param numOfPlayers
     * @param playerName
     * @return
     * @throws RemoteException
     * @throws UnacceptableNumOfPlayersException
     * @throws IllegalOperationException
     * @throws PlayerNameNotUniqueException
     */
    public GameKey bootGame(int numOfPlayers, String playerName) throws RemoteException, UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException {
        Controller controller=new Controller(gameManager);
        ViewRMI view = controller.getView();
        view.setViewContainer(this);
        GameKey gameKey= view.bootGame(numOfPlayers,playerName);
        UUID gameID = gameKey.gameID();
        views.put(gameID,view);
        joinableGames.put(gameID,view.getPlayersList());

        return gameKey;

    }

    /**
     * joins the game
     * @author Giuseppe Laguardia
     * @param gameID
     * @param playerName
     * @return
     * @throws RemoteException
     * @throws PlayerNameNotUniqueException
     * @throws IllegalOperationException
     * @throws InvalidGameID
     */
    public UUID joinGame(UUID gameID,String playerName) throws RemoteException, PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID {
        ViewRMIInterface view =  getView(gameID);
        UUID userID = view.joinGame(gameID,playerName);
        joinableGames.put(gameID,view.getPlayersList());
        if(view.isGameStarted())
            joinableGames.remove(gameID);
        return userID;

    }

    /**
     * removers the view
     * @author Giuseppe Laguardia
     * @param viewRMI
     */

    public void removeView(ViewRMI viewRMI) {
        views.values().remove(viewRMI);
    }
}
