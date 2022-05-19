package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.MoveStudent;

import java.awt.event.ActionEvent;

public class MoveStudentCommand extends GameCommand {
    public MoveStudentCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        Color color = Color.values()[viewCli.getColorInput()];
        viewCli.sendToServer(new MoveStudent(color, viewCli.getMoveStudentDestination()));
    }

    @Override
    public String toString() {
        return "Move student";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
