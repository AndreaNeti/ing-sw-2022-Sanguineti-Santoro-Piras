package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

import java.awt.event.ActionEvent;

public class CONNECT_SERVER extends GameCommand {
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand(ViewCli viewCli) {
        viewCli.print("Select the port of the server");
        if (!viewCli.connectToServer(new byte[]{127, 0, 0, 1}, viewCli.getIntInput()))
            viewCli.print("Cannot connect to this server");
        //TODO set the phase
    }
}
