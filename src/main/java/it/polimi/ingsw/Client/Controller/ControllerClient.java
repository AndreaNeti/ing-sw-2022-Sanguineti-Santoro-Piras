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
    private Map<CLICommands, GameCommand> commands;
    private Map<GamePhase, ClientPhaseController> phases;
    private GamePhase oldPhase;
    private Wizard myWizard;

    public ControllerClient() {
        socket = new Socket();
        playerClients = new ArrayList<>();
        myWizard = null;

    }

    public void setCommands(Map<CLICommands, GameCommand> commands) {
        this.commands = commands;
    }

    public void instantiateAllPhases() {
        phases = Map.ofEntries(
                entry(GamePhase.INIT_PHASE, new InitPhase(Arrays.asList(commands.get(CLICommands.CONNECT_SERVER), commands.get(CLICommands.QUIT)))),
                entry(GamePhase.NICK_PHASE, new NicknamePhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.SET_NICKNAME), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.CREATE_MATCH), commands.get(CLICommands.JOIN_MATCH_BY_TYPE), commands.get(CLICommands.JOIN_MATCH_BY_ID), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.WAIT_PHASE, new WaitPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.PLAY_CARD), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_STUDENT), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_MOTHER_NATURE), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.MOVE_FROM_CLOUD), commands.get(CLICommands.TEXT_MESSAGE), commands.get(CLICommands.QUIT))))),
                entry(GamePhase.SET_CH_CARD_PHASE, new SetCharacterInputPhase(new ArrayList<>(List.of(commands.get(CLICommands.QUIT))))),
                entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhase(new ArrayList<>(List.of(commands.get(CLICommands.QUIT))))),
                entry(GamePhase.QUIT_PHASE, new QuitPhase(new ArrayList<>(Arrays.asList(commands.get(CLICommands.UNDO), commands.get(CLICommands.QUIT))))));
        oldPhase = GamePhase.INIT_PHASE;
        notifyClientPhase(phases.get(oldPhase), false);
    }

    public boolean connect(byte[] ipAddress, int port) {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), port));
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        new Thread(new ServerListener(socket, this)).start();
        serverSender = new ServerSender(socket);


        return true;
    }

    public void setNextClientPhase() {
        GamePhase newPhase = GamePhase.values()[oldPhase.ordinal() + 1];
        super.notifyClientPhase(phases.get(newPhase), true);
        oldPhase = newPhase;

    }

    public void repeatPhase(boolean notifyScanner) {
        notifyClientPhase(phases.get(oldPhase), notifyScanner);
    }

    public void changePhaseAndCurrentPlayer(GamePhase gamePhase, Byte currentPlayer) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayer != null) {
            gameClient.setCurrentPlayer(currentPlayer);
            if (gameClient.getCurrentPlayer().getWizard() != myWizard) {
                gamePhase = GamePhase.WAIT_PHASE;
                oldPhase = gamePhase;
            } else {
                oldPhase = gamePhase;
            }
        }
        super.notifyClientPhase(phases.get(gamePhase), true);
    }

    public void addMembers(HashMap<Player, HouseColor> members) {
        this.playerClients = new ArrayList<>();
        if (myWizard == null) {
            myWizard = Wizard.values()[members.size() - 1];
        }
        for (Map.Entry<Player, HouseColor> entry : members.entrySet()) {
            playerClients.add(new PlayerClient(entry.getKey(), entry.getValue(), Server.getMatchConstants(matchType)));
        }
        if (playerClients.size() == matchType.nPlayers()) {
            //create a game
            gameClient = new GameClient(playerClients, myWizard, Server.getMatchConstants(matchType));
            // decorate phases
            if (matchType.isExpert()) {
                // TODO find a way to remove cast
                phases.put(GamePhase.MOVE_ST_PHASE, new ExpertClientPhaseDecorator((ClientPhase) phases.get(GamePhase.MOVE_ST_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.MOVE_MN_PHASE, new ExpertClientPhaseDecorator((ClientPhase) phases.get(GamePhase.MOVE_MN_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.MOVE_CL_PHASE, new ExpertClientPhaseDecorator((ClientPhase) phases.get(GamePhase.MOVE_CL_PHASE), List.of(commands.get(CLICommands.CHOOSE_CHARACTER))));
                phases.put(GamePhase.PLAY_CH_CARD_PHASE, new ExpertClientPhaseDecorator((ClientPhase) phases.get(GamePhase.PLAY_CH_CARD_PHASE), Arrays.asList(commands.get(CLICommands.PLAY_CHARACTER), commands.get(CLICommands.DELETE_LAST_INPUT), commands.get(CLICommands.UNDO))));
                phases.put(GamePhase.SET_CH_CARD_PHASE, new ExpertClientPhaseDecorator((ClientPhase) phases.get(GamePhase.SET_CH_CARD_PHASE), Arrays.asList(commands.get(CLICommands.SET_CHARACTER_INPUT), commands.get(CLICommands.PLAY_CHARACTER), commands.get(CLICommands.DELETE_LAST_INPUT), commands.get(CLICommands.UNDO))));
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
        repeatPhase(true);
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
            if(entry.getValue()!=null)
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

    // returns true if the client process has to quit
    public boolean setQuit() {
        if (gameClient != null) {
            changePhaseAndCurrentPlayer(GamePhase.SELECT_MATCH_PHASE, null);
            return false;
        } else if (oldPhase != GamePhase.INIT_PHASE) {
            changePhaseAndCurrentPlayer(GamePhase.INIT_PHASE, null);
            return false;
        } else
            return true;
    }
}
