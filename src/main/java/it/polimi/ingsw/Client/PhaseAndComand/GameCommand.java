package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.awt.event.ActionListener;

public abstract class GameCommand implements ActionListener {
    private final ViewGUI viewGUI;

    public GameCommand(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
    }
    public GameCommand(){
        this.viewGUI=null;
    }

    abstract void playCLICommand(ViewCli viewCli);

    public ViewGUI getViewGUI() {
        return viewGUI;
    }
}
