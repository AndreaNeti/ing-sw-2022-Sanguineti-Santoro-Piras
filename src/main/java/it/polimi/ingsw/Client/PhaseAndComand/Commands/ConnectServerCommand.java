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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        if (!viewCli.connectToServer(viewCli.getIpAddressInput(), viewCli.getServerPortInput())) {
            System.err.println("Cannot connect to this server");
            viewCli.repeatPhase(false);
        } else
            System.out.println("Connected with server");
    }

    @Override
    public String toString() {
        return "Connect to server";
    }
}
