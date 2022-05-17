package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.*;
import it.polimi.ingsw.Client.model.GameClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

import static java.util.Map.entry;

public class ControllerClient extends GameClientListened {
    private final Socket socket;
    private ServerSender serverSender;
    private GameClient gameClient;
    private MatchType matchType;
    private ArrayList<PlayerClient> playerClients;
    private List<ClientPhaseController> clientPhaseController;
    private Map<CLICommands, GameCommand> commands;
    private Map<GamePhase, ClientPhaseController> phases;
    private ClientPhaseController oldPhase;
    private Wizard wizardLocal;

    public ControllerClient() {
        socket = new Socket();
        playerClients = new ArrayList<>();
        wizardLocal = null;

    }

    public void setCommands(Map<CLICommands, GameCommand> commands) {
        this.commands = commands;
    }

    public void instantiateAllPhases() {
        phases = Map.ofEntries(
                entry(GamePhase.INIT_PHASE, new InitPhaseClient(Arrays.asList(commands.get(CLICommands.CONNECT_SERVER), commands.get(CLICommands.QUIT)))),
                entry(GamePhase.NICK_PHASE, new NicknamePhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.SET_NICKNAME), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.CREATE_MATCH), commands.get(CLICommands.JOIN_MATCH_BY_TYPE), commands.get(CLICommands.JOIN_MATCH_BY_ID), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.WAIT_PHASE, new WaitPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.PLAY_CARD), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_STUDENT), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_MOTHER_NATURE), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_FROM_CLOUD), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.SET_CH_CARD_PHASE, new SetCharacterInputPhaseClient(new ArrayList<>(List.of(commands.get(CLICommands.QUIT))))),
                entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhaseClient(new ArrayList<>(List.of(commands.get(CLICommands.QUIT))))),
                entry(GamePhase.QUIT_PHASE, new QuitPhaseClient(new ArrayList<>(Arrays.asList(commands.get(CLICommands.UNDO), commands.get(CLICommands.QUIT))))));
        oldPhase = phases.get(GamePhase.INIT_PHASE);
        //notifyClientPhase(oldPhase);
    }

    public boolean connect(byte[] ipAddress, int port) {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), port));
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        new Thread(new ServerListener(socket, this)).start();
        serverSender = new ServerSender(socket);

        setNewGamePhase();
        return true;
    }

    public void setNewGamePhase() {
        //TODO check if this work with quit
        //super.notify(phases.get(GamePhase.values()[oldPhase.ordinal() + 1]));
        //oldPhase = phases.get(GamePhase.values()[oldPhase.ordinal() + 1]);

    }

    public void changePhaseAndCurrentPlayer(GamePhase gamePhase, Byte currentPlayer) {
        //se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        //currentPlayer!=se stesso-> wait Phas
        // TODO set phase in view cli
        // this.gamePhase = gamePhase;
        gameClient.setCurrentPlayer(currentPlayer);
        if (gameClient.getCurrentPlayer().getWizard() != wizardLocal) {
            super.notify(GamePhase.WAIT_PHASE);
        } else {
            //oldPhase = gamePhase;
            super.notify(gamePhase);
        }
    }

    public void addMembers(HashMap<Player, HouseColor> members) {
        this.playerClients = new ArrayList<>();
        if (wizardLocal == null) {
            wizardLocal = Wizard.values()[members.size() - 1];
        }
        for (Map.Entry<Player, HouseColor> entry : members.entrySet()) {
            playerClients.add(new PlayerClient(entry.getKey(), entry.getValue(), Server.getMatchConstants(matchType)));
        }
        if (playerClients.size() == matchType.nPlayers()) {
            //create a game
            gameClient = new GameClient(playerClients, Server.getMatchConstants(matchType));
            // decorate phases
            if (matchType.isExpert()) {
                // TODO find a way to remove cast
                phases.put(GamePhase.MOVE_ST_PHASE, new ExpertClientGamePhaseDecorator((AbstractClientGamePhase) phases.get(GamePhase.MOVE_ST_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.MOVE_MN_PHASE, new ExpertClientGamePhaseDecorator((AbstractClientGamePhase) phases.get(GamePhase.MOVE_MN_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.MOVE_CL_PHASE, new ExpertClientGamePhaseDecorator((AbstractClientGamePhase) phases.get(GamePhase.MOVE_CL_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.PLAY_CH_CARD_PHASE, new ExpertClientGamePhaseDecorator((AbstractClientGamePhase) phases.get(GamePhase.PLAY_CH_CARD_PHASE), Arrays.asList(commands.get(CLICommands.PLAY_CHARACTER), commands.get(CLICommands.DELETE_LAST_INPUT), commands.get(CLICommands.UNDO))));
                phases.put(GamePhase.SET_CH_CARD_PHASE, new ExpertClientGamePhaseDecorator((AbstractClientGamePhase) phases.get(GamePhase.SET_CH_CARD_PHASE), Arrays.asList(commands.get(CLICommands.SET_CHARACTER_INPUT), commands.get(CLICommands.PLAY_CHARACTER), commands.get(CLICommands.DELETE_LAST_INPUT), commands.get(CLICommands.UNDO))));
            }
            gameClient.addListener(this);
            attachModel(gameClient);
        }
        super.notifyMembers(matchType.nPlayers() - playerClients.size());
    }

    public synchronized void sendMessage(ToServerMessage command) {
        if (serverSender == null) error("Must connect to a Server Before");
        else {
            serverSender.sendServerMessage(command);
        }
    }

    public void error(String e) {
        notifyError(e);
    }

    public void ok() {
        notifyOk();
    }

    public synchronized void changeGame(GameDelta gameDelta) {
        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            gameClient.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            gameClient.setProfessors(entry.getKey(), entry.getValue());
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

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }
}
