package it.polimi.ingsw.client.view.gui.SceneController;


import it.polimi.ingsw.client.GameClientListener;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import javafx.scene.Node;

public interface SceneController extends GameClientListener {
    /**
     * Method setViewGUI sets view GUI in the scene controller
     *
     * @param viewGUI of type {@code ViewGUI} - instance of the client's view (GUI).
     */
    void setViewGUI(ViewGUI viewGUI);

    /**
     * Method hideEverything hides nodes in the scene
     */
    void hideEverything();

    /**
     * Method disableEverything disables nodes in the scene
     */
    void disableEverything();

    /**
     * Method getElementById retrieves a Node given its id
     *
     * @param id of type {@code String} - the Node's id.
     * @return {@code Node} - the Node with the given id (if exists).
     */
    Node getElementById(String id);

    /**
     * Method enableNode enables the given node and sets it clickable with a shadow animation
     *
     * @param node          of type {@code Node} - the node you want to be enabled.
     * @param addVisibility of type {@code boolean} - if true also sets the node visible.
     */
    void enableNode(Node node, boolean addVisibility);

    /**
     * Method selectNode sets a node as selected
     *
     * @param node of type {@code Node} - the node you want to be selected.
     */
    void selectNode(Node node);

    /**
     * Method enableNode enables the given node and sets it clickable with a shadow animation
     *
     * @param node of type {@code Node} - the node you want to be enabled.
     */
    void enableNode(Node node);
}
