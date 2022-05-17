package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.network.toServerMessage.JoinMatchById;

import java.awt.event.ActionEvent;

public class JoinMatchByIdCommand extends GameCommand {

    public JoinMatchByIdCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        Long ID = viewCli.getLongInput("Write game ID");
        viewCli.sendToServer(new JoinMatchById(ID));
    }

    @Override
    public String toString() {
        return "join match by id";
    }
}
