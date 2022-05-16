package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;

import java.awt.event.ActionEvent;

public class JoinMatchByTypeCommand extends GameCommand {
    public JoinMatchByTypeCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.print("Select number of players:");
        int number = viewCli.getIntInput();
        viewCli.print("Do you want to play an Expert game? true/false");
        boolean isExpert = viewCli.getBooleanInput();
        viewCli.setMatchType(new MatchType((byte) number, isExpert));
    }
}
