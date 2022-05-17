package it.polimi.ingsw.Client.PhaseAndComand.Commands;

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
        System.out.println("Select student color:");

        //viewCli.sendToServer(new MoveStudent());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
