package it.polimi.ingsw.Client.View.Gui.SceneController;


import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.Node;

public interface SceneController extends GameClientListener {
    void setViewGUI(ViewGUI viewGUI);

    void hideEverything();

    void disableEverything();

    Node getElementById(String id);

    void enableNode(Node node, boolean addVisibility);
    void enableNode(Node node);
}
