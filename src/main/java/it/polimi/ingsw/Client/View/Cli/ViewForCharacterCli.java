package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

public interface ViewForCharacterCli {
    Color getColorInput(boolean canBeStopped) throws SkipCommandException;

    int getIslandDestination(String message, boolean canBeStopped) throws SkipCommandException;
}
