package it.polimi.ingsw;

import it.polimi.ingsw.client.AppCLI;
import it.polimi.ingsw.client.AppGUI;
import it.polimi.ingsw.server.controller.Server;

public class Eriantys {
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                AppCLI.main(args);
                return;
            } else if (arg.equals("--server") || arg.equals("-s")) {
                Server.main(args);
                return;
            }
        }
        AppGUI.main(args);
    }
}
