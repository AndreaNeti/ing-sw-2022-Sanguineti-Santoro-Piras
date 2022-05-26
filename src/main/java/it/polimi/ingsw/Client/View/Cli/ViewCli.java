package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ViewCli extends AbstractView implements ViewForCharacterCli {
    private ClientPhaseView phaseToExecute;
    private volatile boolean isInputReady, phaseChanged, requestInput, forcedScannerSkip, mustReprint;
    private String input;
    private final CliPrinter cliPrinter;

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        cliPrinter = new CliPrinter(this);
        controllerClient.addListener(cliPrinter);
        Thread scannerThread = new Thread(() -> {
            final Scanner myInput = new Scanner(System.in, StandardCharsets.UTF_8);
            try {
                while (!canQuit()) {
                    do {
                        input = myInput.nextLine();
                    } while (input.isBlank());
                    synchronized (this) {
                        isInputReady = true;
                        requestInput = false;
                        notifyAll();
                        while (!requestInput && !canQuit())
                            wait();
                    }
                }
            } catch (Exception ignored) {
            } finally {
                System.err.println("Closing...");
            }
        });
        phaseToExecute = null;
        isInputReady = false;
        phaseChanged = false;
        forcedScannerSkip = false;
        requestInput = false;
        scannerThread.setName("Scanner Thread");
        scannerThread.start();
    }

    public void start() throws InterruptedException {
        do {
            synchronized (this) {
                while (phaseToExecute == null)
                    wait();
                if (mustReprint)
                    cliPrinter.printGame();
                phaseToExecute.playPhase(this);
            }
        } while (!canQuit());
    }

    // called by server listener or app thread
    @Override
    public synchronized void setPhaseInView(ClientPhaseView clientPhaseView, boolean forceScannerSkip) {
        phaseToExecute = clientPhaseView;
        phaseChanged = true;
        if (forceScannerSkip)
            forcedScannerSkip = true;
        notifyAll();
    }

    public void unsetPhase() {
        phaseToExecute = null;
    }

    protected synchronized void setMustReprint(boolean reprint) {
        this.mustReprint = reprint;
        if (reprint)
            notifyAll();
    }

    private String optionString(int value, String option) {
        return "[" + value + "] " + option;
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

    private int getIntInput(Set<Integer> optionValues, String message, boolean canBeStopped) throws ScannerException {
        if (optionValues.isEmpty()) throw new SkipCommandException();
        int option = getIntInput(message, canBeStopped);
        while (!optionValues.contains(option)) {
            System.err.println("Not a valid input");
            option = getIntInput(message, canBeStopped);
        }
        return option;
    }

    public int getIntInput(Object[] options, String message, boolean canBeStopped) throws ScannerException {
        System.out.println("--OPTIONS--");
        for (byte i = 0; i < options.length; i++)
            System.out.println(optionString(i, options[i].toString()));
        return getIntInput(0, options.length - 1, message, canBeStopped);
    }

    public int getIntInput(int min, int max, String message, boolean canBeStopped) throws ScannerException {
        int ret = getIntInput(message + " (from " + min + " to " + max + ")", canBeStopped);
        while (ret < min || ret > max) {
            System.err.println("Not a valid input (from " + min + " to " + max + ")");
            ret = getIntInput(message, canBeStopped);
        }
        return ret;
    }

    //     Message is the string printed before asking the input
    public int getIntInput(String message, boolean canBeStopped) throws ScannerException {
        int ret = 0;
        boolean err;
        do {
            err = false;
            System.out.println(message + ":");
            try {
                ret = Integer.parseInt(getInput(canBeStopped));
            } catch (NumberFormatException ex) {
                err = true;
                System.err.println("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public byte[] getIpAddressInput(boolean canBeStopped) throws ScannerException {
        String input = getStringInput("Select server IP", 15, canBeStopped);
        byte[] ret = getIpFromString(input);
        while (ret == null) {
            System.err.println("Not a valid IP");
            input = getStringInput("Select server IP", 15, canBeStopped);
            ret = getIpFromString(input);
        }
        return ret;
    }

    // return null if it's not a valid ip, otherwise returns the IP bytes
    private byte[] getIpFromString(String ip) {
        if (ip.equals("localhost") || ip.equals("l") || ip.equals("paolino")) return new byte[]{127, 0, 0, 1};
        String[] bytes = ip.split("[.]");
        if (bytes.length != 4) return null;
        byte[] ret = new byte[4];
        for (byte i = 0; i < 4; i++) {
            int x; // just because java doesn't have unsigned bytes
            try {
                x = Integer.parseInt(bytes[i]);
            } catch (NumberFormatException e) {
                return null;
            }
            if (x < 0 || x > 255) return null;
            ret[i] = (byte) x;
        }
        return ret;
    }

    public int getColorInput(boolean canBeStopped) throws ScannerException {
        return getIntInput(Color.values(), "Select a color", canBeStopped);
    }

    public MatchType getMatchTypeInput(boolean canBeStopped) throws ScannerException {
        return new MatchType((byte) getIntInput(2, 4, "Select the number of players", canBeStopped), getBooleanInput("Do you want to play in expert mode?", canBeStopped));
    }

    public byte getCharacterCharToPlayInput() throws ScannerException {
        List<CharacterCardClient> characterCards = getModel().getCharacters();
        System.out.println("--OPTIONS--");
        for (int i = 0; i < characterCards.size(); i++)
            System.out.println(optionString(i, characterCards.get(i) + ": " + characterCards.get(i).getDescription()));
        return (byte) getIntInput(0, characterCards.size() - 1, "Select the character to play", false);
    }

    public byte getAssistantCardToPlayInput(boolean canBeStopped) throws ScannerException {
        boolean[] usedCards = getModel().getCurrentPlayer().getUsedCards();
        TreeSet<Integer> choices = new TreeSet<>();
        for (int i = 1; i <= usedCards.length; i++) {
            if (!usedCards[i - 1])
                choices.add(i);
        }
        System.out.println("--OPTIONS--");
        System.out.println(optionString(choices, "Assistant card"));

        return (byte) getIntInput(choices, "Select an assistant card to play", canBeStopped);
    }

    public int getMoveStudentDestination(boolean canBeStopped) throws ScannerException {
        List<GameComponentClient> validDestinations = new ArrayList<>();

        GameComponentClient lunchHall = getModel().getCurrentPlayer().getLunchHall();
        List<IslandClient> islands = getModel().getIslands();

        TreeSet<Integer> choices = new TreeSet<>();
        for (IslandClient island : islands)
            choices.add(islands.indexOf(island) + 1);

        System.out.println("--OPTIONS--");
        System.out.println("[0] " + lunchHall.getNameOfComponent());
        System.out.println(optionString(choices, islands.get(0).getNameOfComponent()));

        choices.add(0);

        validDestinations.add(lunchHall);
        validDestinations.addAll(islands);
        int input = getIntInput(choices, "Select a destination", canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return validDestinations.get(index).getId();
    }

    public int getIslandDestination(String message, boolean canBeStopped) throws ScannerException {
        List<IslandClient> islandClients = getModel().getIslands();
        System.out.println("--OPTIONS--");
        SortedSet<Integer> choices = new TreeSet<>();
        for (IslandClient i : islandClients)
            choices.add(islandClients.indexOf(i) + 1);
        System.out.println(optionString(choices, islandClients.get(0).getNameOfComponent()));
        int input = getIntInput(choices, message, canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return islandClients.get(index).getId();
    }

    public byte getMotherNatureMovesInput(boolean canBeStopped) throws ScannerException {
        return (byte) getIntInput(1, getModel().getCurrentPlayer().getPlayedCardMoves(), "How many steps do you want mother nature to move?", canBeStopped);
    }

    public int getCloudSource(boolean canBeStopped) throws ScannerException {
        List<GameComponentClient> clouds = getModel().getClouds();
        List<GameComponentClient> availableClouds = new ArrayList<>();
        SortedSet<Integer> choices = new TreeSet<>();
        System.out.println("--OPTIONS--");
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : clouds) {
            if (cloud.howManyStudents() > 0) {
                int cloudIndex = clouds.indexOf(cloud) + 1;
                availableClouds.add(cloud);
                choices.add(cloudIndex);
            }
        }
        System.out.println(optionString(choices, clouds.get(0).getNameOfComponent()));
        int input = getIntInput(choices, "Select a cloud source", canBeStopped);
        int index = new ArrayList<>(choices).indexOf(input);
        return availableClouds.get(index).getId();
    }

    public boolean getBooleanInput(String message, boolean canBeStopped) throws ScannerException {
        System.out.println(message + " (Y/N):");
        String s = getInput(canBeStopped).toLowerCase();
        while (!s.equals("y") && !s.equals("n")) {
            System.err.println("Not a valid input");
            System.out.println(message + " (Y/N):");
            s = getInput(canBeStopped).toLowerCase();
        }
        return s.equals("y");
    }

    public String getStringInput(String message, int maxLength, boolean canBeStopped) throws ScannerException {
        System.out.println(message + ": ");
        String s = getInput(canBeStopped);
        while (s.length() > maxLength) {
            System.err.println("Max length is " + maxLength);
            System.out.println(message + ": ");
            s = getInput(canBeStopped);
        }
        return s;
    }

    public long getLongInput(String message, boolean canBeStopped) throws ScannerException {
        long ret = 0;
        boolean err;
        do {
            err = false;
            System.out.println(message + ":");
            try {
                ret = Long.parseLong(getInput(canBeStopped));
            } catch (NumberFormatException ex) {
                err = true;
                System.err.println("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public synchronized String getInput(boolean canBeStopped) throws ScannerException {
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
            while (!isInputReady && ((!mustReprint && !phaseChanged) || !canBeStopped) && !forcedScannerSkip) {
                wait();
            }
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted");
            throw new SkipCommandException();
        }
        if (mustReprint) {
            // reprint game
            cliPrinter.printGame();
            // if one of these conditions is true it should not repeat the command, skip instead to the new one
            if (!phaseChanged && !isInputReady && !forcedScannerSkip)
                throw new RepeatCommandException();
        }
        // if input is ready, return it
        if (isInputReady) return this.input;
        // input not ready and all others conditions are not verified, skip to new command
        throw new SkipCommandException();
    }

    @Override
    public void setQuit(boolean forceScannerSkip) {
        super.setQuit(forceScannerSkip);
        // notifies scanner to wakeup and terminate
        notifyAll();
    }

    @Override
    public void setModel(GameClientView model) {
        super.setModel(model);
    }
}
