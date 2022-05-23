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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.sendToServer(new MoveMotherNature(viewCli.getMotherNatureMovesInput(false)));
    }

    @Override
    public String toString() {
        return "Move mother nature";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
