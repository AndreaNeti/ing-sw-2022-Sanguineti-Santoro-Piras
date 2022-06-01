package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.SceneController.MenuController;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

public class NicknamePhase extends ClientPhase {

    public NicknamePhase() {
        super();
    }

    public void playPhase(ViewGUI viewGUI, SceneController sceneController) {
        MenuController menuController = (MenuController) sceneController;
        menuController.hideEverything();
        menuController.getElementById("#nickName").setVisible(true);
    }

    @Override
    public String toString() {
        return "Nickname Phase";
    }
}