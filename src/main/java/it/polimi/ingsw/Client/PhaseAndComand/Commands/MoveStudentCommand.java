package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.MoveStudent;

import java.awt.event.ActionEvent;

public class MoveStudentCommand extends GameCommand {
    public MoveStudentCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        Color color = null;
        Integer destination = null;
        boolean phaseChanged;
        // request the student color
        do {
            phaseChanged = false;
            try {
                color = viewCli.getColorInput(false);
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        // request the destination where you want the student to move
        do {
            phaseChanged = false;
            try {
                destination = viewCli.getMoveStudentDestination(false);
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        viewCli.sendToServer(new MoveStudent(color, destination));
    }

    @Override
    public String toString() {
        return "Move student";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
