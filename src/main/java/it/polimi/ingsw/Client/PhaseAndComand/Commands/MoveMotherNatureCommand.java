package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.MoveMotherNature;

import java.awt.event.ActionEvent;

public class MoveMotherNatureCommand extends GameCommand {

    public MoveMotherNatureCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                viewCli.sendToServer(new MoveMotherNature(viewCli.getMotherNatureMovesInput(false)));
            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);

    }

    @Override
    public String toString() {
        return "Move mother nature";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
