package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.InvalidGameID;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ViewRMIContainerInterface extends Remote {
     ViewRMIInterface getView(UUID gameID) throws RemoteException, InvalidGameID;
     Set<Map.Entry<UUID, ViewRMI>> getAllViews() throws RemoteException;
     Map<UUID, List<String>> getJoinableGames() throws RemoteException;
     Controller getController(UUID gameID) throws RemoteException, InvalidGameID;
     GameKey bootGame(int numOfPlayers, String playerName) throws RemoteException, UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException;
     UUID joinGame(UUID gameID,String playerName) throws RemoteException, PlayerNameNotUniqueException, IllegalOperationException, InvalidGameID;

}
