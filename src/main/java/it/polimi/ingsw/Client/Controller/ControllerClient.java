package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
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
import java.util.*;

import static java.util.Map.entry;

public class ControllerClient extends GameClientListened {
    private ServerSender serverSender;
    private GameClient gameClient;
    private MatchType matchType;
    private MatchConstants matchConstants;
    private ArrayList<TeamClient> teamsClient;
    private Map<CLICommands, GameCommand> commands;
    private Map<GamePhase, ClientPhaseController> phases;
    private GamePhase oldPhase;
    private Wizard myWizard;
    private ServerListener serverListener;
    private AbstractView abstractView;

    private boolean isInMatch;
    private boolean alreadyAttachedExpert = false;

    public void instantiateAllPhases() {
        phases = Map.ofEntries(entry(GamePhase.INIT_PHASE, new InitPhase()), entry(GamePhase.NICK_PHASE, new NicknamePhase()), entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhase()), entry(GamePhase.WAIT_PHASE, new WaitPhase()), entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhase()), entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhase()), entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhase()), entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhase()), entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhase()));
        oldPhase = GamePhase.INIT_PHASE;
        attachCommandToPhase();
        notifyClientPhase(phases.get(oldPhase), false);
    }

    private void instantiateCommands() {
        commands = Map.ofEntries(entry(CLICommands.CONNECT_SERVER, new ConnectServerCommand(abstractView)), entry(CLICommands.SET_NICKNAME, new SetNicknameCommand(abstractView)), entry(CLICommands.CREATE_MATCH, new CreateMatchCommand(abstractView)), entry(CLICommands.JOIN_MATCH_BY_TYPE, new JoinMatchByTypeCommand(abstractView)), entry(CLICommands.JOIN_MATCH_BY_ID, new JoinMatchByIdCommand(abstractView)), entry(CLICommands.PLAY_CARD, new PlayCardCommand(abstractView)), entry(CLICommands.MOVE_STUDENT, new MoveStudentCommand(abstractView)), entry(CLICommands.MOVE_MOTHER_NATURE, new MoveMotherNatureCommand(abstractView)), entry(CLICommands.MOVE_FROM_CLOUD, new MoveFromCloudCommand(abstractView)), entry(CLICommands.TEXT_MESSAGE, new TextCommand(abstractView)), entry(CLICommands.QUIT, new QuitCommand(abstractView)), entry(CLICommands.SHOW_ENTRANCE_HALL, new ShowEntranceHall(abstractView)), entry(CLICommands.CHOOSE_CHARACTER, new ChooseCharacterCommand(abstractView)), entry(CLICommands.SET_CHARACTER_INPUT, new SetCharacterInputCommand(abstractView)), entry(CLICommands.PLAY_CHARACTER, new PlayCharacterCommand(abstractView)), entry(CLICommands.DELETE_LAST_INPUT, new DeleteLastInputCommand(abstractView)), entry(CLICommands.GET_DESCRIPTION, new GetDescriptionCommand(abstractView)), entry(CLICommands.UNDO, new UndoCommands(abstractView)));
    }

    private void attachCommandToPhase() {
        commands.get(CLICommands.CONNECT_SERVER).attachToAPhase(List.of(phases.get(GamePhase.INIT_PHASE)));
        commands.get(CLICommands.SET_NICKNAME).attachToAPhase(List.of(phases.get(GamePhase.NICK_PHASE)));
        commands.get(CLICommands.CREATE_MATCH).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.JOIN_MATCH_BY_TYPE).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.JOIN_MATCH_BY_ID).attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        commands.get(CLICommands.PLAY_CARD).attachToAPhase(List.of(phases.get(GamePhase.PLANIFICATION_PHASE)));
        commands.get(CLICommands.MOVE_STUDENT).attachToAPhase(List.of(phases.get(GamePhase.MOVE_ST_PHASE)));
        commands.get(CLICommands.SHOW_ENTRANCE_HALL).attachToAPhase(Arrays.asList(phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_MN_PHASE), phases.get(GamePhase.MOVE_CL_PHASE)));
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
            commands.get(CLICommands.GET_DESCRIPTION).attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            alreadyAttachedExpert = true;
        }
    }


    public boolean connect(byte[] ipAddress) {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), Server.serverPort));
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        serverListener = new ServerListener(socket, this);
        new Thread(serverListener).start();
        serverSender = new ServerSender(socket);
        return true;
    }

    public void setNextClientPhase() {
        GamePhase newPhase = GamePhase.values()[oldPhase.ordinal() + 1];
        notifyClientPhase(phases.get(newPhase), false);
        oldPhase = newPhase;

    }

    public void addMessage(String message) {
        if (gameClient != null)
            gameClient.addMessage(message);
        notifyView();
//        repeatPhase(false);
    }

    public void repeatPhase(boolean forceScannerSkip) {
        notifyClientPhase(phases.get(oldPhase), forceScannerSkip);
    }

    public synchronized void changePhase(GamePhase newGamePhase, boolean setOldPhase, boolean forceScannerSkip) {
        if (setOldPhase) oldPhase = newGamePhase;
        notifyClientPhase(phases.get(newGamePhase), forceScannerSkip);
    }

    public synchronized void goToOldPhase() {
        notifyClientPhase(phases.get(oldPhase), false);
    }

    public void changePhase(GamePhase gamePhase, Byte currentPlayer, boolean forceScannerSkip) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayer == null) return;
        gameClient.setCurrentPlayer(currentPlayer);
        if (gameClient.getCurrentPlayer().getWizard() != myWizard) {
            changePhase(GamePhase.WAIT_PHASE, true, forceScannerSkip);
        } else {
            changePhase(gamePhase, true, forceScannerSkip);
        }
    }

    public void addMember(Player playerJoined, HouseColor teamColor) {
        teamsClient.get(teamColor.ordinal()).addPlayer(new PlayerClient(playerJoined, matchConstants));
        super.notifyMembers(matchType.nPlayers() - playersInMatch());
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    public void sendMessage(ToServerMessage command) {
        if (serverSender == null) error("Must connect to a Server Before", false);
        else serverSender.sendServerMessage(command);
    }

    public void error(String e, boolean forceScannerSkip) {
        repeatPhase(forceScannerSkip);
//        notifyError(e);
    }

    public void ok() {
//        notifyOk();
    }

    public void changeGame(GameDelta gameDelta) {
        if (matchType.isExpert()) {
            if (gameDelta.getCharacters().size() != 0) gameClient.setCharacters(gameDelta.getCharacters());
            gameDelta.getNewCoinsLeft().ifPresent(newCoinsLeft -> gameClient.setNewCoinsLeft(newCoinsLeft));
            gameDelta.getNewProhibitionsLeft().ifPresent(newProhibitionsLeft -> gameClient.setNewProhibitionsLeft(newProhibitionsLeft));
            for (Map.Entry<Byte, Byte> newEntry : gameDelta.getUpdatedCoinPlayer().entrySet())
                gameClient.setUpdatedCoinPlayer(newEntry.getKey(), newEntry.getValue());
        }

        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            gameClient.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            if (entry.getValue() != null) gameClient.setProfessors(entry.getKey(), entry.getValue());
        }

        for (Byte b : gameDelta.getDeletedIslands()) {
            gameClient.removeIsland(b);
        }

        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> gameClient.setMotherNaturePosition(mnPosition));

        gameDelta.getPlayedCard().ifPresent(playedCard -> gameClient.playCard(playedCard));

        for (Map.Entry<HouseColor, Byte> entry : gameDelta.getNewTeamTowersLeft().entrySet()) {
            gameClient.setTowerLeft(entry.getKey(), entry.getValue());
        }
    }

    public void setMatchInfo(MatchType matchType, List<Team> teams, Wizard myWizard) {
        this.myWizard = myWizard;
        isInMatch = true;
        this.matchType = matchType;
        this.matchConstants = Server.getMatchConstants(matchType);
        teamsClient = new ArrayList<>();
        for (Team t : teams)
            teamsClient.add(new TeamClient(t.getHouseColor(), t.getPlayers(), matchConstants));
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    private void startGame() {
        //create a game
        gameClient = new GameClient(teamsClient, myWizard, matchType);
        // decorate phases
        if (matchType.isExpert()) {
            attachExpertCommand();
        }
        gameClient.addListener(this);
        abstractView.setModel(gameClient);
    }

    // returns true if the client process has to quit
    public synchronized boolean setQuit(boolean forceScannerSkip) {
        if (isInMatch) {
            changePhase(GamePhase.SELECT_MATCH_PHASE, true, forceScannerSkip);
            isInMatch = false;
            gameClient = null;
            abstractView.setModel(null);
            return false;
        } else if (oldPhase != GamePhase.INIT_PHASE) {
            sendMessage(new Quit());
            // quit connection to server
            serverListener.quit();
            serverSender.closeStream();
            return false;
        } else return true;
    }

    public void notifyClientPhase(ClientPhaseController clientPhaseController, boolean forceScannerSkip) {
        clientPhaseController.setPhaseInView(abstractView, forceScannerSkip);
    }

    public void attachView(AbstractView view) {
        this.abstractView = view;
        instantiateCommands();
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        gameClient.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    private int playersInMatch() {
        int sum = 0;
        for (TeamClient t : teamsClient)
            sum += t.getPlayers().size();
        return sum;
    }
}
