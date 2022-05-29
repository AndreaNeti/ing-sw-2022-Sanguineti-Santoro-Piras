package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.PlayCharacter;
import it.polimi.ingsw.network.toServerMessage.SetCharacterInput;

import java.awt.event.ActionEvent;
import java.util.List;

public class PlayCharacterCommand extends GameCommand {
    public PlayCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) super.getView();
        CharacterCardClient current = viewCli.getCurrentCharacterCard();
        if (current == null) {
            viewCli.addMessage("No card selected");
            viewCli.goToOldPhase();
            return;
        }
        boolean confirmToPlay = false;
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                confirmToPlay = viewCli.getBooleanInput("Confirm you want to play this character card?", false);

            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        if (current.canPlay() && confirmToPlay) {
            List<Integer> inputs = current.getInputs();
            if (inputs != null)
                for (Integer i : inputs)
                    viewCli.sendToServer(new SetCharacterInput(i));
            current.resetInput();
            viewCli.sendToServer(new PlayCharacter());
        } else {
            viewCli.addMessage("Card cannot be played because it needs more input");
        }
        viewCli.unsetCurrentCharacterCard();
        viewCli.goToOldPhase();
    }

    @Override
    public String toString() {
        return "Play character";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
