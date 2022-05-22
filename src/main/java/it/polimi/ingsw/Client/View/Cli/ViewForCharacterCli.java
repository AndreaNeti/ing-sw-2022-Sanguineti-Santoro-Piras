package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.exceptions.PhaseChangedException;

public interface ViewForCharacterCli {
    int getColorInput() throws PhaseChangedException;
    int getIslandDestination(String message) throws PhaseChangedException;
}
