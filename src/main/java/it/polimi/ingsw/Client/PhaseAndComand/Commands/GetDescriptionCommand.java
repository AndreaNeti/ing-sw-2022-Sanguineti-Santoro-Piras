package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

import java.awt.event.ActionEvent;

public class GetDescriptionCommand extends GameCommand {
    public GetDescriptionCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand()  {
        ViewCli viewCli = (ViewCli) getView();
        System.out.println(viewCli.getCurrentCharacterCard().getDescription());
        viewCli.repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Get Description";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
