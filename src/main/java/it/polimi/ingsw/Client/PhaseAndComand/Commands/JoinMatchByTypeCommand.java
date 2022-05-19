package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.PhaseChangedException;
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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        MatchType matchType = viewCli.getMatchTypeInput();
        viewCli.setMatchType(matchType);
        viewCli.sendToServer(new JoinMatchByType(matchType));
    }

    @Override
    public String toString() {
        return "Join match by type";
    }
}
