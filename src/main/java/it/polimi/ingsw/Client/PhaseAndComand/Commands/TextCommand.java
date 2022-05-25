package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.TextMessageCS;

import java.awt.event.ActionEvent;

public class TextCommand extends GameCommand {
    public TextCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        String comment = null;
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                comment = viewCli.getStringInput("Comment", 50, false);
            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        viewCli.sendToServer(new TextMessageCS(comment));
        viewCli.addMessage("[You]: " + comment);
        viewCli.repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Send message";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
