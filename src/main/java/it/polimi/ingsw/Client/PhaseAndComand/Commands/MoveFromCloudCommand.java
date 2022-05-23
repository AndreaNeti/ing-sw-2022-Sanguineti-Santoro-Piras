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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
    }

    @Override
    public String toString() {
        return "Move from cloud";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
