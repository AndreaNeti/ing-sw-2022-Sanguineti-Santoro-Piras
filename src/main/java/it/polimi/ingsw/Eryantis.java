package it.polimi.ingsw;

import it.polimi.ingsw.Client.AppCLI;
import it.polimi.ingsw.Client.AppGUI;
import it.polimi.ingsw.Server.controller.Server;

public class Eryantis {
    public static void main(String[] args) {

        boolean cliParam = false; // default value
        boolean serverParam = false;
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = true;
                break;
            }
            if (arg.equals("--server") || arg.equals("-s")) {
                serverParam = true;
            }
        }

        if (cliParam) {
            AppCLI.main(args);
        } else if (serverParam) {
            Server.main(args);
        } else {
            AppGUI.main(args);
        }

    }
}
