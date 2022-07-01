package it.polimi.ingsw.client.phaseAndComand.Phases;

import it.polimi.ingsw.client.model.GameComponentClient;
import it.polimi.ingsw.client.phaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.view.Gui.GuiFX;
import it.polimi.ingsw.client.view.Gui.SceneController.SceneController;
import it.polimi.ingsw.client.view.Gui.ViewGUI;
import javafx.scene.Node;

/**
 * MoveCloudPhase class represents the game phase in which the client can move students from the clouds to the entrance hall.
 */
public class MoveCloudPhase extends ClientPhase {

    /**
     * Constructor MoveCloudPhase creates a new instance of MoveCloudPhase.
     */
    public MoveCloudPhase() {
        super();
    }

    /**
     * Method playPhase disables everything except the clouds, adding the respective command on the mouse clicked event.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        for (GameComponentClient cloud : viewGUI.getModel().getClouds()) {
            if (cloud.howManyStudents() > 0) {
                Node cloudClickable = sceneController.getElementById("#" + cloud.getId());
                sceneController.enableNode(cloudClickable);
                cloudClickable.setOnMouseClicked(GameCommand.MOVE_FROM_CLOUD.getGUIHandler(viewGUI));
            }
        }
        if (getGameCommands().contains(GameCommand.CHOOSE_CHARACTER))
            viewGUI.enableChooseCharacter(GameCommand.CHOOSE_CHARACTER.getGUIHandler(viewGUI));
    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Cloud Phase".
     */
    @Override
    public String toString() {
        return "Cloud Phase";
    }
}
