package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class ChooseCharacterCommand extends GameCommand{
    public ChooseCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
//        viewCli.getChoosenCharacterInput();
   }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
