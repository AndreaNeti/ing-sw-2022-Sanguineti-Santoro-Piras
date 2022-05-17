package it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

public interface ClientPhaseView {
    void playPhase(ViewCli viewCli);
    void playPhase(ViewGUI viewGUI);
}
