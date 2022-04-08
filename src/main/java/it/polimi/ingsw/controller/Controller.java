package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.model.*;

import java.net.Socket;
import java.util.*;

public class Controller {
    private final boolean isExpertGame;
    private final int matchId;
    private final ArrayList<Player> playersList;
    private final byte numberOfPlayers;
    private final Map<HouseColor, Queue<String>> messages;
    private final ArrayList<Team> teams;
    // This array is also used to represent the order of round
    private final ArrayList<Byte> playerOrder;
    private Game game;
    private Player currentPlayer;
    // current phase is true in the planification phase, false during the action phase
    private boolean isPlanificationPhase;
    /* it's a number that goes from 1 to 3, it represents the subsection of the action phase
    1-move 3-4 students; 2-move mother nature(calculate influence and merge); 3-drawStudent from cloud*/
    private byte actionPhase;
    //it's the index of playerOrder: it goes from 0 to players.size() and when it's 3 it changes phase
    private byte roundIndex;
    private boolean lastRound;

    public Controller(int matchId, boolean isExpertGame, byte numberOfPlayers) {
        this.matchId = matchId;
        this.isExpertGame = isExpertGame;
        this.numberOfPlayers = numberOfPlayers;
        this.playersList = new ArrayList<>(numberOfPlayers);
        this.playerOrder = new ArrayList<>(numberOfPlayers);
        for (byte i = 0; i < numberOfPlayers; i++)
            playerOrder.add(i);
        byte nTeams = (byte) ((numberOfPlayers % 2) + 2); // size is 2 or 3
        byte maxTowers = (byte) (12 - nTeams * 2); // 2 teams -> 8 towers, 3 teams -> 6 towers
        this.teams = new ArrayList<>(nTeams);
        for (byte i = 0; i < nTeams; i++) {
            teams.add(new Team(HouseColor.values()[i], (byte) (numberOfPlayers / nTeams), maxTowers));
        }
        this.messages = new HashMap<>();
        this.isPlanificationPhase = false;
        this.actionPhase = 0;
        this.roundIndex = 0;
        this.lastRound = false;
    }

    public void move(Color color, int idGameComponent) {
        if (isPlanificationPhase) {
            handleError(new NotAllowedException("Not in action phase"));
            return;
        }
        try {
            if (actionPhase == 1) {
                if (idGameComponent <= 0)
                    throw new NotAllowedException("Can't move to the selected GameComponent");
                game.move(color, 0, idGameComponent);
            } else if (actionPhase == 3) { // move students from cloud, destination is player entrance hall
                game.moveFromCloud(idGameComponent);
            } else {
                throw new NotAllowedException("Wrong Phase");
            }
        } catch (GameException e) {
            handleError(e);
        }
        nextActionPhase();
    }

    public void playCard(byte value) {
        if (!isPlanificationPhase) {
            handleError(new NotAllowedException("Not in planification phase"));
            return;
        }
        try {
            game.playCard(value);
        } catch (GameException e) {
            handleError(e);
        } catch (EndGameException e) {
            if (e.isEndInstantly()) endGame();
            else lastRound = true;
        }
        nextPlayer();
    }

    public void moveMotherNature(int i) {
        if (isPlanificationPhase || actionPhase != 2) {
            handleError(new NotAllowedException("Not allowed in this phase"));
            return;
        }
        try {
            game.moveMotherNature(i);
        } catch (NotAllowedException e) {
            handleError(e);
        } catch (EndGameException e) {
            if (e.isEndInstantly()) endGame();
            else lastRound = true;
        }
        nextActionPhase();
    }


    public int getMatchId() {
        return matchId;
    }

    public void addPlayer(Socket s, String nickName) {
        int teamIndex = playersList.size() % teams.size(); // circular team selection
        int entranceHallSize = (teams.size() % 2 == 0) ? 7 : 9;
        Player newPlayer = new Player(s, teams.get(teamIndex), Wizard.values()[playersList.size()], nickName, entranceHallSize);
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
        if (isPlanificationPhase) {
            handleError(new NotAllowedException("Not in action phase"));
            return;
        }
        try {
            game.setCharacterInput(input);
        } catch (GameException e) {
            handleError(e);
        }
    }

    public void chooseCharacter(int character) {
        if (isPlanificationPhase) {
            handleError(new NotAllowedException("Not in action phase"));
            return;
        }
        try {
            game.chooseCharacter(character);
        } catch (GameException e) {
            handleError(e);
        }
    }

    public void playCharacter() {
        if (isPlanificationPhase) {
            handleError(new NotAllowedException("Not in action phase"));
            return;
        }
        try {
            game.playCharacter();
        } catch (GameException e) {
            handleError(e);
        } catch (EndGameException e) {
            if (e.isEndInstantly()) endGame();
            else lastRound = true;
        }
    }

    private void startGame() {
        if (isExpertGame)
            game = new ExpertGame(numberOfPlayers, teams, playersList);
        else
            game = new NormalGame(numberOfPlayers, teams, playersList);
        Random rand = new Random(System.currentTimeMillis());
        currentPlayer = playersList.get(rand.nextInt(numberOfPlayers));
        game.setCurrentPlayer(currentPlayer);
    }

    private void nextPlayer() {
        int index;
        roundIndex++;
        if (roundIndex >= playersList.size())
            nextPhase();
        if (isPlanificationPhase) {
            // is planification phase, clockwise order
            index = (playerOrder.get(0) + roundIndex) % playersList.size();
        } else {
            // action phase, follow the playerOrder
            index = playerOrder.get(roundIndex);
            actionPhase = 1;
        }

        Random rand = new Random(System.currentTimeMillis());
        currentPlayer = playersList.get(index);
        game.setCurrentPlayer(currentPlayer);
    }

    private void nextPhase() {
        this.isPlanificationPhase = !isPlanificationPhase;
        //sort the array if the nextPhase is the action phase
        if (!isPlanificationPhase) {
            playerOrder.sort(Comparator.comparingInt((Byte b) -> playersList.get(b).getPlayedCard()));
        } else {
            if (lastRound) {
                // should go in new preparation phase but is last round
                endGame();
            } else { // new planification phase
                try {
                    game.refillClouds();
                } catch (EndGameException e) {
                    if (e.isEndInstantly()) endGame();
                    else lastRound = true;
                }
            }
        }
        roundIndex = 0;
    }

    private void nextActionPhase() {
        // pass to new player
        if (actionPhase >= 3) nextPlayer();
        else actionPhase++;
    }

    private void endGame() {
        ArrayList<Team> winners = game.calculateWinner();
        StringBuilder message = new StringBuilder("Game ended: ");
        byte i;
        for (i = 0; i < winners.size() - 1; i++) {
            message.append(winners.get(i).toString()).append(", ");
        }
        message.append(winners.get(i).toString()).append(" won the game!!!");
        System.out.println(message);
        if(winners.size() == 3)
            System.out.println("Paolino tvb <3");
    }

    private void handleError(GameException e) {
        e.printStackTrace();
        System.err.println(e.getErrorMessage());
    }
}
