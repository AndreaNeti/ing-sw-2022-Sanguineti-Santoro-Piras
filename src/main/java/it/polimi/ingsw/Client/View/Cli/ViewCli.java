package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCli extends AbstractView implements ViewForCharacterCli {
    private ClientPhaseView phaseToExecute;
    private volatile boolean isInputReady, phaseChanged, requestInput, forcedScannerSkip;
    private String input;

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        Thread scannerThread = new Thread(() -> {
            final Scanner myInput = new Scanner(System.in);
            try {
                synchronized (this) {
                    while (!requestInput)
                        wait();
                }
                while (!canQuit()) {
                    do {
                        input = myInput.nextLine();
                    } while (input.isBlank());
                    synchronized (this) {
                        isInputReady = true;
                        requestInput = false;
                        notify();
                        while (!requestInput && !canQuit())
                            wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        scannerThread.start();
        System.out.println("You've chosen to play with client line interface");
        phaseToExecute = null;
        isInputReady = false;
        phaseChanged = false;
        forcedScannerSkip = false;
        requestInput = false;
    }

    public void start() throws InterruptedException {
        do {
            synchronized (this) {
                while (phaseToExecute == null)
                    wait();
                phaseToExecute.playPhase(this);
            }
        } while (!canQuit());
    }

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


    private String listOptions(Object[] options) {
        StringBuilder ret = new StringBuilder();
        ret.append("--OPTIONS--\n");
        for (int i = 0; i < options.length; i++)
            ret.append("[").append(i).append("] ").append(options[i]).append("\n");
        return ret.toString();
    }

    public int getIntInput(Object[] options, String message, boolean canBeStopped) throws PhaseChangedException {
        System.out.println(listOptions(options));
        return getIntInput(0, options.length - 1, message, canBeStopped);
    }

    public int getIntInput(int min, int max, String message, boolean canBeStopped) throws PhaseChangedException {
        int ret = getIntInput(message + " (from " + min + " to " + max + ")", canBeStopped);
        while (ret < min || ret > max) {
            System.err.println("Not a valid input (from " + min + " to " + max + ")");
            ret = getIntInput(message, canBeStopped);
        }
        return ret;
    }

    //     Message is the string printed before asking the input
    public int getIntInput(String message, boolean canBeStopped) throws PhaseChangedException {
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

    public byte[] getIpAddressInput(boolean canBeStopped) throws PhaseChangedException {
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

    public int getColorInput(boolean canBeStopped) throws PhaseChangedException {
        return getIntInput(Color.values(), "Choose a color", canBeStopped);
    }

    public MatchType getMatchTypeInput(boolean canBeStopped) throws PhaseChangedException {
        return new MatchType((byte) getIntInput(2, 4, "Choose the number of players", canBeStopped), getBooleanInput("Do you want to play in expert mode?", canBeStopped));
    }

    public byte getAssistantCardToPlayInput(boolean canBeStopped) throws PhaseChangedException {
        // TODO use getIntInput Options[] and a record for assistant cards
        boolean[] usedCards = getModel().getCurrentPlayer().getUsedCards();
        byte ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play", canBeStopped);
        while (usedCards[ret - 1]) {
            System.err.println("Card already played");
            ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play", canBeStopped);
        }
        return ret;
    }

    public int getMoveStudentDestination(boolean canBeStopped) throws PhaseChangedException {
        List<GameComponentClient> validDestinations = new ArrayList<>();
        validDestinations.add(getModel().getCurrentPlayer().getLunchHall());
        validDestinations.addAll(getModel().getIslands());
        int index = getIntInput(validDestinations.toArray(), "Select a destination", canBeStopped);
        return validDestinations.get(index).getId();
    }

    public int getIslandDestination(String message, boolean canBeStopped) throws PhaseChangedException {
        List<IslandClient> islandClients = getModel().getIslands();
        return islandClients.get(getIntInput(islandClients.toArray(), message, canBeStopped)).getId();
    }

    public byte getMotherNatureMovesInput(boolean canBeStopped) throws PhaseChangedException {
        return (byte) getIntInput(1, getModel().getCurrentPlayer().getPlayedCardMoves(), "How many steps do you want mother nature to move?", canBeStopped);
    }

    public int getCloudSource(boolean canBeStopped) throws PhaseChangedException {
        ArrayList<GameComponentClient> availableClouds = new ArrayList<>();
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : getModel().getClouds()) {
            if (cloud.howManyStudents() > 0)
                availableClouds.add(cloud);
        }
        int cloudIndex = getIntInput(availableClouds.toArray(), "Choose a cloud source", canBeStopped);
        return availableClouds.get(cloudIndex).getId();
    }

    public boolean getBooleanInput(String message, boolean canBeStopped) throws PhaseChangedException {
        System.out.println(message + " (Y/N):");
        String s = getInput(canBeStopped).toLowerCase();
        while (!s.equals("y") && !s.equals("n")) {
            System.err.println("Not a valid input");
            System.out.println(message + " (Y/N):");
            s = getInput(canBeStopped).toLowerCase();
        }
        return s.equals("y");
    }


    public String getStringInput(String message, int maxLength, boolean canBeStopped) throws PhaseChangedException {
        System.out.println(message + ": ");
        String s = getInput(canBeStopped);
        while (s.length() > maxLength) {
            System.err.println("Max length is " + maxLength);
            System.out.println(message + ": ");
            s = getInput(canBeStopped);
        }
        return s;
    }

    public long getLongInput(String message, boolean canBeStopped) throws PhaseChangedException {
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

    public synchronized String getInput(boolean canBeStopped) throws PhaseChangedException {
        isInputReady = false;
        phaseChanged = false;
        forcedScannerSkip = false;
        this.input = "";
        // wakes up scanner thread
        requestInput = true;
        notify();
        try {
            // wait for scanner notify or phase changed flag (if it can be stopped) or fored skip (used if someone lost connection)
            while (!isInputReady && (!phaseChanged || !canBeStopped) && !forcedScannerSkip) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (isInputReady) return this.input;

        throw new PhaseChangedException();
    }


    public void printEntranceHall(Integer player) {
        System.out.println(getModel().getPlayer(player).getEntranceHall());
    }

    @Override
    public void setQuit(boolean forceScannerSkip) {
        super.setQuit(forceScannerSkip);
        // notifies scanner to wakeup and terminate
        notify();
    }

    @Override
    public void setModel(GameClientView model) {
        super.setModel(model);
    }
}
