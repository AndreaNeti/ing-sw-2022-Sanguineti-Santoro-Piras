module GC45 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.fusesource.jansi;
    opens it.polimi.ingsw.Client.View.Gui to javafx.controls, javafx.fxml, javafx.graphics, javafx.base;
    opens it.polimi.ingsw.Client.View.Gui.SceneController to javafx.controls, javafx.fxml, javafx.graphics, javafx.base;
    opens it.polimi.ingsw.Client.View to javafx.controls, javafx.fxml, javafx.graphics, javafx.base;
    opens it.polimi.ingsw.Client.PhaseAndComand.Phases to javafx.controls, javafx.fxml, javafx.graphics, javafx.base;
    opens it.polimi.ingsw.Client.PhaseAndComand.Commands to javafx.controls, javafx.fxml, javafx.graphics, javafx.base;
    exports it.polimi.ingsw.Client.View.Gui;
    exports it.polimi.ingsw.Client.PhaseAndComand.Phases;
    exports it.polimi.ingsw.Client.PhaseAndComand.Commands;
    exports it.polimi.ingsw.Client.View.Gui.SceneController;
    exports it.polimi.ingsw.Client.View;
    exports it.polimi.ingsw.Client;
}