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

    public Set<Map.Entry<UUID, ViewRMI>> getAllViews()throws RemoteException{
        return views.entrySet();
    }
    public Map<UUID,List<String>> getJoinableGames() throws RemoteException{
       return joinableGames;
    }

    public ViewRMIContainer(GameManager gameManager) throws RemoteException {
        this.gameManager=gameManager;
    }

    public ViewRMIInterface getView(UUID gameId) throws InvalidGameID {
        return Optional.ofNullable(views.get(gameId)).orElseThrow(InvalidGameID::new);
    }

    @Override
    public Controller getController(UUID gameID) throws RemoteException, InvalidGameID {
        return getView(gameID).getController();
    }

    public GameKey bootGame(int numOfPlayers, String playerName) throws RemoteException, UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException {
        Controller controller=new Controller(gameManager);
        ViewRMI view = controller.getView();
        //UnicastRemoteObject.exportObject(view,0);
        view.setViewContainer(this);
        GameKey gameKey=view.bootGame(numOfPlayers,playerName);
        UUID gameID = gameKey.gameID();

        views.put(gameID,view);
        joinableGames.put(gameID,view.getPlayersList());

        return gameKey;

    }
    public UUID joinGame(UUID gameID,String playerName) throws RemoteException, PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID {
        ViewRMIInterface view=  getView(gameID);
        UUID userID= view.joinGame(gameID,playerName);
        if(view.isGameStarted())
            joinableGames.remove(gameID);
        return userID;

    }

    public void removeView(ViewRMI viewRMI) {
        views.values().remove(viewRMI);
    }
}
