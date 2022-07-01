package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.network.GameDelta;
import it.polimi.ingsw.network.toClientMessage.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class represents the controller in the "MVC" pattern. <br>
 * All clients have to call the Controller's method to interact with the game's model. <br>
 * This class contains the players order and game phase logic, so it notifies the clients if a method is
 * called in the right or wrong phase. <br>
 * It also contains a list of all the players and their respective client handlers, a list of the teams
 * and an instance of the game it controls.
 */
public class Controller {
    private final MatchConstants matchConstants;
    private final MatchType matchType;
    private final ArrayList<Player> playersList;
    private final ArrayList<GameListener> clientHandlers;
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
    private boolean lastRound, characterCardPlayed, skipCloudPhase;

    private ArrayList<HouseColor> winners;
    private final ArrayList<AssistantCard> playedCards;
    private boolean gameFinished;
    private final Long matchId;

    /**
     * Constructor Controller creates a new instance of Controller.
     *
     * @param matchType of type {@link MatchType} - match type of the controller.
     * @param id        of type {@code Long} - unique ID of the controller.
     */
    public Controller(MatchType matchType, Long id) {
        this.matchConstants = Server.getMatchConstants(matchType);
        this.matchType = matchType;
        this.playersList = new ArrayList<>(matchType.nPlayers());
        this.clientHandlers = new ArrayList<>(matchType.nPlayers());
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
        this.skipCloudPhase = false;
        playedCards = new ArrayList<>(matchType.nPlayers());
    }

    /**
     * Method isMyTurn checks if it is currently the turn of the caller ClientHandler to play.
     *
     * @param caller of type {@link ClientHandler} - handler that wants to know if it's its turn.
     * @return {@code boolean} - true if it is the turn of the caller, false else.
     */
    public boolean isMyTurn(ClientHandler caller) {
        synchronized (clientHandlers) {
            return currentPlayerIndex == clientHandlers.indexOf(caller);
        }
    }

