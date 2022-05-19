package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.PlayCard;

import java.awt.event.ActionEvent;

public class PlayCardCommand extends GameCommand {
    public PlayCardCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput()));
    }

    @Override
    public String toString() {
        return "Play card";
    }
}
