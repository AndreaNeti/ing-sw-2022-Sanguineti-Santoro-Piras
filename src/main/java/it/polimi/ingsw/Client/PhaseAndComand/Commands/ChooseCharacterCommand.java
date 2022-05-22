package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.exceptions.PhaseChangedException;
import it.polimi.ingsw.network.toServerMessage.ChooseCharacter;

import java.awt.event.ActionEvent;
import java.util.List;

public class ChooseCharacterCommand extends GameCommand {
    public ChooseCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        ViewCli viewCli = (ViewCli) getView();
        List<CharacterCardClient> characters = viewCli.getModel().getCharacters();
        int index = viewCli.getIntInput((characters.toArray()), "Select the character you want to play");
        CharacterCardClient current = characters.get(index);
        viewCli.setCurrentCharacterCard(current);
        viewCli.sendToServer(new ChooseCharacter((byte) index));
        viewCli.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false);

    }

    @Override
    public String toString() {
        return "Choose a character card";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