    /**
     * Method move is used to move a student of a specified color to a selected game component. <br>
     * The source game component is obtained from the current game phase.
     *
     * @param color           of type {@code Color} - the color of the student to move.
     * @param idGameComponent of type {@code Int} - the ID of the source component.
     * @throws GameException if the color is null or the game component's ID is not valid or if it's not possible to move the student
     *                       to the specified game component or if this method is called during a phase that is not move_st_phase.
     */
    public synchronized void move(Color color, int idGameComponent) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_ST_PHASE) {
            // not your lunchHall neither an island
            if (idGameComponent != (currentPlayerIndex * 2 + 1) && (idGameComponent < 2 * MatchType.MAX_PLAYERS || idGameComponent >= 2 * MatchType.MAX_PLAYERS + 12))
                throw new NotAllowedException("Can't move to the selected GameComponent");
            game.move(color, (currentPlayerIndex * 2), idGameComponent);
            // do not increment counter if exception thrown
            movesCounter++;
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

    /**
     * Method moveFromCloud is used to move students from a cloud to the current player's entrance hall.
     *
     * @param idGameComponent of type {@code byte} - ID of the cloud.
     * @throws GameException    if the selected game component is not a cloud or if this method is called
     *                          during a phase that is not move_cl_phase or if the selected cloud has no students.
     * @throws EndGameException if it is the last turn and after moving students from the cloud the game ends.
     */
    public synchronized void moveFromCloud(int idGameComponent) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_CL_PHASE) { // move students from cloud, destination is player entrance hall
            if (idGameComponent >= 0 || idGameComponent < -matchType.nPlayers())
                throw new NotAllowedException("Component is not a cloud");
            game.moveFromCloud(idGameComponent);
            nextPhase();
        } else throw new NotAllowedException("Wrong Phase");

    }

    /**
     * Method playCard is used by the user to play an assistant card during the planification phase.
     *
     * @param card of type {@link AssistantCard} - instance of the assistant card to play.
     * @throws GameException if the card selected cannot be played or if this method is called during a phase
     *                       that is not planification_phase or if the selected assistant card is not available to play.
     */
    //the value here need to go from 1 to 10
    public synchronized void playCard(AssistantCard card) throws GameException, NullPointerException, EndGameException {
        if (gamePhase != GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }

        if (playersList.get(currentPlayerIndex).canPlayCard(playedCards, card)) {
            try {
                game.playCard(card);
                playedCards.add(card);
            } catch (EndGameException e) {
                handleError(e);
            }
            nextPhase();
        } else {
            throw new NotAllowedException("Cannot play this card");
        }
    }

    /**
     * Method moveMotherNature is used to move mother nature a selected amount of moves.
     *
     * @param moves of type {@code byte} - number of moves wanted.
     * @throws GameException    if this method is called during a phase that is not move_mn_phase or if mother nature cannot
     *                          be moved the selected amount of moves.
     * @throws EndGameException if after moving mother nature there are less than 3 islands left in the game.
     */
    public synchronized void moveMotherNature(int moves) throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.MOVE_MN_PHASE) {
            try {
                game.moveMotherNature(moves);
            } catch (EndGameException e) {
                handleError(e);
            }
            nextPhase();
        } else {
            throw new NotAllowedException("Wrong phase");
        }
    }

    /**
     * Method addPlayer is used to add a player and its respective handler to the game. <br>
     * After adding the player, all players already in match are notified and the player receives
     * the game's info.
     *
     * @param newPlayerHandler of type {@link GameListener} - instance of the player's ClientHandler.
     * @param nickName         of type {@code String} - nickname of the player to add.
     * @throws GameException if the match is already full.
     */
    public synchronized void addPlayer(GameListener newPlayerHandler, String nickName) throws GameException {
        if (playersList.size() == matchType.nPlayers()) {
            throw new NotAllowedException("Match is full");
        }
        int teamIndex = playersList.size() % teams.size(); // circular team selection
        Player newPlayer;
        newPlayer = new Player(nickName, teams.get(teamIndex), Wizard.values()[playersList.size()], matchConstants);

        // notifyGameComponent other players in lobby about the new player
        notifyClients(new PlayerJoined(newPlayer, HouseColor.values()[teamIndex]));
        // notifyGameComponent the new player about match info and other players in lobby
        newPlayerHandler.update(new MatchInfo(matchType, matchConstants, matchId, teams, Wizard.values()[playersList.size()]));

        playersList.add(newPlayer);
        clientHandlers.add(newPlayerHandler);

        if (playersList.size() == matchType.nPlayers()) {
            System.out.println("Game is starting...");
            startGame();
            Server.removeMatch(matchId);
        }
    }

    /**
     * Method sendMessage is used by the player to send a message to the other players in the game.
     *
     * @param me      of type {@link ClientHandler} - instance of the client handler that will send the message.
     * @param message of type {@code String} - text of the message.
     * @throws NullPointerException if the client handler instance is null.
     */
    public void sendMessage(ClientHandler me, String message) throws NullPointerException {
        if (me == null) throw new NullPointerException();
        notifyClients(new TextMessageSC("[" + me.getNickName() + "]: " + message), me);
    }

    /**
     * Method setCharacterInputs is used to add inputs to play the character card.
     *
     * @param inputs of type {@code List}<{@code Integer}> - list of inputs to add.
     * @throws GameException if this method is called during the planification_phase or if there is not selected
     *                       character card.
     */
    public synchronized void setCharacterInputs(List<Integer> inputs) throws GameException, NullPointerException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        game.setCharacterInputs(inputs);
    }

    /**
     * Method chooseCharacter is used to choose a character card to play.
     *
     * @param charId of type {@code Byte} - ID of the character card. If charId is null then the current chosen card is set to null
     * @throws GameException if this method is called during the planification phase or
     *                       if the current player has already played a card in this turn or if the
     *                       selected character card is not available or costs more than the number of coins of the player.
     */
    public synchronized void chooseCharacter(Byte charId) throws GameException, NullPointerException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        if (characterCardPlayed) {
            throw new NotAllowedException("You already played a card in this turn");
        }
        game.chooseCharacter(charId);
        //if a card is deselected repeat the phase (which is one of the action)
        if (charId != null)
            notifyClients(new Phase(GamePhase.PLAY_CH_CARD_PHASE, currentPlayerIndex));
        else
            repeatPhase();

    }

    /**
     * Method playCharacter is used to play the chosen character card with the inputs added by the player.
     *
     * @throws GameException    if this method is called during the planification phase
     *                          if the current player has already played a card in this turn or if there's an error
     *                          while playing the card (invalid or wrong inputs).
     * @throws EndGameException if the card's effect triggers an endgame event (no more students in the bag,
     *                          * no more towers in a team's board or less than 3 islands left)
     */
    public synchronized void playCharacter() throws GameException, NullPointerException, EndGameException {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            throw new NotAllowedException("Not in action phase");
        }
        if (characterCardPlayed) {
            throw new NotAllowedException("You already played a card in this turn");
        }
        try {
            game.playCharacter();
        } catch (EndGameException e) {
            handleError(e);
        }
        characterCardPlayed = true;
        repeatPhase();
    }

    /**
     * Method disconnectPlayerQuit is used when a player disconnects from the game. All other players in the game
     * are notified and the game ends instantly with no winner.
     *
     * @param playerAlreadyDisconnected of type {@link GameListener} - instance of the ClientHandler of the disconnected player.
     */
    public void disconnectPlayerQuit(GameListener playerAlreadyDisconnected) {
        removePlayer(playerAlreadyDisconnected);
        if (gameFinished) return;
        gameFinished = true;
        Server.removeMatch(matchId);
        notifyClients(new EndGame(null));
    }

    /**
     * Method notifyClients is used to send a message to all the players in the game, except a single GameListener
     * (usually the one who sent the message or currently playing their turn).
     *
     * @param message   of type {@link ToClientMessage} - instance of the message to send to the clients.
     * @param excludeMe of type {@link GameListener} - instance of the ClientHandler that should not receive the message.
     */
    private void notifyClients(ToClientMessage message, GameListener excludeMe) {
        for (GameListener gl : clientHandlers) {
            if (!gl.equals(excludeMe)) gl.update(message);
        }
    }

    /**
     * Method notifyClients is used to send a message to all players in the game.
     *
     * @param message of type {@link ToClientMessage} - instance of the message to send to the clients.
     */
    private void notifyClients(ToClientMessage message) {
        notifyClients(message, null);
    }

    /**
     * Method broadcastMessage is used by the server to send a broadcast message to all players in game.
     *
     * @param message of type {@link TextMessageSC} - instance of the message to broadcast.
     */
    public void broadcastMessage(TextMessageSC message) {
        notifyClients(message);
    }

    /**
     * Method removePLayer is used to remove a player from the game.
     *
     * @param toRemovePlayer of type {@link GameListener} - instance of the ClientHandler of the player to remove.
     * @throws NullPointerException if the player to remove is null.
     */
    public void removePlayer(GameListener toRemovePlayer) throws NullPointerException {
        synchronized (clientHandlers) {
            if (toRemovePlayer == null) throw new NullPointerException();
            else if (!clientHandlers.contains(toRemovePlayer))
                System.err.println("PlayerHandler not present on controller");
            clientHandlers.remove(toRemovePlayer);
        }
    }

    /**
     * Method startGame is used to start a game (normal or expert). <br>
     * It sets the current player and sets the phase to planification_phase and then adds the GameListeners
     * to the GameDelta and finally sends the initial game's info to all clients.
     */
    private void startGame() {
        if (matchType.isExpert()) game = new ExpertGame(teams, matchConstants);
        else game = new NormalGame(teams, matchConstants);
        game.setCurrentPlayer(currentPlayerIndex);
        GameDelta gameDelta = game.getGameDelta();
        for (GameListener listener : clientHandlers)
            gameDelta.addListener(listener);

        game.transformAllGameInDelta().send();
        setPhase(GamePhase.PLANIFICATION_PHASE);
    }

    /**
     * Method nextPlayer updates the current player of the game with the next one. If it was the last player playing their turn,
     * the next player will be the first in the playerOrder array. <br>
     * If the phase was planification phase, a new playerOrder is obtained based on the lowest assistant card played and who played
     * it first in case two players have used the same value.
     */
    private void nextPlayer() {
        characterCardPlayed = false;
        roundIndex++;
        if (roundIndex >= playersList.size()) {
            roundIndex = 0;
            if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
                //it's a new turn because it passed from planification to actionPhase
                //changed the order of player, need to sort based on the playedCard
                playerOrder.sort((b1, b2) -> {
                    int ret = playersList.get(b1).getPlayedCard().value() - playersList.get(b2).getPlayedCard().value();
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

    /**
     * Method getCurrentPlayerIndex returns the index of the current player based on the current round index.
     *
     * @return {@code byte} - index of the current player.
     */
    private byte getCurrentPlayerIndex() {
        if (gamePhase == GamePhase.PLANIFICATION_PHASE) {
            // is planification phase, clockwise order
            return (byte) ((playerOrder.get(0) + roundIndex) % playersList.size());
        } else {
            // action phase, follow the playerOrder
            return playerOrder.get(roundIndex);
        }
    }

    /**
     * Method nextPhase updates the phase and the current player based on the current one. <br>
     * <b>Transitions</b>: <br>
     * planification_phase -> new player, move_st_phase if all players have played an assistant card. <br>
     * move_st_phase -> move_mn_phase. <br>
     * move_mn_phase -> move_cl_phase. <br>
     * move_cl_phase or move_mn_phase & skipCloudPhase (no more students available to refill them)
     * -> move_st_phase if there's another player that needs to play an action phase, else: <br>
     * if it was the last round -> endGame, else try to refill clouds (could trigger last round) -> planification_phase.
     *
     * @throws EndGameException if it's the last round and the last player finished their action phase.
     */
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
            if (gamePhase == GamePhase.MOVE_CL_PHASE || (gamePhase == GamePhase.MOVE_MN_PHASE && skipCloudPhase)) {
                //set the next player, if it's the last player of the round, round index will be 0
                nextPlayer();
                // it's not last action phase round
                if (roundIndex != 0) {
                    setPhase(GamePhase.MOVE_ST_PHASE);
                } else if (lastRound) {
                    // it's last player in action phase, and it's last round, end game
                    endGame();
                } else {
                    // it's last player in action phase, pass to new planification phase
                    try {
                        game.refillClouds();
                    } catch (EndGameException e) {
                        // couldn't refill all clouds, skip cloud phase next times
                        skipCloudPhase = true;
                        handleError(e);
                    }
                    playedCards.clear();
                    //there is a new turn completely
                    setPhase(GamePhase.PLANIFICATION_PHASE);
                }
            } else {
                //it's not last action phase
                //set the phase to the next action phase
                setPhase(GamePhase.values()[gamePhase.ordinal() + 1]);
            }
        }
    }

    /**
     * Method setPhase sets the game phase to the selected one.
     *
     * @param gamePhase of type {@link GamePhase} - game phase to set on the controller.
     */
    private void setPhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notifyClients(new Phase(gamePhase, currentPlayerIndex));
    }

    /**
     * Method endGame ends the game, calculating the winners and notifying them to all clients.
     */
    protected void endGame() {
        winners = game.calculateWinner();
        gameFinished = true;
        game.getGameDelta().send();
        notifyClients(new EndGame(winners));
        if (winners.size() == 3) System.out.println("Paolino tvb <3");
    }


    /**
     * Method handleError handles EndGameExceptions and checks if the game should end instantly or
     * there will be a last round to be played, informing then the clients.
     *
     * @param e of type {@link EndGameException} - instance of the exception.
     * @throws EndGameException if the game should end instantly (less than 3 islands left or a team has no more towers left).
     */
    protected void handleError(EndGameException e) throws EndGameException {
        // throws to client handler, in this way should also stop the controller function caller
        if (e.isEndInstantly()) throw e;
        else if (!lastRound) { // then continues to the controller function caller
            lastRound = true;
            broadcastMessage(new TextMessageSC("Server: This is the last round"));
        }
    }

    /**
     * Method getWinners returns all the teams that won the game.
     *
     * @return {@code ArrayList}<{@link HouseColor}> - list of the house color of the winners.
     */
    protected ArrayList<HouseColor> getWinners() {
        return winners;
    }

    /**
     * Method isGameFinished check if the controller's match is finished.
     *
     * @return {@code boolean} - true if the game is finished, false else.
     */
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Method getMatchId returns the unique ID of the controller's match.
     *
     * @return {@code Long} - ID of the controller's match.
     */
    protected Long getMatchId() {
        return matchId;
    }

    /**
     * Method repeatPhase is used to notify that there is again the same phase because there has been an error.
     */
    protected void repeatPhase() {
        setPhase(this.gamePhase);
    }
}
