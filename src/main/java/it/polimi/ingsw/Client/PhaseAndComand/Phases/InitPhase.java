package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.SceneController.MenuController;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.control.TextField;

public class InitPhase extends ClientPhase {
    public InitPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI, SceneController sceneController) {
        MenuController menuController =(MenuController) sceneController;
        TextField textBox=menuController.getTextBoxInput();
        textBox.setVisible(true);
        //textBox.setEditable(true);
        //textBox.setPromptText();
    }

    @Override
    public String toString() {
        return "Init Phase";
    }
}
