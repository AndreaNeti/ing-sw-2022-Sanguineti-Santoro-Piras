package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.model.*;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.network.toClientMessage.*;

import java.util.ArrayList;

public class Controller {
    private final MatchConstants matchConstants;
    private final MatchType matchType;
    private final ArrayList<Player> playersList;
    private final ArrayList<GameListener> playerHandlers;
    private final ArrayList<Team> teams;
    // This array is also used to represent the order of round
    private final ArrayList<Byte> playerOrder;
    private GamePhase gamePhase;
    private Game game;

    //it's the index of playerOrder: it goes from 0 to players.size() and when it's 3 it changes phase
    private byte roundIndex;
    // counts the number of students moved during the action phase
    private byte movesCounter;
    // Byte so it's immutable
    private Byte currentPlayerIndex;
    private boolean lastRound, characterCardPlayed;

    private ArrayList<HouseColor> winners;
    private boolean gameFinished;

    private final Long matchId;

    public Controller(MatchType matchType, Long id) {
        this.matchConstants = Server.getMatchConstants(matchType);
        this.matchType = matchType;
        this.playersList = new ArrayList<>(matchType.nPlayers());
        this.playerHandlers = new ArrayList<>(matchType.nPlayers());
        this.playerOrder = new ArrayList<>(matchType.nPlayers());
        for (byte i = 0; i < matchType.nPlayers(); i++)
            playerOrder.add(i);
        byte nTeams = (byte) ((matchType.nPlayers() % 2) + 2); // size is 2 or 3
        this.teams = new ArrayList<>(nTeams);
        for (byte i = 0; i < nTeams; i++) {
            teams.add(new Team(HouseColor.values()[i], (byte) (matchType.nPlayers() / nTeams), (byte) matchConstants.towersForTeam()));
        }
        this.roundIndex = 0;
        this.currentPlayerIndex = 0;
        this.lastRound = false;
        this.movesCounter = 0;
        this.characterCardPlayed = false;
        this.winners = null;
        this.gameFinished = false;
        this.matchId = id;

    }

    public boolean isMyTurn(ClientHandler caller) {
        synchronized (playerHandlers) {
            return currentPlayerIndex == playerHandlers.indexOf(caller);
        }
    }

