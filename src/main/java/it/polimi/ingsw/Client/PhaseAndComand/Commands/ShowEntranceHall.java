package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class ShowEntranceHall extends GameCommand {
    public ShowEntranceHall(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.printEntranceHall(viewCli.getIntInput(0,3,"Select the player of which you want to see the entrance hall", false));
        viewCli.goToOldPhase(true);
    }

    @Override
    public String toString() {
        return "Show Entrance Halls";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
