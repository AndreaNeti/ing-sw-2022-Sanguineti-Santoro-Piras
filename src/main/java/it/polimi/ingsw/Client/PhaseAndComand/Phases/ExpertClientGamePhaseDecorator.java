package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpertClientGamePhaseDecorator extends AbstractClientGamePhase {
    private final AbstractClientGamePhase decorated;

    public ExpertClientGamePhaseDecorator(AbstractClientGamePhase toDecorate, List<GameCommand> expertCommands) {
        super(expertCommands);
        decorated = toDecorate;
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        decorated.playPhase(viewGUI);
    }

    @Override
    public List<GameCommand> getGameCommands() {
        return Stream.concat(decorated.getGameCommands().stream(), super.getGameCommands().stream()).collect(Collectors.toList());
    }
}
