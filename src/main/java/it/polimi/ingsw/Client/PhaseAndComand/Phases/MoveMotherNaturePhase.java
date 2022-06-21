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

/**
 * MoveMotherNaturePhase class represents the game phase in which the client can move mother nature.
 */
public class MoveMotherNaturePhase extends ClientPhase {

    /**
     * Constructor MoveMotherNaturePhase creates a new instance of MoveMotherNaturePhase.
     */
    public MoveMotherNaturePhase() {
        super();
    }

    /**
     * Method playPhase disables everything except the islands where mother nature can be placed, according
     * to the allowed number of moves, and adds the respective game command on the mouse clicked event.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        GameClientView model = viewGUI.getModel();
        List<IslandClient> islands = model.getIslands();

        int max_moves = model.getCurrentPlayer().getPlayedCard().moves();
        if (model.isExtraSteps()) {
            max_moves += model.getMatchConstants().extraStep();
        }
        if (max_moves > islands.size()) max_moves = islands.size();
        byte motherNaturePosition = model.getMotherNaturePosition();
        // the island distance in steps from the relative one where mother nature is positioned
        int moves = 1;
        // from 0 to 11
        int islandId = islands.get((motherNaturePosition + 1) % islands.size()).getId();
        int islandNumber = islandId - 2 * MatchType.MAX_PLAYERS;
        Node island = sceneController.getElementById("#" + islandId);
        int relativeId = (int) island.getProperties().get("relativeId");
        System.out.println("Max moves: " + max_moves);
        // until relative id is equal to the first not reachable island
        do {
            island.getProperties().put("moves", moves);
            System.out.println("Island " + islandNumber + " (#" + islandId + " -> #" + relativeId + "), distance: " + moves);
            // make clickable
            sceneController.enableNode(island,false );
            island.setOnMouseClicked(GameCommand.MOVE_MOTHER_NATURE.getGUIHandler(viewGUI));
            islandNumber++;
            islandNumber %= 12;
            islandId = 2 * MatchType.MAX_PLAYERS + islandNumber;
            island = sceneController.getElementById("#" + islandId);

            // keep in temp to check if it's different from the old relative id before assigning it
            int temp = (int) island.getProperties().get("relativeId");
            // TODO rewrite using contained islands
            // if true, it's a new archipelago
            if (relativeId != temp)
                moves++;

            relativeId = temp;
        }while (moves <= max_moves);

        if (getGameCommands().contains(GameCommand.CHOOSE_CHARACTER)) {
            viewGUI.enableChooseCharacter(GameCommand.CHOOSE_CHARACTER.getGUIHandler(viewGUI));

        }

    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Move Mother Nature phase".
     */
    @Override
    public String toString() {
        return "Move Mother Nature phase";
    }
}
