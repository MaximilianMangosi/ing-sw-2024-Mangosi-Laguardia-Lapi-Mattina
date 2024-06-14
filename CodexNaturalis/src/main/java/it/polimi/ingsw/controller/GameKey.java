package it.polimi.ingsw.controller;

import java.io.Serializable;
import java.util.UUID;

public record GameKey(UUID gameID, UUID userID) implements Serializable { }
