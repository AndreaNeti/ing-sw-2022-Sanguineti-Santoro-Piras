package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.PlayCharacter;
import it.polimi.ingsw.network.toServerMessage.SetCharacterInput;

import java.awt.event.ActionEvent;
import java.util.List;

public class PlayCharacterCommand extends GameCommand {
    public PlayCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) super.getView();
        CharacterCardClient current = viewCli.getCurrentCharacterCard();
        if (current.canPlay() && viewCli.getBooleanInput("Confirm you want to play this character card?", false)) {
            List<Integer> inputs = current.getInputs();
            if (inputs != null)
                for (Integer i : inputs)
                    viewCli.sendToServer(new SetCharacterInput(i));
            current.resetInput();
            viewCli.sendToServer(new PlayCharacter());

        } else {
            System.out.println("Card cannot be played because it needs more input");
            viewCli.goToOldPhase(false);
        }
    }

    @Override
    public String toString() {
        return "Play character";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
