package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.CreateMatch;

import java.awt.event.ActionEvent;

public class CreateMatchCommand extends GameCommand {
    @Override
    public void playCLICommand(ViewCli viewCli) throws ScannerException {
        boolean phaseChanged = false;
        do {
            try {
                viewCli.sendToServer(new CreateMatch(viewCli.getMatchTypeInput(false)));
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);

    }

    @Override
    public String toString() {
        return "Create match";
    }

}
