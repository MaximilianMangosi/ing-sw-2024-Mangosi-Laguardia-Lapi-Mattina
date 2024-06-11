package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ViewRMIContainer extends UnicastRemoteObject implements ViewRMIContainerInterface {
    private Map<UUID,ViewRMI> views= new HashMap<>();
    private Map<UUID,List<String >> joinableGames= new HashMap<>();
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

    public UUID[] bootGame(int numOfPlayers,String playerName) throws RemoteException, UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException {
        Controller controller=new Controller(gameManager);
        ViewRMI view = controller.getView();
        //UnicastRemoteObject.exportObject(view,0);
        UUID[] ids=view.bootGame(numOfPlayers,playerName);
        UUID gameID = ids[0];

        views.put(gameID,view);
        joinableGames.put(gameID,view.getPlayersList());

        return ids;

    }
    public UUID joinGame(UUID gameID,String playerName) throws RemoteException, PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID {
        ViewRMIInterface view=  getView(gameID);
        UUID userID= view.joinGame(gameID,playerName);
        if(view.isGameStarted())
            joinableGames.remove(gameID);
        return userID;

    }
}
