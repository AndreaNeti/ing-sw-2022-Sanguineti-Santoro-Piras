package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.Quit;

import java.awt.event.ActionEvent;

public class QuitCommand extends GameCommand {

    public QuitCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        boolean quit = viewCli.getBooleanInput("Quit?", false);
        if (quit) {
            viewCli.setQuit(false);
        } else
            viewCli.goToOldPhase(false);
    }

    @Override
    public String toString() {
        return "Quit";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
