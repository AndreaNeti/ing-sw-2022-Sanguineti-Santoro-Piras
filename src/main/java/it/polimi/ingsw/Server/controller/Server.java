package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Map.entry;

public class Server {
    private static final int serverPort = 4026;
    public static Long matchId = 0L;
    public static final ConcurrentHashMap<MatchType, Map<Long, Controller>> matches = new ConcurrentHashMap<>();
    private static final Set<String> nickNames = Collections.synchronizedSet(new HashSet<>());
    private static final Map<MatchType, MatchConstants> matchConstants = Map.ofEntries(
            entry(new MatchType((byte) 2, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3)),
            entry(new MatchType((byte) 2, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3)),
            entry(new MatchType((byte) 3, false), new MatchConstants(10, 3, 0, 0, 0, 9, 10, 6, 4)),
            entry(new MatchType((byte) 3, true), new MatchConstants(10, 3, 3, 20, 1, 9, 10, 6, 4)),
            entry(new MatchType((byte) 4, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3)),
            entry(new MatchType((byte) 4, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3))
    );

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(serverPort)) {

            while (true) {
                System.out.println("Server ready to receive on " + serverPort);
                try {
                    Thread t = new Thread(new ClientHandler(server.accept()));
                    t.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setNickName(String nickNameToAdd) throws NotAllowedException {
        if (!nickNames.add(nickNameToAdd))
            throw new NotAllowedException("Nickname already taken");
    }

    public static void removeNickName(String nickNameToRemove) {
        nickNames.remove(nickNameToRemove);
    }

    public static Controller createMatch(MatchType matchType) {
        Map<Long, Controller> filteredMatches = matches.computeIfAbsent(matchType, k -> Collections.synchronizedMap(new LinkedHashMap<>()));
        Controller c = new Controller(matchType, matchId);
        filteredMatches.put(matchId, c);
        matchId++;
        return c;
    }

    public static Long getOldestMatchId(MatchType matchType) throws NotAllowedException {
        Map<Long, Controller> filteredMatches = matches.get(matchType);
        if (filteredMatches == null) throw new NotAllowedException("No matches found :(");
        // get oldest
        Long c;
        try {
            c = filteredMatches.entrySet().iterator().next().getKey();
        } catch (NoSuchElementException ex) {
            throw new NotAllowedException("No matches found :(");
        }
        if (c == null) throw new NotAllowedException("No matches found :(");
        return c;
    }

    public static Controller getMatchById(Long id) throws NotAllowedException {
        Controller c = null;
        for (Map<Long, Controller> matches : matches.values())
            if ((c = matches.get(id)) != null) break;
        if (c == null) throw new NotAllowedException("No matches found :(");
        return c;
    }

    public static void removeMatch(Long id) {
        for (Map<Long, Controller> matches : matches.values())
            if (matches.remove(id) != null) break;
    }

    public static MatchConstants getMatchConstants(MatchType matchType) {
        return matchConstants.get(matchType);
    }
}
