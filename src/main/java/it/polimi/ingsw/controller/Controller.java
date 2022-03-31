package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.net.Socket;
import java.util.*;

public class Controller {
    private final boolean isExpertGame;
    private Game game;
    private final int matchId;
    private final List<Player> playersList;
    private final byte numberOfPlayers;
    private final Map<HouseColor, Queue<String>> messages;
    private final List<Team> teams;

    public Controller(int matchId, boolean isExpertGame, byte numberOfPlayers) {
        this.matchId = matchId;
        this.isExpertGame = isExpertGame;
        this.numberOfPlayers = numberOfPlayers;
        this.playersList = new ArrayList<>(numberOfPlayers);
        this.teams = new ArrayList<>();
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
        return this.matchId;
    }

    public void addPlayer(Socket s) {

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
