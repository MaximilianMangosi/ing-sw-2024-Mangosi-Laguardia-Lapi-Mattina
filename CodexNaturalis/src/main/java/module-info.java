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
}