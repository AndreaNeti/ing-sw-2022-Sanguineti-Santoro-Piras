package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.awt.event.ActionEvent;

public class SetCharacterInputCommand extends GameCommand {
    public SetCharacterInputCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        CharacterCardClient current = viewCli.getCurrentCharacterCard();
        if (current.isFull())
            viewCli.addMessage("Can't add more inputs to this character");
        else {
            boolean phaseChanged;
            do {
                phaseChanged = false;
                try {
                    current.setNextInput(viewCli);
                } catch (RepeatCommandException e) {
                    phaseChanged = true;
                }
            } while (phaseChanged);
        }
        if (current.canPlay())
            viewCli.addMessage("Card can already be played");
        viewCli.repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Set the input for the character";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
