package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Server {
    public static HashMap<MatchType, LinkedHashMap<Long, Controller>> matches;
    public static Long matchId = 0L;

    public static void main(String[] args) {
        matches = new HashMap<>();
        ServerSocket server;
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

    public static Controller createMatch(MatchType matchType) {
        LinkedHashMap<Long, Controller> filteredMatches = matches.computeIfAbsent(matchType, k -> new LinkedHashMap<>());
        Controller c = new Controller(matchType);
        filteredMatches.put(matchId, c);
        matchId++;
        return c;
    }

    public static Controller joinMatch(MatchType matchType) throws NotAllowedException {
        LinkedHashMap<Long, Controller> filteredMatches = matches.get(matchType);
        if (filteredMatches == null) throw new NotAllowedException("No matches found :(");
        // get oldest
        Controller c = (Controller) filteredMatches.entrySet().iterator().next();
        if (c == null) throw new NotAllowedException("No matches found :(");
        return c;
    }

    public static Controller joinMatchById(Long id) throws NotAllowedException {
        Controller c = null;
        for (LinkedHashMap<Long, Controller> matches : matches.values())
            if ((c = matches.get(id)) != null) break;
        if (c == null) throw new NotAllowedException("No matches found :(");
        return c;
    }

    public static void removeMatch(Long id) {
        for (LinkedHashMap<Long, Controller> matches : matches.values())
            if (matches.remove(id) != null) break;
    }
}
