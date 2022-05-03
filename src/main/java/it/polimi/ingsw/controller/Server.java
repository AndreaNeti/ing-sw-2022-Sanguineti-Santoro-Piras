package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

import static java.util.Map.entry;

public class Server {

    public static Long matchId = 0L;

    public static final HashMap<MatchType, LinkedHashMap<Long, Controller>> matches = new HashMap<>();

    // TODO add number if duplicate
    private static final Set<String> nickNames = new HashSet<>();
    private static final Map<MatchType, MatchConstants> matchConstants = Map.ofEntries(
            entry(new MatchType((byte) 2, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3)),
            entry(new MatchType((byte) 2, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3)),
            entry(new MatchType((byte) 3, false), new MatchConstants(10, 3, 0, 0, 0, 9, 10, 6, 4)),
            entry(new MatchType((byte) 3, true), new MatchConstants(10, 3, 3, 20, 1, 9, 10, 6, 4)),
            entry(new MatchType((byte) 4, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3)),
            entry(new MatchType((byte) 4, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3))
    );

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(4206)) {
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

    public static void setNickName(String nickNameToAdd) throws NotAllowedException {
        synchronized (nickNames) {
            if (!nickNames.add(nickNameToAdd))
                throw new NotAllowedException("Nickname already taken");
        }
    }

    public static void removeNickName(String nickNameToRemove) {
        synchronized (nickNames) {
            nickNames.remove(nickNameToRemove);
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

    public static MatchConstants getMatchConstants(MatchType matchType) {
        synchronized (matchConstants) {
            return matchConstants.get(matchType);
        }
    }
}
