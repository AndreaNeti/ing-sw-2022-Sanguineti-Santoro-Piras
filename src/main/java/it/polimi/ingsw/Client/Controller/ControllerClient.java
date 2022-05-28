package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.LimitedChat;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.*;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.*;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.model.GameClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Client.model.TeamClient;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.Server.model.Team;
import it.polimi.ingsw.network.toServerMessage.Quit;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ControllerClient extends GameClientListened {
    private ServerSender serverSender;
    private GameClient model;
    private MatchType matchType;
    private MatchConstants matchConstants;
    private ArrayList<TeamClient> teamsClient;
    private Map<CLICommands, GameCommand> commands;
    private Map<GamePhase, ClientPhaseController> phases;
    private GamePhase oldPhase, currentPhase;
    private Wizard myWizard;
    private ServerListener serverListener;
    private AbstractView abstractView;
    private boolean isInMatch;
    private boolean alreadyAttachedExpert = false;
    private final LimitedChat<String> chat;

    public ControllerClient() {
        this.chat = new LimitedChat<>(15);
    }

    public void instantiateAllPhases() {
        phases = Map.ofEntries(entry(GamePhase.INIT_PHASE, new InitPhase()), entry(GamePhase.NICK_PHASE, new NicknamePhase()), entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhase()), entry(GamePhase.WAIT_PHASE, new WaitPhase()), entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhase()), entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhase()), entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhase()), entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhase()), entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhase()));
        currentPhase = GamePhase.INIT_PHASE;
        attachCommandToPhase();
        notifyClientPhase(phases.get(currentPhase), false);
    }

    private void instantiateCommands() {
        commands = Map.ofEntries(entry(CLICommands.CONNECT_SERVER, new ConnectServerCommand(abstractView)), entry(CLICommands.SET_NICKNAME, new SetNicknameCommand(abstractView)), entry(CLICommands.CREATE_MATCH, new CreateMatchCommand(abstractView)), entry(CLICommands.JOIN_MATCH_BY_TYPE, new JoinMatchByTypeCommand(abstractView)), entry(CLICommands.JOIN_MATCH_BY_ID, new JoinMatchByIdCommand(abstractView)), entry(CLICommands.PLAY_CARD, new PlayCardCommand(abstractView)), entry(CLICommands.MOVE_STUDENT, new MoveStudentCommand(abstractView)), entry(CLICommands.MOVE_MOTHER_NATURE, new MoveMotherNatureCommand(abstractView)), entry(CLICommands.MOVE_FROM_CLOUD, new MoveFromCloudCommand(abstractView)), entry(CLICommands.TEXT_MESSAGE, new TextCommand(abstractView)), entry(CLICommands.CHOOSE_CHARACTER, new ChooseCharacterCommand(abstractView)), entry(CLICommands.SET_CHARACTER_INPUT, new SetCharacterInputCommand(abstractView)), entry(CLICommands.PLAY_CHARACTER, new PlayCharacterCommand(abstractView)), entry(CLICommands.DELETE_LAST_INPUT, new DeleteLastInputCommand(abstractView)), entry(CLICommands.UNDO, new UndoCommands(abstractView)), entry(CLICommands.QUIT, new QuitCommand(abstractView)));
    }

    private void attachCommandToPhase() {
        commands.get(CLICommands.CONNECT_SERVER).attachToAPhase(List.of(phases.get(GamePhase.INIT_PHASE)));
        commands.get(CLICommands.SET_NICKNAME).attachToAPhase(List.of(phases.get(GamePhase.NICK_PHASE)));
        commands.get(CLICommands.CREATE_MATCH).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.JOIN_MATCH_BY_TYPE).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.JOIN_MATCH_BY_ID).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.PLAY_CARD).attachToAPhase(List.of(phases.get(GamePhase.PLANIFICATION_PHASE)));
        commands.get(CLICommands.MOVE_STUDENT).attachToAPhase(List.of(phases.get(GamePhase.MOVE_ST_PHASE)));
        commands.get(CLICommands.MOVE_MOTHER_NATURE).attachToAPhase(List.of(phases.get(GamePhase.MOVE_MN_PHASE)));
        commands.get(CLICommands.MOVE_FROM_CLOUD).attachToAPhase(List.of(phases.get(GamePhase.MOVE_CL_PHASE)));
        commands.get(CLICommands.TEXT_MESSAGE).attachToAPhase(List.of(phases.get(GamePhase.WAIT_PHASE), phases.get(GamePhase.PLANIFICATION_PHASE), phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_MN_PHASE), phases.get(GamePhase.MOVE_CL_PHASE)));
        commands.get(CLICommands.QUIT).attachToAPhase(new ArrayList<>(phases.values()));
    }

    private void attachExpertCommand() {
        //this is needed, so it doesn't attach commands multiple times
        if (!alreadyAttachedExpert) {
            commands.get(CLICommands.CHOOSE_CHARACTER).attachToAPhase(Arrays.asList(phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_CL_PHASE), phases.get(GamePhase.MOVE_MN_PHASE)));
            commands.get(CLICommands.SET_CHARACTER_INPUT).attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            commands.get(CLICommands.PLAY_CHARACTER).attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            commands.get(CLICommands.UNDO).attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            alreadyAttachedExpert = true;
        }
    }


    public boolean connect(byte[] ipAddress) {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), Server.serverPort), 5000);
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        serverListener = new ServerListener(socket, this);
        new Thread(serverListener).start();
        serverSender = new ServerSender(socket);
        return true;
    }

    //this is called when is received an ack, it set the next phase in order
    public void setNextClientPhase() {
        GamePhase newPhase = GamePhase.values()[currentPhase.ordinal() + 1];
        notifyClientPhase(phases.get(newPhase), false);
        currentPhase = newPhase;
        oldPhase = currentPhase;
    }

    public void addMessage(String message) {
        chat.add(new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        notifyView();
    }

    public ArrayList<String> getChat() {
        return new ArrayList<>(chat);
    }

    public void repeatPhase(boolean forceScannerSkip) {
        notifyClientPhase(phases.get(currentPhase), forceScannerSkip);
    }

    public synchronized void changePhase(GamePhase newGamePhase, boolean setOldPhase, boolean forceScannerSkip) {
        currentPhase = newGamePhase;
        if (setOldPhase)
            oldPhase = currentPhase;
        notifyClientPhase(phases.get(currentPhase), forceScannerSkip);
    }

    public synchronized void goToOldPhase() {
        notifyClientPhase(phases.get(oldPhase), false);
    }

    public void changePhase(GamePhase gamePhase, Byte currentPlayer, boolean forceScannerSkip) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayer == null) return;
        model.setCurrentPlayer(currentPlayer);
        if (model.getCurrentPlayer().getWizard() != myWizard) {
            changePhase(GamePhase.WAIT_PHASE, true, forceScannerSkip);
        } else {
            changePhase(gamePhase, true, forceScannerSkip);
        }
    }


    public void sendMessage(ToServerMessage command) {
        if (serverSender == null) error(false);
        else serverSender.sendServerMessage(command);
    }

    public void error(boolean forceScannerSkip) {
        repeatPhase(forceScannerSkip);
    }

    public void ok() {
//        notifyOk();
    }

    public void changeGame(GameDelta gameDelta) {
        if (matchType.isExpert()) {
            if (gameDelta.getCharacters().size() != 0) model.setCharacters(gameDelta.getCharacters());
            gameDelta.getNewCoinsLeft().ifPresent(newCoinsLeft -> model.setNewCoinsLeft(newCoinsLeft));
            gameDelta.getNewProhibitionsLeft().ifPresent(newProhibitionsLeft -> model.setNewProhibitionsLeft(newProhibitionsLeft));
            gameDelta.getIgnoredColorInfluence().ifPresent((ignoredColorInfluence) -> model.setIgnoredColorInfluence(ignoredColorInfluence));
            for (Map.Entry<Byte, Byte> newEntry : gameDelta.getUpdatedCoinPlayer().entrySet())
                model.setUpdatedCoinPlayer(newEntry.getKey(), newEntry.getValue());
            for (Map.Entry<Byte, Boolean> entry : gameDelta.getUsedCharacter().entrySet()) {
                model.setUpdatedCharacter(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            model.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            if (entry.getValue() != null) model.setProfessors(entry.getKey(), entry.getValue());
        }

        for (Byte b : gameDelta.getDeletedIslands()) {
            model.removeIsland(b);
        }

        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> model.setMotherNaturePosition(mnPosition));

        gameDelta.getPlayedCard().ifPresent(playedCard -> model.playCard(playedCard));

        for (Map.Entry<HouseColor, Byte> entry : gameDelta.getNewTeamTowersLeft().entrySet()) {
            model.setTowerLeft(entry.getKey(), entry.getValue());
        }
    }

    public void addMember(Player playerJoined, HouseColor teamColor) {
        teamsClient.get(teamColor.ordinal()).addPlayer(new PlayerClient(playerJoined));
        super.notifyMembers(matchType.nPlayers() - playersInMatch(), playerJoined.toString());
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    public void setMatchInfo(MatchType matchType, List<Team> teams, Wizard myWizard) {
        this.myWizard = myWizard;
        isInMatch = true;
        this.matchType = matchType;
        this.matchConstants = Server.getMatchConstants(matchType);
        teamsClient = new ArrayList<>();
        for (Team t : teams)
            teamsClient.add(new TeamClient(t.getHouseColor(), t.getPlayers(), matchConstants));
        notifyMembers(matchType.nPlayers() - playersInMatch(), "You");
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    private void startGame() {
        model = new GameClient(teamsClient, myWizard, matchType);
        if (matchType.isExpert()) {
            attachExpertCommand();
        }
        model.addListener(this);
        abstractView.setModel(model);
    }

    // returns true if the client process has to quit
    public synchronized boolean setQuit(boolean forceScannerSkip) {
        if (isInMatch) {
            sendMessage(new Quit());
            unsetModel();
            changePhase(GamePhase.SELECT_MATCH_PHASE, true, forceScannerSkip);
            return false;
        } else if (currentPhase != GamePhase.INIT_PHASE) {
            chat.clear();
            sendMessage(new Quit());
            closeConnection();
            return false;
        } else return true;
    }

    protected void closeConnection() {
        // quit connection to server
        serverListener.quit();
        serverSender.closeStream();
    }

    protected void unsetModel() {
        chat.clear();
        isInMatch = false;
        model = null;
        abstractView.setModel(null);
    }

    public void notifyClientPhase(ClientPhaseController clientPhaseController, boolean forceScannerSkip) {
        notifyView();
        clientPhaseController.setPhaseInView(abstractView, forceScannerSkip);
    }

    public void attachView(AbstractView view) {
        this.abstractView = view;
        instantiateCommands();
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        model.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    private int playersInMatch() {
        int sum = 0;
        for (TeamClient t : teamsClient)
            sum += t.getPlayers().size();
        return sum;
    }

    public void unsetCurrentCharacterCard() {
        model.unsetCurrentCharacterCard();
    }
}
