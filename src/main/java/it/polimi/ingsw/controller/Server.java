package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;

import it.polimi.ingsw.exceptions.UnexpectedValueException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server {

    public static Long matchId = 0L;

    public static final HashMap<MatchType, LinkedHashMap<Long, Controller>> matches = new HashMap<>();
    private static final Set<String> nickname = new HashSet<>();

    public static void main(String[] args) {
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

    public static boolean setNickname(String nicknameToAdd) throws UnexpectedValueException {
        synchronized (nickname) {
            if (!nickname.add(nicknameToAdd))
                throw new UnexpectedValueException();
            return true;
        }
    }


    public static Controller createMatch(MatchType matchType) {
        synchronized (matches) {
            LinkedHashMap<Long, Controller> filteredMatches = matches.computeIfAbsent(matchType, k -> new LinkedHashMap<>());
            Controller c = new Controller(matchType);
            filteredMatches.put(matchId, c);
            matchId++;
            return c;
        }
    }

    public static Long getMatch(MatchType matchType) throws NotAllowedException {
        synchronized (matches) {
            LinkedHashMap<Long, Controller> filteredMatches = matches.get(matchType);
            if (filteredMatches == null) throw new NotAllowedException("No matches found :(");
            // get oldest
            Long c = filteredMatches.entrySet().iterator().next().getKey();
            if (c == null) throw new NotAllowedException("No matches found :(");
            return c;
        }
    }

    public static Controller getMatchById(Long id) throws NotAllowedException {
        synchronized (matches) {
            Controller c = null;
            for (LinkedHashMap<Long, Controller> matches : matches.values())
                if ((c = matches.get(id)) != null) break;
            if (c == null) throw new NotAllowedException("No matches found :(");
            return c;
        }
    }

    public static void removeMatch(Long id) {
        synchronized (matches) {
            for (LinkedHashMap<Long, Controller> matches : matches.values())
                if (matches.remove(id) != null) break;
        }
    }

    public static Controller getControllerById(Long idController) throws NotAllowedException {
        Controller c = null;
        for (LinkedHashMap<Long, Controller> matches : matches.values()) {
            if (matches.containsKey(idController)) {
                c = matches.get(idController);
            }
        }
        if (c == null) throw new NotAllowedException("Error in retrieving the id");

        return c;
    }


}
