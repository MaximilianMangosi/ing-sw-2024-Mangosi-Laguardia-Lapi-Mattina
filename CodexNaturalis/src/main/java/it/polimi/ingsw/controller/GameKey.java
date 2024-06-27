package it.polimi.ingsw.controller;

import java.io.Serializable;
import java.util.UUID;

/**
 * the record class containing the userID and the gameID
 * @param gameID the ID of the game played by th user
 * @param userID the ID of the player
 */
public record GameKey(UUID gameID, UUID userID) implements Serializable { }
