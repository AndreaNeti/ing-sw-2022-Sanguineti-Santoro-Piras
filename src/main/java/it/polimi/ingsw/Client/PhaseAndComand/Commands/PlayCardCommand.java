package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.MoveFromCloud;
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
        boolean phaseChanged;
        do {
            phaseChanged = false;
            try {
                viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput(false)));
            } catch (PhaseChangedException e) {
                phaseChanged = true;
            }
        } while (phaseChanged);
    }

    @Override
    public String toString() {
        return "Play card";
    }
}
