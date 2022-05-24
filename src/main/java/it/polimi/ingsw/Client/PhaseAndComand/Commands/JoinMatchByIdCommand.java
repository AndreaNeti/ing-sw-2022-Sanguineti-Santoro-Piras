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
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                Long ID = viewCli.getLongInput("Write game ID", false);
                viewCli.sendToServer(new JoinMatchById(ID));
            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
    }

    @Override
    public String toString() {
        return "Join match by ID";
    }
}