    public synchronized void move(Color color, int idGameComponent) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_ST_PHASE) {
            if (idGameComponent <= 0 || idGameComponent >= 2 * matchType.nPlayers() + 12)
                throw new NotAllowedException("Can't move to the selected GameComponent");

            if (idGameComponent < 2 * matchType.nPlayers() && idGameComponent != (currentPlayerIndex * 2 + 1))
                throw new NotAllowedException("Can't move to the selected GameComponent");
            //currentPlayerIndex*2 is the id of the entranceHall
            game.move(color, (currentPlayerIndex * 2), idGameComponent);
            movesCounter++;
            broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " moved a " + color + " student"));
            if (movesCounter == matchConstants.studentsToMove()) {
                movesCounter = 0;
                nextPhase();
            } else {
                setPhase(GamePhase.MOVE_ST_PHASE);
            }
        } else {
            throw new NotAllowedException("Wrong Phase");
        }

    }

    public synchronized void moveFromCloud(int idGameComponent) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_CL_PHASE) { // move students from cloud, destination is player entrance hall
            if (idGameComponent >= 0 || idGameComponent < -matchType.nPlayers())
                throw new NotAllowedException("Component is not a cloud");
            game.moveFromCloud(idGameComponent);
            broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " take student from a cloud"));
            nextPhase();
        } else
            throw new NotAllowedException("Wrong Phase");

    }

    //the value here need to go from 1 to 10
    public synchronized void playCard(byte value) throws GameException, NullPointerException, EndGameException {
        if (gamePhase != GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        ArrayList<Byte> playedCards = new ArrayList<>();
        //loop where I put in playedCard the previous card played by other Player.If it's current Player
        //it breaks the loop 'cause there aren't other previous player
        for (int i = 0; i < roundIndex; i++) {
            playedCards.add(playersList.get((playerOrder.get(0) + i) % playersList.size()).getPlayedCard());
        }

        if (playersList.get(currentPlayerIndex).canPlayCard(playedCards, value)) {
            game.playCard(value);
            broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " played card n° " + value));
            nextPhase();
        } else {
            throw new NotAllowedException("Cannot play this card");
        }
    }

    public synchronized void moveMotherNature(int moves) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_MN_PHASE) {
            game.moveMotherNature(moves);
            broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " moved MotherNature by " + moves + " moves"));
            nextPhase();
        } else {
            throw new NotAllowedException("Wrong phase");
        }
    }

    public synchronized void addPlayer(GameListener newPlayerHandler, String nickName) throws GameException {
        if (playersList.size() == matchType.nPlayers()) {
            throw new NotAllowedException("Match is full");
        }
        int teamIndex = playersList.size() % teams.size(); // circular team selection
        Player newPlayer;
        newPlayer = new Player(nickName, teams.get(teamIndex), Wizard.values()[playersList.size()], matchConstants);

        // notify other players in lobby about the new player
        notifyClients(new PlayerJoined(newPlayer, HouseColor.values()[teamIndex]));
        // notify the new player about match info and other players in lobby
        newPlayerHandler.update(new MatchInfo(matchType, matchId, teams, Wizard.values()[playersList.size()]));

        playersList.add(newPlayer);
        playerHandlers.add(newPlayerHandler);

        if (playersList.size() == matchType.nPlayers()) {
            System.out.println("game is starting");
            startGame();
            Server.removeMatch(matchId);
        } else { // sends ok only if need to wait other players, otherwise newPlayer will receive Phase message
            newPlayerHandler.update(new OK());
        }
    }

    public void sendMessage(ClientHandler me, String message) throws NullPointerException {
        if (me == null) throw new NullPointerException();
        notifyClients(new TextMessageSC("[" + me.getNickName() + "]: " + message), me);
    }

    public synchronized void setCharacterInput(int input) throws GameException, NullPointerException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        game.setCharacterInput(input);
    }

    public synchronized void chooseCharacter(byte charId) throws GameException, NullPointerException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        game.chooseCharacter(charId);
        broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " chose character card " + charId));
    }

    public synchronized void playCharacter() throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        if (characterCardPlayed) {
            throw new NotAllowedException("A card has already been played this turn");
        }
        game.playCharacter();
        characterCardPlayed = true;
        broadcastMessage(new TextMessageSC("Server: " + Wizard.values()[getCurrentPlayerIndex()] + " played character card"));
    }

    public void disconnectPlayerQuit(GameListener playerAlreadyDisconnected) {
        removePlayer(playerAlreadyDisconnected);
        if (gameFinished) return;
        gameFinished = true;
        Server.removeMatch(matchId);
        notifyClients(new EndGame(null));
    }

    private void notifyClients(ToClientMessage m, GameListener excludeMe) {
        synchronized (playerHandlers) {
            for (GameListener gl : playerHandlers) {
                if (!gl.equals(excludeMe))
                    gl.update(m);
            }
        }
    }

    private void notifyClients(ToClientMessage m) {
        notifyClients(m, null);
    }

    public void broadcastMessage(TextMessageSC m) {
        notifyClients(m);
    }

    public void removePlayer(GameListener toRemovePlayer) throws NullPointerException {
        synchronized (playerHandlers) {
            if (toRemovePlayer == null) throw new NullPointerException();
            else if (!playerHandlers.contains(toRemovePlayer))
                System.err.println("PlayerHandler not present on controller");
            playerHandlers.remove(toRemovePlayer);
        }
    }

    private void startGame() {
        if (matchType.isExpert())
            game = new ExpertGame(teams, matchConstants);
        else
            game = new NormalGame(teams, matchConstants);
        game.setCurrentPlayer(currentPlayerIndex);
        // TODO find a better way
        GameDelta gameDelta = game.getGameDelta();
        for (GameListener listener : playerHandlers)
            gameDelta.addListener(listener);

        game.transformAllGameInDelta().send();
        setPhase(GamePhase.PLANIFICATION_PHASE);
    }

    private void nextPlayer() {
        characterCardPlayed = false;
        roundIndex++;
        if (roundIndex >= playersList.size()) {
            roundIndex = 0;
            if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
                //it's a new turn because it passed from planification to actionPhase
                //changed the order of player, need to sort based on the playedCard
                playerOrder.sort((b1, b2) -> {
                    int ret = playersList.get(b1).getPlayedCard() - playersList.get(b2).getPlayedCard();
                    // players have used the same card, compare in function of who played first
                    // [3,1,2,4] : player 3 is the first playing in planification phase then clockwise order (3,4,1,2), doing bx -= 3 (mod 4) obtains -> [0,2,3,1] and that's the planification phase order from 0 to 3
                    if (ret == 0)
                        ret = Math.floorMod(b1 - playerOrder.get(0), matchType.nPlayers()) - Math.floorMod(b2 - playerOrder.get(0), matchType.nPlayers());
                    return ret;
                });
            }
        }
        currentPlayerIndex = getCurrentPlayerIndex();
        game.setCurrentPlayer(currentPlayerIndex);
    }

    private byte getCurrentPlayerIndex() {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            // is planification phase, clockwise order
            return (byte) ((playerOrder.get(0) + roundIndex) % playersList.size());
        } else {
            // action phase, follow the playerOrder
            return playerOrder.get(roundIndex);
        }
    }

    private void nextPhase() throws EndGameException {
        //sort the array if the nextPhase is the action phase
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            nextPlayer();
            if (roundIndex == 0) {
                setPhase(GamePhase.MOVE_ST_PHASE);
            } else {
                setPhase(GamePhase.PLANIFICATION_PHASE);
            }
        } else {

            //check if it's the last action phase and if it's the last player playing in that turn
            if (gamePhase == GamePhase.MOVE_CL_PHASE) {
                //set the next player, if it's the last player of the round, round index will be 0
                nextPlayer();

                if (roundIndex != 0) {
                    setPhase(GamePhase.MOVE_ST_PHASE);
                } else {
                    if (lastRound) {
                        // should go in new preparation phase but is last round
                        endGame();
                    } else {
                        //there is a new turn completely
                        setPhase(GamePhase.PLANIFICATION_PHASE);
                        game.refillClouds();
                    }
                }

            } else {
                //set the phase to the next action phase
                setPhase(GamePhase.values()[gamePhase.ordinal() + 1]);
            }
        }
    }

    private void setPhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notifyClients(new Phase(gamePhase, currentPlayerIndex));
    }

    private void endGame() {
        winners = game.calculateWinner();
        gameFinished = true;
        notifyClients(new EndGame(winners));
        if (winners.size() == 3) System.out.println("Paolino tvb <3");
    }


    protected void handleError(EndGameException e) {
        if (e.isEndInstantly()) endGame();
        else {
            lastRound = true;
            broadcastMessage(new TextMessageSC("Server: This is the last round"));
        }
    }

    protected ArrayList<HouseColor> getWinners() {
        return winners;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    protected Long getMatchId() {
        return matchId;
    }
}
