package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.TextMessageCS;

import java.awt.event.ActionEvent;

public class TextCommand extends GameCommand {
    public TextCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        String comment = viewCli.getStringInput("Comment", false);
        viewCli.sendToServer(new TextMessageCS(comment));
        viewCli.goToOldPhase(false);
    }

    @Override
    public String toString() {
        return "Send message";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
