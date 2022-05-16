package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

import java.awt.event.ActionEvent;

public class MoveStudentCommand extends GameCommand {
    public MoveStudentCommand(AbstractView view) {
        super(view);
    }

    @Override
    void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
