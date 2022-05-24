package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

public class PlayCharacterCardPhase extends ClientPhase {
    public PlayCharacterCardPhase() {
        super();
    }

    @Override
    public void playPhase(ViewCli viewCli) {
        System.out.println("This is the selected character: " + viewCli.getCurrentCharacterCard().toString());
        super.playPhase(viewCli);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "Play character card phase";
    }
}
