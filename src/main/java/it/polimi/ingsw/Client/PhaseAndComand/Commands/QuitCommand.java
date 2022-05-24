package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class QuitCommand extends GameCommand {

    public QuitCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean quit = false;
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                quit = viewCli.getBooleanInput("Quit?", false);

            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        if (quit) {
            viewCli.setQuit(false);
        } else
            viewCli.repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Quit";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
