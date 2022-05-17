package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
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
    public void playCLICommand() {
        ViewCli viewCli = (ViewCli) getView();
        viewCli.print("Insert the number of the assistant card you want to play");
        viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput()));
    }
}
