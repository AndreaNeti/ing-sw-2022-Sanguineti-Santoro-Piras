package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        Long ID = viewCli.getLongInput("Write game ID", false);
        viewCli.sendToServer(new JoinMatchById(ID));
    }

    @Override
    public String toString() {
        return "Join match by id";
    }
}
