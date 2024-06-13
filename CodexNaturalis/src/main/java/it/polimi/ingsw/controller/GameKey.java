package it.polimi.ingsw.controller;

import java.util.UUID;

public record GameKey(UUID gameID, UUID userID) { }
