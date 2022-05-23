package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.exceptions.PhaseChangedException;

public interface ViewForCharacterCli {
    int getColorInput(boolean canBeStopped) throws PhaseChangedException;

    int getIslandDestination(String message, boolean canBeStopped) throws PhaseChangedException;
}
