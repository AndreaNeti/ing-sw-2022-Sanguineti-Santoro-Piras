package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;

public class PlayCharacterCardPhase extends ClientPhase {
    public PlayCharacterCardPhase() {
        super();
    }

    @Override
    public void playPhase(ViewCli viewCli) {
        CharacterCardClient selectedCharacter = viewCli.getCurrentCharacterCard();
        if (selectedCharacter == null) return;
        System.out.println("This is the selected character: " + selectedCharacter);
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
