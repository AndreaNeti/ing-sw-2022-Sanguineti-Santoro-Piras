package it.polimi.ingsw.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;

public class Server {
    public static HashMap<MatchType, HashMap<Integer, List<Controller>>> matches;

    public static void main(String[] args) {
        matches = new HashMap<>();
        ServerSocket server = null;
        try {
            server = new ServerSocket(42069);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                new Thread(new PlayerHandler(server.accept())).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
