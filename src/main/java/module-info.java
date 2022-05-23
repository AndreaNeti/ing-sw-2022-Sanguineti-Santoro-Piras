module GC45 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.fusesource.jansi;
    opens it.polimi.ingsw.Client.View.Gui to javafx.fxml;
    exports it.polimi.ingsw.Client.View.Gui;
}