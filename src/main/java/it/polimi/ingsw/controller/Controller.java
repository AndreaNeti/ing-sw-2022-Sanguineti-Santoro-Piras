package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.model.*;

import java.net.Socket;
import java.util.*;

public class Controller {
    private final boolean isExpertGame;
    private final int matchId;
    private final List<Player> playersList;
    private final byte numberOfPlayers;
    private final Map<HouseColor, Queue<String>> messages;
    private final List<Team> teams;
    private Game game;

    public Controller(int matchId, boolean isExpertGame, byte numberOfPlayers) {
        this.matchId = matchId;
        this.isExpertGame = isExpertGame;
        this.numberOfPlayers = numberOfPlayers;
        this.playersList = new ArrayList<>(numberOfPlayers);
        byte nTeams = (byte) ((numberOfPlayers % 2) + 2); // size is 2 or 3
        byte maxTowers = (byte) (12 - nTeams * 2); // 2 teams -> 8 towers, 3 teams -> 6 towers
        this.teams = new ArrayList<>(nTeams);
        for (byte i = 0; i < nTeams; i++) {
            teams.add(new Team(HouseColor.values()[i], (byte) (numberOfPlayers / nTeams), maxTowers));
        }
        this.messages = new HashMap<>();
    }

    public void move(Color color, int idGameComponent) {

    }

    public void playCard(byte value) {

    }

    public void moveMotherNature(int i) {

    }

    public boolean isExpertGame() {
        return isExpertGame;
    }

    public int getMatchId() {
        return matchId;
    }

    public void addPlayer(Socket s, String nickName) {
        int teamIndex = playersList.size() % teams.size(); // circular team selection
        Player newPlayer = new Player(s, teams.get(teamIndex), Wizard.values()[playersList.size()], nickName);
        try {
            teams.get(teamIndex).addPlayer(newPlayer);
        } catch (NotAllowedException e) {
            e.printStackTrace();
            System.err.println(e.getErrorMessage());
        }
        playersList.add(newPlayer);
        if (playersList.size() == numberOfPlayers) {
            startGame();
        }
    }

    public void changeTeam() {

    }

    public void leaveLobby() {

    }

    public void sendMessage(String message) {

    }

    public void setCharacterInput(int input) {

    }

    public void chooseCharacter(int character) {

    }

    public void playCharacter() {

    }

    private void startGame() {

    }
}
