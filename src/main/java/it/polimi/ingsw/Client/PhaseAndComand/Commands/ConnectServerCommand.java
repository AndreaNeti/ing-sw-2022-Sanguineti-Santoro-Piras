package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

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
        if (!viewCli.connectToServer(viewCli.getIpAddressInput(), viewCli.getServerPortInput()))
            viewCli.print("Cannot connect to this server");
        else {
            System.out.println("Connected with server");
            viewCli.setNewPhase();
        }
    }

    @Override
    public String toString() {
        return "Connect to server";
    }
}
