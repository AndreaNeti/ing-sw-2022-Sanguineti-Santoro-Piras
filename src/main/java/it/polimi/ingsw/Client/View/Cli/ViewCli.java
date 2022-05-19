package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.io.IOException;
import java.util.*;

public class ViewCli extends AbstractView {

    private final static String operatingSystem = System.getProperty("os.name");
    private ClientPhaseView phaseToExecute;
    private Boolean isInputReady, phaseChanged;
    private String input;

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        Thread scannerThread = new Thread(() -> {
            final Scanner myInput = new Scanner(System.in);
            while (!canQuit()) {
                input = myInput.nextLine();
                notifyInput();
            }
        });
        scannerThread.start();
        System.out.println("You've chosen to play with client line interface");
        isInputReady = false;
        phaseChanged = false;
    }

    private synchronized void notifyInput() {
        isInputReady = true;
        notifyAll();
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

    public void unsetPhase() {
        this.phaseToExecute = null;
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
        if (towerLefts > 1)
            System.out.println("Team " + houseColor + "has now " + towerLefts + " towers");
        else
            System.out.println("Team " + houseColor + "has now " + towerLefts + " tower");
    }

    @Override
    public void update(Wizard[] professors) {
        for (int i = 0; i < professors.length; i++) {
            System.out.println(Color.values()[i] + " professor is owned by " + professors[i]);
        }
    }

    @Override
    public void update(String currentPlayer, boolean isMyTurn) {
        if (isMyTurn) System.out.println("It's your turn");
        else System.out.println(currentPlayer + " is now playing his turn");
    }

    @Override
    public void updateMembers(int membersLeftToStart) {
        if (membersLeftToStart > 0)
            System.out.println(membersLeftToStart + " members left before game starts");
        else System.out.println("Game is about to start");
    }

    @Override
    public void updateCardPlayed(Byte playedCard) {
        System.out.println(getModel().getCurrentPlayer().getWizard() + "played assistant card" + playedCard);
    }

    @Override
    public synchronized void setPhaseInView(ClientPhaseView clientPhaseView, boolean notifyScanner) {
        this.phaseToExecute = clientPhaseView;
        if (notifyScanner)
            phaseChanged = true;
        notifyAll();
    }

    @Override
    public void error(String e) {
        System.out.println("Operation failed because " + e);
    }

    @Override
    public void ok() {
        System.out.println("Successful operation");
    }

    public void clearConsole() {
        try {
            if (operatingSystem.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String listOptions(Object[] options) {
        StringBuilder ret = new StringBuilder();
        ret.append("--OPTIONS--\n");
        for (int i = 0; i < options.length; i++)
            ret.append("[").append(i).append("] ").append(options[i]).append("\n");
        return ret.toString();
    }

    public int getIntInput(Object[] options, String message) throws PhaseChangedException {
        System.out.println(listOptions(options));
        return getIntInput(0, options.length - 1, message);
    }

    public int getIntInput(int min, int max, String message) throws PhaseChangedException {
        int ret = getIntInput(message + " (from " + min + " to " + max + ")");
        while (ret < min || ret > max) {
            ret = getIntInput("Not a valid input (from " + min + " to " + max + ")\n" + message);
        }
        return ret;
    }

    //     Message is the string printed before asking the input
    public int getIntInput(String message) throws PhaseChangedException {
        int ret = 0;
        boolean err;
        do {
            err = false;
            System.out.println(message + ":");
            try {
                ret = Integer.parseInt(getInput());
            } catch (NumberFormatException ex) {
                err = true;
                print("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public int getServerPortInput() throws PhaseChangedException {
        return getIntInput(0, 65535, "Select server port");
    }

    public byte[] getIpAddressInput() throws PhaseChangedException {
        String input = getStringInput("Select server IP");
        byte[] ret = getIpFromString(input);
        while (ret == null) {
            input = getStringInput("Select a valid server IP");
            ret = getIpFromString(input);
        }
        return ret;
    }

    // return null if it's not a valid ip, otherwise returns the IP bytes
    private byte[] getIpFromString(String ip) {
        if (ip.equals("localhost")) return new byte[]{127, 0, 0, 1};
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

    public int getColorInput() throws PhaseChangedException {
        return getIntInput(Color.values(), "Choose a color");
    }

    public MatchType getMatchTypeInput() throws PhaseChangedException {
        return new MatchType((byte) getIntInput(2, 4, "Choose the number of players"), getBooleanInput("Do you want to play in expert mode?"));
    }

    public byte getAssistantCardToPlayInput() throws PhaseChangedException {
        // TODO use getIntInput Options[] and a record for assistant cards
        boolean[] usedCards = getModel().getCurrentPlayer().getUsedCards();
        byte ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        while (usedCards[ret - 1]) {
            System.out.println("Card already played");
            ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        }
        return ret;
    }

    public int getMoveStudentDestination() throws PhaseChangedException {
        List<GameComponentClient> validDestinations = new ArrayList<>();
        validDestinations.add(getModel().getCurrentPlayer().getLunchHall());
        validDestinations.addAll(getModel().getIslands());
        int index = getIntInput(validDestinations.toArray(), "Select a destination");
        return validDestinations.get(index).getId();
    }

    public byte getMotherNatureMovesInput() throws PhaseChangedException {
        return (byte) getIntInput(1, getModel().getCurrentPlayer().getPlayedCardMoves(), "How many steps do you want mother nature to move?");
    }

    public int getCloudSource() throws PhaseChangedException {
        ArrayList<GameComponentClient> availableClouds = new ArrayList<>();
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : getModel().getClouds()) {
            if (cloud.howManyStudents() > 0)
                availableClouds.add(cloud);
        }
        int cloudIndex = getIntInput(availableClouds.toArray(), "Choose a cloud source");
        return availableClouds.get(cloudIndex).getId();
    }

    public boolean getBooleanInput(String message) throws PhaseChangedException {
        System.out.println(message + "(Y/N):");
        String s = getInput().toLowerCase();
        while (!s.equals("y") && !s.equals("n")) {
            print("Not a valid input\n" + message + "(Y/N):");
            s = getInput().toLowerCase();
        }
        return s.equals("y");
    }


    public String getStringInput(String message) throws PhaseChangedException {
        System.out.println(message + ": ");
        String ret = getInput();

        while (ret.isBlank()) {
            System.out.println("Input can't be empty.\n" + message + ": ");
            ret = getInput();
        }

        return ret;
    }

    public void print(String s) {
        System.out.println(s);
    }

    public long getLongInput(String message) throws PhaseChangedException {
        long ret = 0;
        boolean err;
        do {
            err = false;
            System.out.println(message + ":");
            try {
                ret = Long.parseLong(getInput());
            } catch (NumberFormatException ex) {
                err = true;
                print("Not a valid input");
            }
        } while (err);
        return ret;
    }

    public synchronized String getInput() throws PhaseChangedException {
        isInputReady = false;
        phaseChanged = false;
        this.input = null;
        try {
            while (!isInputReady && !phaseChanged) {
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
        System.out.println(getModel().getPlayers().get(player).getEntranceHall());

    }
}
