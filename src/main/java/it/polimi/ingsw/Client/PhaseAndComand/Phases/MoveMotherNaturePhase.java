package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Server.controller.MatchType;
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
        int motherNatureRelativeId = islands.get(motherNaturePosition).getId();
        // the relative id (may be a merged one) of the last clickable island
        int farthestClickableIslandRelativeId = islands.get((motherNaturePosition + max_moves) % islands.size()).getId();
        // the island distance in steps from the relative one where mother nature is positioned
        int moves = 1;
        // from 0 to 11
        int islandId = islands.get((motherNaturePosition + 1) % islands.size()).getId();
        int islandNumber = islandId - 2 * MatchType.MAX_PLAYERS;
        Node island = sceneController.getElementById("#" + islandId);
        int relativeId = (int) island.getProperties().get("relativeId");
        System.out.println("Max moves: " + max_moves);
        // until relative id is equal to the first not reachable island
        while (farthestClickableIslandRelativeId + 1 != relativeId) {
            island.getProperties().put("moves", moves);
            System.out.println("Island " + islandNumber + " (#" + islandId + " -> #" + relativeId + "), distance: " + moves);
            // make clickable
            sceneController.enableNode(island);
            island.setOnMouseClicked(GameCommand.MOVE_MOTHER_NATURE.getGUIHandler(viewGUI));
            islandNumber++;
            islandNumber %= 12;
            islandId = 2 * MatchType.MAX_PLAYERS + islandNumber;
            island = sceneController.getElementById("#" + islandId);

            // keep in temp to check if it's different from the old relative id before assigning it
            int temp = (int) island.getProperties().get("relativeId");
            // if true, it's a new archipelago
            if (relativeId != temp)
                moves++;

            relativeId = temp;
        }
    }

    @Override
    public String toString() {
        return "Move mother nature phase";
    }
}
