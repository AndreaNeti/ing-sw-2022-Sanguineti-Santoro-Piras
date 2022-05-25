package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.network.toServerMessage.NickName;

import java.awt.event.ActionEvent;

public class SetNicknameCommand extends GameCommand {

    public SetNicknameCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() throws ScannerException {
        ViewCli viewCli = (ViewCli) getView();
        String nick = null;
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                nick = viewCli.getStringInput("Select nickname", 16, false);

            } catch (RepeatCommandException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
        viewCli.sendToServer(new NickName(nick));
    }

    @Override
    public String toString() {
        return "Set nickname";
    }
}
