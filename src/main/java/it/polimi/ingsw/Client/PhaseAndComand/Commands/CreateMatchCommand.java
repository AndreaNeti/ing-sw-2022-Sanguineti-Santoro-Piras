package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.CreateMatch;

import java.awt.event.ActionEvent;

public class CreateMatchCommand extends GameCommand {
    public CreateMatchCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.sendToServer(new CreateMatch(viewCli.getMatchTypeInput()));
    }

    @Override
    public String toString() {
        return "Create match";
    }

}
