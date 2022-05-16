package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.View.GamePhaseView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toServerMessage.*;

import java.util.*;

import static java.util.Map.entry;

public class ViewCli extends AbstractView implements GameClientListener {

    private final Scanner myInput = new Scanner(System.in);

    private static final Map<GamePhase, List<CLICommands>> phaseCommands = Map.ofEntries(
            entry(GamePhase.INIT_PHASE, new ArrayList<>(Arrays.asList(CLICommands.CONNECT_SERVER, CLICommands.QUIT))),
            entry(GamePhase.NICK_PHASE, new ArrayList<>(Arrays.asList(CLICommands.SET_NICKNAME, CLICommands.QUIT))),
            entry(GamePhase.SELECT_MATCH_PHASE, new ArrayList<>(Arrays.asList(CLICommands.CREATE_MATCH, CLICommands.JOIN_MATCH_BY_TYPE, CLICommands.JOIN_MATCH_BY_ID, CLICommands.QUIT))),
            entry(GamePhase.WAIT_PHASE, new ArrayList<>(Arrays.asList(CLICommands.TEXT_MESSAGE, CLICommands.QUIT))),
            entry(GamePhase.PLANIFICATION_PHASE, new ArrayList<>(Arrays.asList(CLICommands.PLAY_CARD, CLICommands.TEXT_MESSAGE, CLICommands.QUIT))),
            entry(GamePhase.MOVE_ST_PHASE, new ArrayList<>(Arrays.asList(CLICommands.MOVE_STUDENT, CLICommands.CHOOSE_CHARACTER, CLICommands.TEXT_MESSAGE, CLICommands.QUIT))),
            entry(GamePhase.MOVE_MN_PHASE, new ArrayList<>(Arrays.asList(CLICommands.MOVE_MOTHER_NATURE, CLICommands.CHOOSE_CHARACTER, CLICommands.TEXT_MESSAGE, CLICommands.QUIT))),
            entry(GamePhase.MOVE_CL_PHASE, new ArrayList<>(Arrays.asList(CLICommands.MOVE_FROM_CLOUD, CLICommands.CHOOSE_CHARACTER, CLICommands.TEXT_MESSAGE, CLICommands.QUIT))),
            entry(GamePhase.CH_CARD_PHASE, new ArrayList<>(Arrays.asList(CLICommands.SET_CHARACTER_INPUT, CLICommands.PLAY_CHARACTER, CLICommands.DELETE_LAST_INPUT, CLICommands.UNDO, CLICommands.QUIT))),
            entry(GamePhase.PLAY_CH_CARD_PHASE, new ArrayList<>(Arrays.asList(CLICommands.PLAY_CHARACTER, CLICommands.DELETE_LAST_INPUT, CLICommands.UNDO, CLICommands.QUIT))),
            entry(GamePhase.QUIT_PHASE, new ArrayList<>(Arrays.asList(CLICommands.UNDO, CLICommands.QUIT)))
    );

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        System.out.println("You've chosen to play with client line interface");
        //oldPhase = gamePhase;
    }

    public ToServerMessage playCLICommand(CLICommands command) {
        switch (command) {
            case CONNECT_SERVER -> {
                if (!getControllerClient().connect(new byte[]{127, 0, 0, 1}, myInput.nextInt())) {
                    System.out.println("Cannot connect to this server");
                } else getControllerClient().setNewGamePhase();
                return null;
            }
            case SET_NICKNAME -> {
                return new NickName(new Scanner(System.in).next());
            }
            case CREATE_MATCH -> {
                MatchType mt = new MatchType((byte) myInput.nextInt(), myInput.nextBoolean());

                return new CreateMatch(mt);
            }
            case JOIN_MATCH_BY_ID -> {
                return new JoinMatchById(myInput.nextLong());
            }
            case JOIN_MATCH_BY_TYPE -> {
                MatchType mt = new MatchType((byte) myInput.nextInt(), myInput.nextBoolean());
                getControllerClient().setMatchType(mt);
                return new JoinMatchByType(mt);
            }
            case PLAY_CARD -> {
                return new PlayCard((byte) 3);
            }
            case MOVE_STUDENT -> {
                return new MoveStudent(Color.BLUE, 4);
            }
            case MOVE_MOTHER_NATURE -> {
                return new MoveMotherNature(3);
            }
            case MOVE_FROM_CLOUD -> {
                return new MoveFromCloud(-3);
            }
            case CHOOSE_CHARACTER -> {
                return new ChooseCharacter((byte) 1);
            }
            case SET_CHARACTER_INPUT -> {
                return new SetCharacterInput(4);
            }
            case PLAY_CHARACTER -> {
                return new PlayCharacter();
            }
            case TEXT_MESSAGE -> {
                return new TextMessageCS(new Scanner(System.in).next());
            }
            case QUIT -> {
                return new Quit();
            }
            default -> {
                System.err.println("Not a valid command");
                return null;
            }
        }
    }

    public void start() {
        System.out.println("You've chosen to play with command-line");
        List<CLICommands> availableCommands;

        int number;
        do {
            availableCommands = phaseCommands.get(gamePhase);

            System.out.println("What do you want to do?");
            System.out.println("--" + gamePhase + "--");
            for (byte i = 0; i < availableCommands.size(); i++)
                System.out.println(i + ") " + availableCommands.get(i));
            number = myInput.nextInt();
            ToServerMessage send = playCLICommand(availableCommands.get(number));
            if (send != null)
                sendToServer(send);
        } while (number != -1);
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
        System.out.println("New motherNaturePosition is " + motherNaturePosition);
    }

    @Override
    public void update(GameComponentClient gameComponent) {
        System.out.println("New gameComponent is " + gameComponent);
    }

    @Override
    public void update(IslandClient island) {
        System.out.println("New gameComponent is " + island);
    }

    @Override
    public void update(ArrayList<IslandClient> islands) {
        System.out.println("Printing all the island");
        for (IslandClient i : islands) {
            System.out.println(i);
        }
    }

    @Override
    public void update(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void update(Wizard[] professors) {

    }

    @Override
    public void update(String currentPlayer) {
        System.out.println(currentPlayer + " is now playing his turn");
    }

    @Override
    public void updateMembers(int membersLeftToStart) {
        if (membersLeftToStart > 0)
            System.out.println(membersLeftToStart + " members left before game starts");
        else System.out.println("Game is about to start");
    }

    @Override
    public void update(GamePhase newPhase) {
        gamePhase = newPhase;
    }

    @Override
    public void updateCardPlayed(Byte playedCard) {

    }

    @Override
    public void setView(GamePhaseView gamePhaseView) {
        gamePhaseView.playPhase(this);
    }

    @Override
    public void error(String e) {
        System.out.println("Operation failed because " + e);
    }

    @Override
    public void ok() {
        System.out.println("Successful operation");
    }

    // Message is the string printed before asking the input
    private int getIntInput(String message) {
        System.out.println(message);
        return myInput.nextInt();
    }

    private int getIntInput(int min, int max, String message) {
        int ret = getIntInput(message + " (from " + min + " to " + max + "): ");
        while (ret < min || ret > max) {
            ret = getIntInput("Not a valid input (from " + min + " to " + max + ")\n" + message + ": ");
        }
        return ret;
    }

    private String getOptions(Object[] options) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < options.length; i++)
            ret.append(i).append(") ").append(options[i].toString()).append("\n");
        return ret.toString();
    }

    public int getServerPortInput() {
        return getIntInput(0, 65535, "Select server port");
    }

    public int getColorInput() {
        return getIntInput(0, Color.values().length, "Choose a color" + getOptions(Color.values()));
    }

    public MatchType getMatchTypeInput() {
        return new MatchType((byte) getIntInput(2, 4, "Choose the number of players"), getBooleanInput("Do you want to play in expert mode?"));
    }

    public byte getAssistantCardToPlayInput() {
        boolean[] usedCards = getModel().getCurrentPlayer().getUsedCards();
        byte ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        while (!usedCards[ret]) {
            System.out.println("Card already played");
            ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        }
        return ret;
    }

    public byte getMotherNatureMovesInput() {
        return (byte) getIntInput(1, getModel().getCurrentPlayer().getPlayedCardMoves(), "How many steps do you want mother nature to move?");
    }

    public int getCloudSource() {
        ArrayList<GameComponentClient> availableClouds = new ArrayList<>();
        int i = 0;
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : getModel().getClouds()) {
            if (cloud.howManyStudents() > 0)
                availableClouds.add(cloud);
        }
        System.out.println(getOptions(availableClouds.toArray()));
        int cloudIndex = getIntInput(0, availableClouds.size() - 1, "Choose a cloud source");
        return availableClouds.get(cloudIndex).getId();
    }

    public boolean getBooleanInput(String message) {
        System.out.println(message + "(TRUE/FALSE):");
        return myInput.nextBoolean();
    }


    public String getStringInput(String message) {
        System.out.println(message);
        String ret = myInput.next();
        while (ret.isBlank()) {
            System.out.println("Input can't be empty.\n" + message);
            ret = myInput.next();
        }
        return ret;
    }

    public void print(String s) {
        System.out.println(s);
    }

    public Long getLongInput(String message) {
        System.out.println(message);
        return myInput.nextLong();
    }

}
