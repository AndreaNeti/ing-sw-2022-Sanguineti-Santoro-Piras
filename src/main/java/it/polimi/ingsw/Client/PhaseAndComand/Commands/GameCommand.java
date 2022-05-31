package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.awt.event.ActionListener;
import java.util.List;

public abstract class GameCommand {

    public abstract void playCLICommand(ViewCli viewCli) throws ScannerException;

    public abstract String toString();

    public void attachToAPhase(List<ClientPhase> clientPhases) {
        for (ClientPhase clientPhase : clientPhases) {
            clientPhase.addCommand(this);
        }
    }
}
