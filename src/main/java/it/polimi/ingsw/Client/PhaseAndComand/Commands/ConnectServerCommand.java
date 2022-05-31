package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.awt.event.ActionEvent;

public class ConnectServerCommand extends GameCommand {

    @Override
    public void playCLICommand(ViewCli viewCli) throws ScannerException {

        boolean phaseChanged = false;
        boolean connected = false;
        do {
            try {
                connected = viewCli.connectToServer(viewCli.getIpAddressInput(false));
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        if (!connected) {
            viewCli.addMessage("Server Error: Cannot connect to this server");
            viewCli.repeatPhase(false);
        }
    }

    @Override
    public String toString() {
        return "Connect to server";
    }
}
