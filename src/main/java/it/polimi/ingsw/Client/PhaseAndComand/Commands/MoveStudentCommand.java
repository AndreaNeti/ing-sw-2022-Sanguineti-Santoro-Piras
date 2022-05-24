package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.MoveFromCloud;
import it.polimi.ingsw.network.toServerMessage.MoveStudent;

import java.awt.event.ActionEvent;

public class MoveStudentCommand extends GameCommand {
    public MoveStudentCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                Color color = Color.values()[viewCli.getColorInput(false)];
                viewCli.sendToServer(new MoveStudent(color, viewCli.getMoveStudentDestination(false)));
            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
    }

    @Override
    public String toString() {
        return "Move student";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
