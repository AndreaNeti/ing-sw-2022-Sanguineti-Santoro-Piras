package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.MatchConstants;
import it.polimi.ingsw.Util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Map.entry;

/**
 * Server class is the main server application that accepts new connections with clients and creates new matches, to which
 * it assigns a new ID and a game controller. <br>
 * It runs on port 42069.
 */
public class Server {
    public static final int serverPort = 42069;
    public static Long matchId = 0L;
    public static final ConcurrentHashMap<MatchType, Map<Long, Controller>> matches = new ConcurrentHashMap<>();
    private static final Set<String> nickNames = Collections.synchronizedSet(new HashSet<>());
    private static final Map<MatchType, MatchConstants> matchConstants = Map.ofEntries(
            entry(new MatchType((byte) 2, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3,0)),
            entry(new MatchType((byte) 2, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3,2)),
            entry(new MatchType((byte) 3, false), new MatchConstants(10, 3, 0, 0, 0, 9, 10, 6, 4,2)),
            entry(new MatchType((byte) 3, true), new MatchConstants(10, 3, 3, 20, 1, 9, 10, 6, 4,2)),
            entry(new MatchType((byte) 4, false), new MatchConstants(10, 3, 0, 0, 0, 7, 10, 8, 3,2)),
            entry(new MatchType((byte) 4, true), new MatchConstants(10, 3, 3, 20, 1, 7, 10, 8, 3,2))
    );

    /**
     * Method main initializes the server and accepts new connections, assigning each ClientHandler to a new thread.
     *
     * @param args of type {@code String[]} - application arguments.
     */
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(serverPort)) {
            String serverIp = InetAddress.getLocalHost().getHostAddress();
            while (true) {
                System.out.println("Server ready to receive on " + serverIp + ":" + serverPort);
                try {
                    new Thread(new ClientHandler(server.accept())).start();
                } catch (IOException e) {
                    server.close();
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Closing...");
        }
    }

    /**
     * Method setNickName adds a nickname to the Server's list of nicknames
     *
     * @param nickNameToAdd of type {@code String} - nickname to add.
     * @throws NotAllowedException if the nickname has already been taken by another player.
     */
    public static void setNickName(String nickNameToAdd) throws NotAllowedException {
        if (!nickNames.add(nickNameToAdd))
            throw new NotAllowedException("Nickname already taken");
    }

    /**
     * Method removeNickname removes a nickname from the Server's list of nicknames
     *
     * @param nickNameToRemove of type {@code String} - nickname to remove.
     */
    public static void removeNickName(String nickNameToRemove) {
        nickNames.remove(nickNameToRemove);
    }

    /**
     * Method createMatch creates a match of the specified type and assigns a new controller to it. It then adds
     * the match and the controller to a Server's map of all available matches.
     *
     * @param matchType of type {@link MatchType} - type of the match to create.
     * @return {@link Controller} - controller assigned to the created match.
     */
    public static Controller createMatch(MatchType matchType) {
        Map<Long, Controller> filteredMatches = matches.computeIfAbsent(matchType, k -> Collections.synchronizedMap(new LinkedHashMap<>()));
        Controller c = new Controller(matchType, matchId);
        filteredMatches.put(matchId, c);
        matchId++;
        return c;
    }

    /**
     * Method fetOldestMatchId returns the oldest active match with the specified match types.
     *
     * @param matchType of type {@link MatchType} - type of the match of which we want to get the ID.
     * @return {@code Long} - ID of the oldest match with the specified type.
     * @throws NotAllowedException if no match with the specified type are found.
     * if no match with the specified type are found.
     */
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

    /**
     * Method getMatchById returns the controller of the match with the specified ID.
     *
     * @param id of type {@code Long} - ID of the match we want to get the controller of.
     * @return {@link Controller} - instance of the Controller associated with the match with the specified ID.
     * @throws NotAllowedException if the match with the specified ID is not found.
     */
    public static Controller getMatchById(Long id) throws NotAllowedException {
        Controller c = null;
        for (Map<Long, Controller> matches : matches.values())
            if ((c = matches.get(id)) != null) break;
        if (c == null) throw new NotAllowedException("No matches found :(");
        return c;
    }

    /**
     * Method removeMatch removes the match with the specified ID from the Server's map of all available matches.
     * @param id of type {@code Long} - ID of the match to remove.
     */
    public static void removeMatch(Long id) {
        for (Map<Long, Controller> matches : matches.values())
            if (matches.remove(id) != null) break;
    }

    /**
     * Method getMatchConstants returns the match constants of a specified match type.
     *
     * @param matchType of type {@link MatchType} - match type of which we want to get the constants.
     * @return {@link MatchConstants} - match constants of the specified match type.
     */
    public static MatchConstants getMatchConstants(MatchType matchType) {
        return matchConstants.get(matchType);
    }
}
