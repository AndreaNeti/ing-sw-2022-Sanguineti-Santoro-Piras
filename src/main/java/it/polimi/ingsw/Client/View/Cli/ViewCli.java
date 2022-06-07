package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.GamePhase;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.IPAddress;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.*;

public class ViewCli extends AbstractView implements ViewForCharacterCli {
    private ClientPhase phaseToExecute;
    private volatile boolean isInputReady, phaseChanged, requestInput, forcedScannerSkip, mustReprint;
    private String input;
    private final CliPrinter cliPrinter;

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        cliPrinter = new CliPrinter(this);
        controllerClient.addListener(cliPrinter);
        Thread scannerThread = new Thread(() -> {
            final Scanner myInput = new Scanner(System.in);
            try {
                while (!canQuit()) {
                    do {
                        input = myInput.nextLine();
                    } while (input.isBlank());
                    synchronized (this) {
                        isInputReady = true;
                        requestInput = false;
                        notifyAll();
                        while (!requestInput && !canQuit()) wait();
                    }
                }
            } catch (Exception ignored) {
            } finally {
                System.err.println("Closing...");
            }
        });
        mustReprint = false;
        isInputReady = false;
        phaseChanged = false;
        forcedScannerSkip = false;
        requestInput = false;
        scannerThread.setName("Scanner Thread");
        scannerThread.start();
        setPhaseInView(GamePhase.INIT_PHASE, true, false);
    }

    public void start() throws InterruptedException {
        do {
            synchronized (this) {
                while (phaseToExecute == null) wait();
                cliPrinter.printGame();
                phaseToExecute.playPhase(this);
            }
        } while (!canQuit());
    }

    // called by server listener or app thread
    @Override
    public synchronized void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution) {
        phaseToExecute = clientPhase;
        phaseChanged = true;
        if (forceImmediateExecution) forcedScannerSkip = true;
        notifyAll();
    }

    public void unsetPhase() {
        phaseToExecute = null;
    }

    protected synchronized void setMustReprint(boolean reprint) {
        this.mustReprint = reprint;
        if (reprint) notifyAll();
    }

    private String optionString(int value, String option) {
        return "[" + value + "] " + option + " ";
    }

    private String optionString(SortedSet<Integer> values, String option) {
        int start, end, temp;
        StringBuilder s = new StringBuilder();
        if (!values.isEmpty()) {
            start = values.first();
            end = start;
            for (Integer integer : values) {
                temp = integer;
                // not adjacent
                if (temp > end + 1) {
                    // last was an interval
                    if (end > start) {
                        s.append("[").append(start).append("-").append(end).append("]");
                    } else {
                        s.append("[").append(start).append("]");
                    }
                    start = temp;
                }
                end = temp;
            }

            if (end > start) {
                s.append("[").append(start).append("-").append(end).append("]");
            } else {
                s.append("[").append(start).append("]");
            }
            s.append(" ").append(option);
        }
        return s.toString();
    }

    private int getIntInput(Set<Integer> optionValues, String message, boolean canBeStopped) throws SkipCommandException {
        if (optionValues.isEmpty()) throw new SkipCommandException();
        int option = getIntInput(message, canBeStopped);
        while (!optionValues.contains(option)) {
            cliPrinter.printGame();
            System.err.println("Not a valid input");
            option = getIntInput(message, canBeStopped);
        }
        return option;
    }

    public int getIntInput(Object[] options, String message, boolean canBeStopped) throws SkipCommandException {
        StringBuilder s = new StringBuilder("--OPTIONS--\n");
        for (byte i = 0; i < options.length; i++)
            s.append(optionString(i, options[i].toString())).append("\n");
        s.append(message);
        return getIntInput(0, options.length - 1, s.toString(), canBeStopped);
    }

    public int getIntInput(int min, int max, String message, boolean canBeStopped) throws SkipCommandException {
        message += " (from " + min + " to " + max + ")";
        int ret = getIntInput(message, canBeStopped);
        while (ret < min || ret > max) {
            cliPrinter.printGame();
            System.err.println("Not a valid input");
            ret = getIntInput(message, canBeStopped);
        }
        return ret;
    }

    //     Message is the string printed before asking the input
    public int getIntInput(String message, boolean canBeStopped) throws SkipCommandException {
        message += ": ";
        int ret = 0;
        boolean err;
        do {
            err = false;
            try {
                ret = Integer.parseInt(getInput(message, canBeStopped));
            } catch (NumberFormatException ex) {
                cliPrinter.printGame();
                err = true;
                System.err.println("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public byte[] getIpAddressInput(boolean canBeStopped) throws SkipCommandException {
        String input = getStringInput("Select server IP", 15, canBeStopped);
        byte[] ret = IPAddress.getIpFromString(input);
        while (ret == null) {
            cliPrinter.printGame();
            System.err.println("Not a valid IP");
            input = getStringInput("Select server IP", 15, canBeStopped);
            ret = IPAddress.getIpFromString(input);
        }
        return ret;
    }


    @Override
    public Color getColorInput(boolean canBeStopped) throws SkipCommandException {
        StringBuilder message = new StringBuilder("--COLORS--\n");
        for (Color c : Color.values())
            message.append(optionString(c.ordinal() + 1, c.toString()));
        message.append("\nSelect a color");
        return Color.values()[getIntInput(1, Color.values().length, message.toString(), canBeStopped) - 1];
    }

    public MatchType getMatchTypeInput(boolean canBeStopped) throws SkipCommandException {
        return new MatchType((byte) getIntInput(2, 4, "Select the number of players", canBeStopped), getBooleanInput("Do you want to play in expert mode?", canBeStopped));
    }

    public byte getCharacterCharToPlayInput() throws SkipCommandException {
        List<CharacterCardClient> characterCards = getModel().getCharacters();
        StringBuilder message = new StringBuilder("--CHARACTERS--\n");
        for (int i = 0; i < characterCards.size(); i++)
            message.append(optionString(i + 1, characterCards.get(i) + ": " + characterCards.get(i).getDescription())).append("\n");
        message.append("Select the character to play");
        return (byte) (getIntInput(1, characterCards.size(), message.toString(), false) - 1);
    }

    public AssistantCard getAssistantCardToPlayInput(boolean canBeStopped) throws SkipCommandException {
        TreeSet<Integer> choices = new TreeSet<>();
        List<AssistantCard> assistantCards = getModel().getCurrentPlayer().getAssistantCards();
        for (AssistantCard card : assistantCards)
            choices.add((int) card.value());

        String message = "--OPTIONS--\n" + optionString(choices, "Assistant card") + "\nSelect an assistant card to play";
        Optional<AssistantCard> ret;
        do {
            byte chosenCardValue = (byte) getIntInput(choices, message, canBeStopped);
            ret = assistantCards.stream().filter(assistantCard -> assistantCard.value() == chosenCardValue).findFirst();
        } while (ret.isEmpty());
        return ret.get();
    }

    public GameComponentClient getMoveStudentDestination(boolean canBeStopped) throws SkipCommandException {
        List<GameComponentClient> validDestinations = new ArrayList<>();

        GameComponentClient lunchHall = getModel().getCurrentPlayer().getLunchHall();
        List<IslandClient> islands = getModel().getIslands();

        TreeSet<Integer> choices = new TreeSet<>();

        for (IslandClient island : islands)
            choices.add(islands.indexOf(island) + 1);
        String message = "--OPTIONS--\n[0] " + lunchHall.getNameOfComponent() + "\n" + optionString(choices, islands.get(0).getNameOfComponent()) + "\nSelect a destination";

        choices.add(0);

        validDestinations.add(lunchHall);
        validDestinations.addAll(islands);

        int input = getIntInput(choices, message, canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return validDestinations.get(index);
    }

    @Override
    public int getIslandDestination(String message, boolean canBeStopped) throws SkipCommandException {
        List<IslandClient> islandClients = getModel().getIslands();
        SortedSet<Integer> choices = new TreeSet<>();
        for (IslandClient i : islandClients)
            choices.add(islandClients.indexOf(i) + 1);
        String s = "--OPTIONS--\n" + optionString(choices, islandClients.get(0).getNameOfComponent()) + "\n" + message;
        int input = getIntInput(choices, s, canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return islandClients.get(index).getId();
    }

    public byte getMotherNatureMovesInput(boolean canBeStopped) throws SkipCommandException {
        byte maxMoves = getModel().getCurrentPlayer().getPlayedCard().moves();
        if (getModel().isExtraSteps()) maxMoves += 2;
        return (byte) getIntInput(1, maxMoves, "How many steps do you want mother nature to move?", canBeStopped);
    }

    public int getCloudSource(boolean canBeStopped) throws SkipCommandException {
        List<GameComponentClient> clouds = getModel().getClouds();
        List<GameComponentClient> availableClouds = new ArrayList<>();
        SortedSet<Integer> choices = new TreeSet<>();
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : clouds) {
            if (cloud.howManyStudents() > 0) {
                int cloudIndex = clouds.indexOf(cloud) + 1;
                availableClouds.add(cloud);
                choices.add(cloudIndex);
            }
        }
        String message = "--OPTIONS--\n" + optionString(choices, clouds.get(0).getNameOfComponent()) + "\nSelect a cloud source";
        int input = getIntInput(choices, message, canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return availableClouds.get(index).getId();
    }

    public boolean getBooleanInput(String message, boolean canBeStopped) throws SkipCommandException {
        message += " (Y/N):";
        String s = getInput(message, canBeStopped).toLowerCase();
        while (!s.equals("y") && !s.equals("n")) {
            cliPrinter.printGame();
            System.err.println("Not a valid input");
            s = getInput(message, canBeStopped).toLowerCase();
        }
        return s.equals("y");
    }

    public String getStringInput(String message, int maxLength, boolean canBeStopped) throws SkipCommandException {
        message += ": ";
        String s = getInput(message, canBeStopped);
        while (s.length() > maxLength) {
            cliPrinter.printGame();
            System.err.println("Max length is " + maxLength);
            s = getInput(message, canBeStopped);
        }
        return s;
    }

    public long getLongInput(String message, boolean canBeStopped) throws SkipCommandException {
        message += ": ";
        long ret = 0;
        boolean err;
        do {
            err = false;
            try {
                ret = Long.parseLong(getInput(message, canBeStopped));
            } catch (NumberFormatException ex) {
                cliPrinter.printGame();
                err = true;
                System.err.println("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public synchronized String getInput(String message, boolean canBeStopped) throws SkipCommandException {
        boolean repeatCommand;
        do {
            System.out.println(message);
            repeatCommand = false;
            isInputReady = false;
            phaseChanged = false;
            forcedScannerSkip = false;
            this.input = "";
            // wakes up scanner thread
            requestInput = true;
            notifyAll();
            try {
                // wait for scanner notify or phase changed flag (if it can be stopped) or forced skip (used if someone lost connection)
                // or if there has been some changes, and it needs to be reprinted
                while (!(isInputReady || forcedScannerSkip || (canBeStopped && (mustReprint || phaseChanged)))) {
                    wait();
                }
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted");
                throw new SkipCommandException();
            }
            if (mustReprint && !phaseChanged) {
                // reprint game, if phase changed will reprint in next phase
                cliPrinter.printGame();
                // if one of these conditions is true it should not repeat the command, skip instead to the new one
                if (!(isInputReady || forcedScannerSkip)) repeatCommand = true;
            }
        } while (repeatCommand);
        // if input is ready, return it
        if (isInputReady) return this.input;
        // input not ready and all others conditions are not verified, skip to new command
        throw new SkipCommandException();
    }

    @Override
    public void setQuit(boolean forceImmediateExecution) {
        super.setQuit(forceImmediateExecution);
        // notifies scanner to wakeup and terminate
        notifyAll();
    }

    @Override
    public void setModel(GameClientView model) {
        super.setModel(model);
    }
}
