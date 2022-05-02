package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Server {

    public static Long matchId = 0L;

    public static final HashMap<MatchType, LinkedHashMap<Long, Controller>> matches = new HashMap<>();

    // TODO add number if duplicate
    private static final Set<String> nickNames = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(7896)) {
            while (true) {
                try {
                    new Thread(new PlayerHandler(server.accept())).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setNickNames(String nicknameToAdd) throws UnexpectedValueException {
        synchronized (nickNames) {
            if (!nickNames.add(nicknameToAdd))
                throw new UnexpectedValueException();
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

    public static Long getOldestMatchId(MatchType matchType) throws NotAllowedException {
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


}
