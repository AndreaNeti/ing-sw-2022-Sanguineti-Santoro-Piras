package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Server.controller.MatchType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ViewCli extends AbstractView {

    private final static String operatingSystem = System.getProperty("os.name");
    private ClientPhaseView phaseToExecute;

    private String inputMessage;
    private Boolean ready, phaseChanged, waitingScanner;

    private Integer optionIndex = 0;
    private Object[] options;

    private Object lock;

    public ViewCli(ControllerClient controllerClient) {
        super(controllerClient);
        Thread t = new Thread(() -> {
            BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
            int a;
//            Reader r = new Reader() {
//                @Override
//                public int read(char[] cbuf, int off, int len) throws IOException {
//                    return 0;
//                }
//
//                @Override
//                public void close() throws IOException {
//
//                }
//            };

            while (true) {
                while (waitingScanner) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    a = inp.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (a == '1' || a == '2') {
                    clearConsole();
                    System.out.println(listOptions());
                    if (a == '1')
                        setIndex(true);
                    else
                        setIndex(false);
                    System.out.println(optionIndex);
                } else if (a == 10) {
                    System.out.println("Ciao");
                    ready = true;
                    notifyInput();
                }
            }
        });
        t.start();
        System.out.println("You've chosen to play with client line interface");
    }

    private synchronized void notifyInput() {
        ready = true;
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

    private void setIndex(boolean moveDown) {
        optionIndex += (moveDown) ? 1 : -1;
        optionIndex = Math.floorMod(optionIndex, options.length);
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

    }

    @Override
    public void update(Wizard[] professors) {

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
    }

    @Override
    public synchronized void setPhaseInView(ClientPhaseView clientPhaseView) {
        this.phaseToExecute = clientPhaseView;
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

//    public int getIntInput(Object[] options, String message) {
//        System.out.println(listOptions());
//        return getIntInput(0, options.length - 1, message);
//    }

    public int getIntInput(int min, int max, String message) {
        int ret = getIntInput(message + " (from " + min + " to " + max + ")");
        while (ret < min || ret > max) {
            ret = getIntInput("Not a valid input (from " + min + " to " + max + ")\n" + message);
        }
        return ret;
    }

    //     Message is the string printed before asking the input
    public int getIntInput(String message) {
        System.out.println(message + ":");

        final Scanner myInput = new Scanner(System.in);
        int ret = myInput.nextInt();
        myInput.close();
        return ret;
    }

    private String listOptions() {
        StringBuilder ret = new StringBuilder();
        ret.append(inputMessage).append("--OPTIONS--\n");
        for (int i = 0; i < options.length; i++)
            ret.append("[").append(i).append("] ").append(options[i]).append("\n");
        return ret.toString();
    }

    public int getServerPortInput() {
        return getIntInput(0, 65535, "Select server port");
    }

    public byte[] getIpAddressInput() {
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

    public int getColorInput() {
        return playOptions(Color.values(), "Choose a color");
    }

    public MatchType getMatchTypeInput() {
        return new MatchType((byte) getIntInput(2, 4, "Choose the number of players"), getBooleanInput("Do you want to play in expert mode?"));
    }

    public byte getAssistantCardToPlayInput() {
        // TODO use getIntInput Options[] and a record for assistant cards
        boolean[] usedCards = getModel().getCurrentPlayer().getUsedCards();
        byte ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        while (usedCards[ret]) {
            System.out.println("Card already played");
            ret = (byte) getIntInput(1, usedCards.length, "Choose an assistant card to play");
        }
        return ret;
    }

    public int getMoveStudentDestination() {
        List<GameComponentClient> validDestinations = new ArrayList<>();
        validDestinations.add(getModel().getCurrentPlayer().getLunchHall());
        validDestinations.addAll(getModel().getIslands());
        int index = playOptions(validDestinations.toArray(), "Select a destination");
//        int index = getIntInput(validDestinations.toArray(), "Select a destination");
        return validDestinations.get(index).getId();
    }

    public byte getMotherNatureMovesInput() {
        return (byte) getIntInput(1, getModel().getCurrentPlayer().getPlayedCardMoves(), "How many steps do you want mother nature to move?");
    }

    public int getCloudSource() {
        ArrayList<GameComponentClient> availableClouds = new ArrayList<>();
        // Add and prints only the not-empty clouds
        for (GameComponentClient cloud : getModel().getClouds()) {
            if (cloud.howManyStudents() > 0)
                availableClouds.add(cloud);
        }
        int cloudIndex = playOptions(availableClouds.toArray(), "Choose a cloud source");
        return availableClouds.get(cloudIndex).getId();
    }

    public boolean getBooleanInput(String message) {
        Object[] trueFalse = new Object[]{"Yes", "No"};
        return playOptions(trueFalse, message) == 0;
//        System.out.println(message + "(TRUE/FALSE):");
//        return myInput.nextBoolean();
    }


    public String getStringInput(String message) {
        waitingScanner=true;
        final Scanner myInput = new Scanner(System.in);
        System.out.println(message + ": ");
        String ret = myInput.next();

        while (ret.isBlank()) {
            System.out.println("Input can't be empty.\n" + message + ": ");
            ret = myInput.next();
        }
        myInput.close();
        lock.notify();

        return ret;
    }

    public void print(String s) {
        System.out.println(s);
    }

    public Long getLongInput(String message) {
        System.out.println(message + ":");

        final Scanner myInput = new Scanner(System.in);
        long ret = myInput.nextLong();
        myInput.close();
        return ret;
    }

    public synchronized int playOptions(Object[] options, String message) {
        ready = false;
        this.options = options;
        optionIndex = 0;
        inputMessage = message;
        listOptions();
        try {
            while (!ready || !phaseChanged) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ready) return optionIndex;
        phaseChanged = false;
        return -1;
    }


}
