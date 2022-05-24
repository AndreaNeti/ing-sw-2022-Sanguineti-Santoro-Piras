package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class GetDescriptionCommand extends GameCommand {
    public GetDescriptionCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        System.out.println(viewCli.getCurrentCharacterCard().getDescription());
        viewCli.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false, false);
    }

    @Override
    public String toString() {
        return "Get Description";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
