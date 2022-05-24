package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class ConnectServerCommand extends GameCommand {

    public ConnectServerCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged = false;
        boolean connected=false;
        do {
            try {
                connected=viewCli.connectToServer(viewCli.getIpAddressInput(false));
            } catch (PhaseChangedException e) {
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
