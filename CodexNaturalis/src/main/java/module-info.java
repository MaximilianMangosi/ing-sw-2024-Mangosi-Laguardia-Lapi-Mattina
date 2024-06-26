module it.polimi.ingsw {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;
    requires java.rmi;
    opens it.polimi.ingsw.GUI to javafx.fxml,javafx.graphics;
    exports it.polimi.ingsw.GUI;
    opens it.polimi.ingsw.model.gamecards.cards to com.google.gson;
    opens it.polimi.ingsw.model.gamecards.resources to com.google.gson;
    opens it.polimi.ingsw.model.gamecards.goals to com.google.gson;
    opens it.polimi.ingsw.view to java.rmi;
}