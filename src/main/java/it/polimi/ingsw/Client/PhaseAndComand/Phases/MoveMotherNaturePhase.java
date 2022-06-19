package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Client.model.IslandClient;
import javafx.scene.Node;

import java.util.List;

public class MoveMotherNaturePhase extends ClientPhase {

    public MoveMotherNaturePhase() {
        super();
    }

    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        GameClientView model = viewGUI.getModel();
        int max_moves = model.getCurrentPlayer().getPlayedCard().moves();
        if (model.isExtraSteps()) {
            max_moves += model.getMatchConstants().extraStep();
        }
        List<IslandClient> islands = model.getIslands();
        byte motherNaturePosition = model.getMotherNaturePosition();
        for (int i = 1; i <= max_moves; i++) {
            int idClickableIsland = islands.get((motherNaturePosition + i) % islands.size()).getId();
            Node island = sceneController.getElementById("#" + idClickableIsland);
            island.getProperties().put("moves", i);
            sceneController.enableNode(island);
            island.setOnMouseClicked(GameCommand.MOVE_MOTHER_NATURE.getGUIHandler(viewGUI));
        }
    }

    @Override
    public String toString() {
        return "Move mother nature phase";
    }
}
