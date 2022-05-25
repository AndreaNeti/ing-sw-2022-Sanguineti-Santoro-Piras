package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.ChooseCharacter;

import java.awt.event.ActionEvent;
import java.util.List;

public class ChooseCharacterCommand extends GameCommand {
    public ChooseCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        List<CharacterCardClient> characters = viewCli.getModel().getCharacters();
        int index = 0;
        boolean phaseChanged = false;
        do {
            try {
                index = viewCli.getCharacterCharToPlayInput();
            } catch (RepeatCommandException e) {
                phaseChanged=true;
            }
        } while (phaseChanged);
        viewCli.setCurrentCharacterCard(index);
        viewCli.sendToServer(new ChooseCharacter((byte) characters.get(index).getCharId()));
        viewCli.repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Choose a character card";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
