package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.MoveFromCloud;

import java.awt.event.ActionEvent;

public class MoveFromCloudCommand extends GameCommand {

    @Override
    public void playCLICommand(ViewCli viewCli) throws ScannerException {
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);

    }

    @Override
    public String toString() {
        return "Move from cloud";
    }

}
