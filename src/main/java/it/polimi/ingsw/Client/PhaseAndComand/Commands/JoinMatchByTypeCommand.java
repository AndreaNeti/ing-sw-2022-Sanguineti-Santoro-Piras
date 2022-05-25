package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.JoinMatchByType;

import java.awt.event.ActionEvent;

public class JoinMatchByTypeCommand extends GameCommand {

    public JoinMatchByTypeCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                MatchType matchType = viewCli.getMatchTypeInput(false);
                viewCli.sendToServer(new JoinMatchByType(matchType));
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);

    }

    @Override
    public String toString() {
        return "Join match by type";
    }
}
