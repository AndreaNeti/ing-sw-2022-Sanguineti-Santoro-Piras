package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;
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
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        MatchType mt = viewCli.getMatchTypeInput();
        viewCli.setMatchType(mt);
        viewCli.sendToServer(new CreateMatch(mt));
        // TODO: set the phase
    }

    @Override
    public String toString() {
        return "create match";
    }

}
