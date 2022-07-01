package it.polimi.ingsw.client.View.Cli;

import it.polimi.ingsw.client.Controller.ControllerClient;
import it.polimi.ingsw.client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.client.View.AbstractView;
import it.polimi.ingsw.client.model.CharacterCardClient;
import it.polimi.ingsw.client.model.GameClientView;
import it.polimi.ingsw.client.model.GameComponentClient;
import it.polimi.ingsw.client.model.IslandClient;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.*;

/**
 * ViewCLI class represents the CLI view that allows the user to interact with the client. <br>
 * It contains methods to visualize the game and to requests inputs from the user.
 */
public class ViewCli extends AbstractView implements ViewForCharacterCli {
    private ClientPhase phaseToExecute;
    private volatile boolean isInputReady, phaseChanged, requestInput, forcedScannerSkip, mustReprint;
    private String input;
    private final CliPrinter cliPrinter;

    /**
     * Constructor ViewCli creates a new instance of ViewCli.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client's controller.
     */
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
        setPhaseInView(GamePhase.INIT_PHASE,false);
    }

    /**
     * Method start is used by the view's thread to print the CLI game when the client needs to execute a new phase. <br>
     * This method will run until the client quits the game.
     *
     * @throws InterruptedException if the thread is interrupted unexpectedly.
     */
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
    protected synchronized void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution) {
        phaseToExecute = clientPhase;
        phaseChanged = true;
        if (forceImmediateExecution) forcedScannerSkip = true;
        notifyAll();
    }

    /**
     * Method unsetPhase resets the client's phase to null.
     */
    public void unsetPhase() {
        phaseToExecute = null;
    }

    /**
     * Method setMustReprint specifies if the view must reprint the game and in case wakes all sleeping threads.
     *
     * @param reprint of type{@code boolean} - boolean that specifies if the view must reprint the game.
     */
    protected synchronized void setMustReprint(boolean reprint) {
        this.mustReprint = reprint;
        if (reprint) notifyAll();
    }

    /**
     * Method optionString creates a string with an option available and the number associated with it.
     *
     * @param value of type {@code int} - number associated with the option.
     * @param option of type {@code String} - the text string of the option.
     * @return {@code String} - "[n] option ".
     */
    private String optionString(int value, String option) {
        return "[" + value + "] " + option + " ";
    }

    /**
     * Method optionString creates a string with the list of options available and the number associated with each.
     *
     * @param values of type {@code SortedSet}<{@code Integer}> - set of numbers associated with each option.
     * @param option of type {@code String} - text string of the options.
     * @return {@code String} - <br>"[n1] option<br>  [n2] option...".
     */
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

    /**
     * Method getIntInput parses the int input from the {@link #getIntInput(String, boolean)} method and checks if it is contained in the
     * option values allowed.
     *
     * @param optionValues of type {@code Set}<{@code Integer}> - set of allowed numbers associated with each option.
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code int} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getIntInput lists all the available options and parses the int input from
     * the {@link #getIntInput(int, int, String, boolean)} method.
     *
     * @param options of type {@code Object[]} - array of options available.
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code int} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    public int getIntInput(Object[] options, String message, boolean canBeStopped) throws SkipCommandException {
        StringBuilder s = new StringBuilder("--OPTIONS--\n");
        for (byte i = 0; i < options.length; i++)
            s.append(optionString(i, options[i].toString())).append("\n");
        s.append(message);
        return getIntInput(0, options.length - 1, s.toString(), canBeStopped);
    }

    /**
     * Method getIntInput lists the range of available input values and parses the int input from the {@link #getIntInput(String, boolean)} method.
     *
     * @param min of type {@code int} - minimum input value allowed.
     * @param max of type {@code int} - maximum input value allowed.
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code int} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getIntInput requests a single input and parses it from the {@link #getInput} method.
     *
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code int} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getIpAddressInput requests an IP address and parses it from the {@link #getStringInput(String, int, boolean)} method.
     *
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code byte[]} - array of bytes equivalent to the IP address obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getColorInput requests a student color and parses it from the {@link #getIntInput(int, int, String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@link Color} - student color obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    @Override
    public Color getColorInput(boolean canBeStopped) throws SkipCommandException {
        StringBuilder message = new StringBuilder("--COLORS--\n");
        for (Color c : Color.values())
            message.append(optionString(c.ordinal() + 1, c.toString()));
        message.append("\nSelect a color");
        return Color.values()[getIntInput(1, Color.values().length, message.toString(), canBeStopped) - 1];
    }

    /**
     * Method getMatchTypeInput returns the match type, parsing its number of players from the {@link #getIntInput(int, int, String, boolean)} method and the
     * type of game (normal/expert) from the {@link #getBooleanInput(String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@link MatchType} - match type obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    public MatchType getMatchTypeInput(boolean canBeStopped) throws SkipCommandException {
        return new MatchType((byte) getIntInput(2, 4, "Select the number of players", canBeStopped), getBooleanInput("Do you want to play in expert mode?", canBeStopped));
    }

    /**
     * Method getCharacterCharToPlayInput requests a character card to play and parses its index from the {@link #getIntInput(int, int, String, boolean)} method.
     *
     * @return {@code byte} - index of the character card obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    public byte getCharacterCharToPlayInput() throws SkipCommandException {
        List<CharacterCardClient> characterCards = getModel().getCharacters();
        StringBuilder message = new StringBuilder("--CHARACTERS--\n");
        for (int i = 0; i < characterCards.size(); i++)
            message.append(optionString(i + 1, characterCards.get(i) + ": " + characterCards.get(i).getDescription())).append("\n");
        message.append("Select the character to play");
        return (byte) (getIntInput(1, characterCards.size(), message.toString(), false) - 1);
    }

    /**
     * Method getAssistantCardToPlayInput requests an assistant card to play and parses it from the {@link #getIntInput(Set, String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@link AssistantCard} - assistant card obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getMoveStudentDestination requests a game component of destination to move a student to it, 
     * parsing it from the {@link #getIntInput(Set, String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@link GameComponentClient} - game component of destination obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getIslandDestination requests an island of destination and parses it from the {@link #getIntInput(Set, String, boolean)} method.
     *
     * @param message of type {@code String} - message that will ask the user to input data.
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@code int} - ID of the island of destination inputted by the user.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getMotherNatureMovesInput requests the number of mother nature moves and parses it from 
     * the {@link #getIntInput(int, int, String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@code byte} - number of mother nature moves obtained from the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    public byte getMotherNatureMovesInput(boolean canBeStopped) throws SkipCommandException {
        byte maxMoves = getModel().getCurrentPlayer().getPlayedCard().moves();
        if (getModel().isExtraSteps()) maxMoves += getModel().getMatchConstants().extraStep();
        return (byte) getIntInput(1, maxMoves, "How many steps do you want mother nature to move?", canBeStopped);
    }

    /**
     * Method getCloudSource requests a cloud to take the students from, parsing it from
     * the {@link #getIntInput(Set, String, boolean)} method.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@code int} - ID of the cloud inputted by the user.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getBooleanInput parses the boolean input (Y/N) from the {@link #getInput} method.
     *
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code boolean} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getStringInput parses the string input with a maximum length provided from the {@link #getInput} method.
     *
     * @param message of type {@code String} - text string of the message to print.
     * @param maxLength of type {@code int} - maximum input string length.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code String} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getLongInput parses the long input from the {@link #getInput} method.
     *
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code long} - user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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

    /**
     * Method getInput waits for the scanner to notify, for a change of phase, for a forced skip (connection lost of another
     * player) or if there have been some changes and the game needs to be reprinted. <br>
     * When one of these events occur is checks if the game must be reprinted instantly or on the next phase change. <br>
     * Finally, it checks if the input must be repeated (input not ready or the scanner was forced to skip it), returned or skipped.
     *
     * @param message of type {@code String} - text string of the message to print.
     * @param canBeStopped of type {@code boolean} - boolean to check if the method can be stopped before user inputs all the
     *                                               required info.
     * @return {@code String} - text string of the user's input.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
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
