package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

public interface ViewForCharacterCli {
    int getColorInput(boolean canBeStopped) throws ScannerException;

    int getIslandDestination(String message, boolean canBeStopped) throws ScannerException;
}
