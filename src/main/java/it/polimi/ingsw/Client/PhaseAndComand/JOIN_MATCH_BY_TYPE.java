package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.MatchType;

import java.awt.event.ActionEvent;

public class JOIN_MATCH_BY_TYPE extends GameCommand{
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand( ViewCli viewCli) {
        viewCli.print("Select number of players:");
        int number=viewCli.getIntInput();
        viewCli.print("Do you want to play an Expert game? true/false");
        boolean isExpert=viewCli.getBooleanInput();
        viewCli.setMatchType(new MatchType((byte) number,isExpert));
    }
}
