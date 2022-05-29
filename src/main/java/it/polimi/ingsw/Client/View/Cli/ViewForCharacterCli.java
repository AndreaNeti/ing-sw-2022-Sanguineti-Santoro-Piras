package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

public interface ViewForCharacterCli {
    Color getColorInput(boolean canBeStopped) throws ScannerException;

    int getIslandDestination(String message, boolean canBeStopped) throws ScannerException;
}
