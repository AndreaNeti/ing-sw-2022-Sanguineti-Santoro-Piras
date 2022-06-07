package it.polimi.ingsw.Client.View.Gui.SceneController;


import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.Node;

public interface SceneController extends GameClientListener {
    void setViewGUI(ViewGUI viewGUI);

    void hideEverything();

    Node getElementById(String id);
}
