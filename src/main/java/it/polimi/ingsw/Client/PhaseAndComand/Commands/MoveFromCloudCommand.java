package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.MoveFromCloud;

import java.awt.event.ActionEvent;

public class MoveFromCloudCommand extends GameCommand {
    public MoveFromCloudCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);

    }

    @Override
    public String toString() {
        return "Move from cloud";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
