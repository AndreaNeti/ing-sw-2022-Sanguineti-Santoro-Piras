package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
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
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        String nick = viewCli.getStringInput("Select nickname");
        viewCli.sendToServer(new NickName(nick));
    }

    @Override
    public String toString() {
        return "Set nickname";
    }
}
