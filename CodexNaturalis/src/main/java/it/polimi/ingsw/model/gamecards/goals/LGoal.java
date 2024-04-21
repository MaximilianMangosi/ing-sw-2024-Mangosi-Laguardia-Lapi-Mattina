package it.polimi.ingsw.model.gamecards.goals;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamecards.resources.Reign;

public class LGoal extends Goal {
    public enum  PrimaryPosition {
        TOPRIGHT,
        TOPLEFT,
        BOTTOMRIGHT,
        BOTTOMLEFT,
    }

    private Reign primaryReign;
    private Reign secondaryReign;

    public Reign getPrimaryReign() {
        return primaryReign;
    }

    public Reign getSecondaryReign() {
        return secondaryReign;
    }

    public PrimaryPosition getPrimaryPosition() {
        return primaryPosition;
    }

    private PrimaryPosition primaryPosition;
}
