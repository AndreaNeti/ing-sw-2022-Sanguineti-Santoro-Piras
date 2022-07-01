package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.CharacterServerLogic.*;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.Util.*;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughCoinsException;
import it.polimi.ingsw.network.ExpertGameDelta;
import it.polimi.ingsw.network.GameDelta;

import java.io.Serializable;
import java.util.*;

/**
 * ExpertGame class represents the second game type available on "Eriantys". <br>
 * Following the decorator pattern, it adds to the NormalGame logic the coins logic and also adds the character cards,
 * with all the effects they provide. Most function are therefore overridden to add checks
 * on coins and character card effects. <br>
 * Most functions add the updated info of the game to the Expert Game Delta, which is then sent to the
 * clients to inform them about the changes happening in the game.
 */
public class ExpertGame extends NormalGame implements GameInterfaceForCharacter, CoinListener, Serializable {
    private final int[] coinsPlayer;
    private final Set<CharacterCard> characters;
    private final List<Integer> inputsCharacter;
    private int coinsLeft;
    private boolean extraInfluence; //default false
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence; // default null
    private byte prohibitionsLeft; // default 4
    private CharacterCard chosenCharacter;
    private final MatchConstants matchConstants;


    /**
     * Constructor ExpertGame creates a new instance of ExpertGame. <br>
     * Info about the character card is read from a JSON file in the 'resources' folder.
     *
     * @param teamList       of type {@code ArrayList}<{@link Team}> - list of the instances of team that are playing in the game.
     * @param matchConstants of type {@link MatchConstants} - match constant of the game, based on its type.
     */
    public ExpertGame(ArrayList<Team> teamList, MatchConstants matchConstants) {
        super(teamList, matchConstants);
        this.matchConstants = matchConstants;
        byte numberOfPlayers = super.getPlayerSize();
        this.coinsLeft = matchConstants.totalCoins() - numberOfPlayers * matchConstants.initialPlayerCoins();
        this.coinsPlayer = new int[numberOfPlayers];
        Arrays.fill(coinsPlayer, matchConstants.initialPlayerCoins());

        characters = new HashSet<>(matchConstants.numOfCharacterCards());
        Random rand = new Random(System.currentTimeMillis());
        byte characterIndex = (byte) rand.nextInt(12);
        byte i = 0;
        ArrayList<Byte> selectedCharacters = new ArrayList<>(matchConstants.numOfCharacterCards());
        CharacterCard c;

        CharacterCardData[] characterConstants = (CharacterCardData[]) JsonReader.getObjFromJson("characterConstants", CharacterCardData[].class);

        while (i < matchConstants.numOfCharacterCards()) {
            while (selectedCharacters.contains(characterIndex)) {
                characterIndex = (byte) rand.nextInt(12);
            }
            c = factoryCharacter(characterIndex, this, characterConstants);
            characters.add(c);
            selectedCharacters.add(characterIndex);
            i++;
        }
        // add Coin Listener
        for (Player p : getPlayers()) {
            p.getLunchHall().addCoinListener(this);
        }
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.prohibitionsLeft = 4;
        this.chosenCharacter = null;
        this.inputsCharacter = new ArrayList<>();
    }

    /**
     * Method getNewGameDelta returns a new ExpertGameDelta for the game.
     *
     * @return {@link GameDelta} - new instance of the game's ExpertGameDelta.
     */
    @Override
    protected GameDelta getNewGameDelta() {
        return new ExpertGameDelta();
    }


    /**
     * Method factoryCharacter creates an instance of one of the 12 possible character cards, based on the index provided. <br>
     * Info about the cards, such as cost and ID, are read from the characterConstants array passed as parameter.
     *
     * @param index of type {@code byte} - index of the CharacterCard to instantiate.
     * @param game of type {@link GameInterfaceForCharacter} - instance of the game used to draw students on the character cards
     *             that contain students.
     * @param characterConstants of type {@link CharacterCardData}{@code []} - array of constants for each card.
     * @return {@link CharacterCard} - instance of the CharacterCard requested.
     */
    static CharacterCard factoryCharacter(int index, GameInterfaceForCharacter game, CharacterCardData[] characterConstants) {
        if (game == null || characterConstants == null)
            throw new IllegalArgumentException("Cannot pass null values");
        if (index >= characterConstants.length)
            throw new IllegalArgumentException("Cannot retrieve character constants");
        switch (index) {
            case 0:
                Char0 c0 = new Char0(characterConstants[index].getCharId());
                try {
                    game.drawStudents(c0, (byte) c0.getMaxStudents());
                } catch (EndGameException | GameException e) {
                    e.printStackTrace();
                }
                return new CharacterCard(c0, characterConstants[index]);
            case 1:
                return new CharacterCard(new Char1(), characterConstants[index]);
            case 2:
                return new CharacterCard(new Char2(), characterConstants[index]);
            case 3:
                return new CharacterCard(new Char3(), characterConstants[index]);
            case 4:
                return new CharacterCard(new Char4(), characterConstants[index]);
            case 5:
                return new CharacterCard(new Char5(), characterConstants[index]);
            case 6:
                Char6 c6 = new Char6(characterConstants[index].getCharId());
                try {
                    game.drawStudents(c6, (byte) c6.getMaxStudents());
                } catch (EndGameException | GameException e) {
                    e.printStackTrace();
                }
                return new CharacterCard(c6, characterConstants[index]);
            case 7:
                return new CharacterCard(new Char7(), characterConstants[index]);
            case 8:
                return new CharacterCard(new Char8(), characterConstants[index]);
            case 9:
                return new CharacterCard(new Char9(), characterConstants[index]);
            case 10:
                Char10 c10 = new Char10(characterConstants[index].getCharId());
                try {
                    game.drawStudents(c10, (byte) c10.getMaxStudents());
                } catch (EndGameException | GameException e) {
                    e.printStackTrace();
                }
                return new CharacterCard(c10, characterConstants[index]);
            case 11:
                return new CharacterCard(new Char11(), characterConstants[index]);
        }
        throw new IllegalArgumentException("Character card " + index + " doesn't exists");
    }

    /**
     * Method calculateInfluence sets the team with the highest influence as the controller of the selected island,
     * based on the number of students present for each controlled color and on the number of towers on the island.
     * Eventual booleans set by the character card can skip or modify the influence calculation.
     *
     * @param island of type {@link Island} - the island of which we want to calculate the new controller.
     * @throws EndGameException if a team has no towers left in its board.
     */
    @Override
    public void calculateInfluence(Island island) throws EndGameException {
        if (island == null) throw new IllegalArgumentException("Cannot calculate influence on null island");
        // if island has prohibitions don't calculate influence and remove one prohibition.
        if (island.getProhibitions() > 0) {
            island.removeProhibition();
            restoreProhibition();
            getGameDelta().addUpdatedGC(island);
        } else {
            HouseColor oldController = island.getTeamColor();
            int maxInfluence = 0;
            HouseColor winnerColor = null;
            for (Team t : getTeams()) {
                int influence = 0;
                for (Color c : Color.values()) {
                    if (c != ignoredColorInfluence) {
                        for (Player p : t.getPlayers()) {
                            if (p.getWizard() == getProfessor()[c.ordinal()]) influence += island.howManyStudents(c);
                        }
                    }
                }
                // tower influence
                if (oldController != null && towerInfluence && t.getHouseColor() == oldController)
                    influence += island.getArchipelagoSize();

                // extra influence
                if (extraInfluence && t.getPlayers().contains(getCurrentPlayer())) {
                    influence += 2;
                }

                if (influence > maxInfluence) {
                    winnerColor = t.getHouseColor();
                    maxInfluence = influence;
                } else if (influence == maxInfluence) {
                    winnerColor = oldController;
                }
            }
            setIslandController(island, winnerColor, oldController);
        }
    }

    /**
     * Method setCurrentPlayer updates the current player and resets ExpertGame's booleans.
     *
     * @param player {@link Player} - the instance of the new current player.
     */
    public void setCurrentPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Cannot set null current player");
        setCurrentPlayer((byte) player.getWizard().ordinal());
    }

    /**
     * Method setCurrentPlayer updates the current player based on his index and resets ExpertGame's booleans.
     *
     * @param currentPlayerIndex {@code byte} - the index of the new current player.
     */
    @Override
    public void setCurrentPlayer(byte currentPlayerIndex) {
        super.setCurrentPlayer(currentPlayerIndex);
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.chosenCharacter = null;
        this.inputsCharacter.clear();
        getGameDelta().setExtraSteps(extraSteps);
    }


    /**
     * Method transformAllGameInDelta saves all the game info inside the ExpertGameDelta that is sent to the client.
     *
     * @return {@link ExpertGameDelta} - ExpertGameDelta with all the info of the game.
     */
    @Override
    public GameDelta transformAllGameInDelta() {
        ExpertGameDelta g = (ExpertGameDelta) super.transformAllGameInDelta();
        g.setNewCoinsLeft(coinsLeft);
        g.setNewProhibitionsLeft(prohibitionsLeft);
        for (CharacterCard c : characters) {
            g.addCharacterCard(c);
        }
        getGameDelta().setExtraSteps(extraSteps);
        return g;

    }

    /**
     * Method checkMoveMotherNature checks if mother nature can move the requested number of steps, based
     * on the current player's played card and on the extraStep boolean set by the character cards.
     *
     * @param moves of type {@code int} - number of steps the player want to move mother nature.
     * @return {@code boolean} - true if moves <= moves allowed by played card (possibly increased by matchConstants.extraStep with the extraStep boolean), boolean false else.
     */
    @Override
    protected boolean checkMoveMotherNature(int moves) {
        if (!extraSteps) return super.checkMoveMotherNature(moves);
        if (moves < 0) throw new IllegalArgumentException("Cannot move backwards");
        return moves <= getCurrentPlayer().getPlayedCard().moves() + matchConstants.extraStep();
    }

    /**
     * Method calculateProfessor compares for each color the number of students in the lunch hall of each player
     * and then puts the wizard of the player with the most students in the professors array, in the slot of the color compared.
     * If the equalProfessorCalculation is set to true, in case of a tie the current player's wizard will be placed in the professor's array.
     */
    @Override
    public void calculateProfessor() {
        byte max;
        Player currentOwner;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            currentOwner = null;
            // player actually controlling that professor
            if (getProfessor()[c.ordinal()] != null)
                currentOwner = getPlayer((byte) getProfessor()[c.ordinal()].ordinal());

            if (currentOwner != null) max = currentOwner.getLunchHall().howManyStudents(c);
            else max = 0;
            newOwner = currentOwner;

            for (Player p : getPlayers()) {
                if (!p.equals(currentOwner)) {
                    if (p.getLunchHall().howManyStudents(c) > max) {
                        max = p.getLunchHall().howManyStudents(c);
                        newOwner = p;
                        // equalProfessorCalculation
                    } else if (equalProfessorCalculation && p.equals(getCurrentPlayer()) && p.getLunchHall().howManyStudents(c) == max && currentOwner != null)
                        newOwner = p;
                }
            }
            if (newOwner != null && !newOwner.equals(currentOwner)) {
                getProfessor()[c.ordinal()] = newOwner.getWizard();

                // add to game delta
                getGameDelta().addUpdatedProfessors(c, newOwner.getWizard());
            }
        }
    }

    /**
     * Method notifyCoins is used to add coins to the current player, removing or adding them from the game.
     *
     * @param coins of type {@code int} - number of coins to add to the current player.
     * @throws NotEnoughCoinsException if there are no more coins left in the game.
     */
    @Override
    public void notifyCoins(int coins) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (coinsLeft < coins) coins = coinsLeft;
        byte playerIndex = getCurrentPlayerIndex();
        coinsPlayer[playerIndex] += coins;
        coinsLeft -= coins;

        // add to game delta
        getGameDelta().setUpdatedCoinPlayer(playerIndex, getCoinsPlayer(playerIndex));
        getGameDelta().setNewCoinsLeft(coinsLeft);
    }

    /**
     * Method notifyCoins is used to remove coins from the current player, adding them from the game.
     *
     * @param coins of type {@code int} - number of coins to remove from the current player.
     * @throws NotEnoughCoinsException if the player has fewer coins than the amount to remove.
     */
    private void removeCoinsFromCurrentPlayer(int coins) throws NotEnoughCoinsException {
        if (coins < 0)
            throw new IllegalArgumentException("Cannot remove negative amount of coins to the current player");
        // player's wizard is its index inside the list
        byte playerIndex = getCurrentPlayerIndex();
        if (coinsPlayer[playerIndex] >= coins) {
            coinsPlayer[playerIndex] -= coins;
            // add coins to player
            coinsLeft += coins;

            // add to game delta
            getGameDelta().setUpdatedCoinPlayer(playerIndex, getCoinsPlayer(playerIndex));
            getGameDelta().setNewCoinsLeft(coinsLeft);
        } else throw new NotEnoughCoinsException();
    }

    /**
     * Method playCharacter is used to play the selected character card, applying its effects, removing coins
     * from the player and increasing its cost by 1 if it has never been played before.
     *
     * @throws GameException    if the inputs set by the player are invalid (wrong inputs or more/fewer than requested).
     * @throws EndGameException if the character card's effect triggers an endgame event (no more students in the bag,
     *                          no more towers in a team's board or less than 3 islands left)
     */
    @Override
    public void playCharacter() throws GameException, EndGameException {
        if (chosenCharacter == null) throw new NotAllowedException("No character card selected");
        if (getChosenCharacter().canPlay(inputsCharacter.size())) {
            try {
                try {
                    getChosenCharacter().play(this);
                } catch (GameException e) {
                    // something gone wrong while playing the card, reset inputs
                    inputsCharacter.clear();
                    throw e;
                }
                // remove coins to player
                removeCoinsFromCurrentPlayer(getChosenCharacter().getCost());
                // this character card is used for the first time
                if (!getChosenCharacter().isUsed()) {
                    getChosenCharacter().setUsed();
                    getGameDelta().addCharacterCard(getChosenCharacter());
                    // a coin is left on the character card to remember it has been used
                    coinsLeft--;
                    getGameDelta().setNewCoinsLeft(coinsLeft);
                }
                chosenCharacter = null;
                inputsCharacter.clear();
            } finally {
                getGameDelta().send();
            }
        } else {
            inputsCharacter.clear();
            throw new NotAllowedException("You didn't set all the parameters needed to play this card"); // canPlay returned false, needs a different amount of inputs
        }
    }

    /**
     * Method chooseCharacter is used to select one of the 3 available character cards based on their unique ID.
     *
     * @param charId of type {@code byte} - id of the character card chosen.
     * @throws GameException if the selected card is not available in the current game or the player doesn't have enough coins.
     */
    @Override
    public void chooseCharacter(Byte charId) throws GameException {
        chosenCharacter = null;
        if (charId != null) {
            inputsCharacter.clear();
            for (CharacterCard c : characters) {
                if (c.getCharId() == charId) {
                    chosenCharacter = c;
                    break;
                }
            }
            if (chosenCharacter == null) throw new NotAllowedException("Card not available");
            if (chosenCharacter.getCost() > coinsPlayer[getCurrentPlayer().getWizard().ordinal()]) {
                chosenCharacter = null;
                throw new NotAllowedException("You have not enough coins to play this card");
            }
        }

    }

    /**
     * Method setCharacterInputs is used by the player to add inputs to the character card.
     *
     * @param inputs of type {@code List}<{@code Integer}> - list of inputs for character chard.
     * @throws GameException if there is no character card selected.
     */
    @Override
    public void setCharacterInputs(List<Integer> inputs) throws GameException {
        if (chosenCharacter != null) inputsCharacter.addAll(inputs);
        else throw new NotAllowedException("There is no chosen character card");
    }

    /**
     * Method getComponentById gets a game component instance based on his unique ID.
     *
     * @param idGameComponent of type {@code int} - the id of the game component
     * @return {@link GameComponent} - the instance of the game component
     * @throws GameException if the id is not a valid one or corresponds to a merged island
     */
    @Override
    public GameComponent getComponentById(int idGameComponent) throws GameException {
        return super.getComponentById(idGameComponent);
    }


    /**
     * Method getChosenCharacter returns the character chard selected.
     *
     * @return {@link CharacterCard} - instance of the chosen character chard.
     */
    private CharacterCard getChosenCharacter() {
        return chosenCharacter;
    }

    /**
     * Method setExtraInfluence is called by the character card to set to true the respective boolean.
     */
    @Override
    public void setExtraInfluence() {
        this.extraInfluence = true;
    }

    /**
     * Method removeTowerInfluence is called by the character card to set to false the respective boolean.
     */
    @Override
    public void removeTowerInfluence() {
        this.towerInfluence = false;
    }

    /**
     * Method setExtraSteps is called by the character card to set to true the respective boolean.
     */
    @Override
    public void setExtraSteps() {
        this.extraSteps = true;
        getGameDelta().setExtraSteps(extraSteps);
    }

    /**
     * Method setIgnoredColorInfluence is called by the character card to set the color to ignore when calculating influence.
     *
     * @param ignoredColorInfluence of type {@link Color} - color of the students to ignore during the influence calculation.
     */
    @Override
    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        if (ignoredColorInfluence == null) throw new IllegalArgumentException("Null color");
        if (this.ignoredColorInfluence != ignoredColorInfluence) {
            this.ignoredColorInfluence = ignoredColorInfluence;
            // add to game delta
            getGameDelta().setIgnoredColorInfluence(ignoredColorInfluence);
        }
    }

    /**
     * Method setEqualProfessorCalculation is called by the character card to set to true the respective boolean.
     */
    @Override
    public void setEqualProfessorCalculation() {
        this.equalProfessorCalculation = true;
        calculateProfessor();
    }

    /**
     * Method getCharacterInputs returns the inputs added by the player for the chosen character card.
     *
     * @return {@code ArrayList}<{@code Integer}> - list of the inputs added by the player.
     */
    @Override
    public ArrayList<Integer> getCharacterInputs() {
        return new ArrayList<>(inputsCharacter);
    }


    /**
     * Method setProhibition is called by the character card to add a prohibition to a selected island.
     *
     * @param island of type {@link Island} - instance of the island to which the prohibition is added.
     * @throws NotAllowedException if there are no more prohibitions left in the game (maximum 4).
     */
    @Override
    public void setProhibition(Island island) throws NotAllowedException {
        if (island == null) throw new IllegalArgumentException("Cannot set prohibition to a null island");
        if (this.prohibitionsLeft <= 0) throw new NotAllowedException("No more prohibitions to set");
        this.prohibitionsLeft--;
        island.addProhibitions((byte) 1);

        // add to game delta
        getGameDelta().setNewProhibitionsLeft(prohibitionsLeft);
    }


    /**
     * Method restoreProhibition adds a prohibition on the game to be set, when an island with prohibition skips
     * the influence calculation.
     */
    private void restoreProhibition() {
        if (this.prohibitionsLeft < 4) {
            this.prohibitionsLeft++;

            // add to game delta
            getGameDelta().setNewProhibitionsLeft(prohibitionsLeft);
        }
    }

    /**
     * Method getCoinsPlayer returns the amount of coins owned by a selected player based on its index.
     *
     * @param playerIndex of type {@code byte} - index of the player.
     * @return {@code byte} - number of coins owned by the selected player.
     */
    protected int getCoinsPlayer(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= getPlayerSize())
            throw new IllegalArgumentException("Not a valid player index");
        return coinsPlayer[playerIndex];
    }

    /**
     * Method drawStudents draws students from the bag to the selected game component.
     *
     * @param gameComponent of type {@link GameComponent} - the game component on which we want to put the students.
     * @param students      of type {@code byte} - the number of students to draw.
     * @throws EndGameException if there are no more students available on the bag.
     * @throws GameException    if the game component selected is null.
     */
    @Override
    public void drawStudents(GameComponent gameComponent, byte students) throws EndGameException, GameException {
        super.drawStudents(gameComponent, students);
    }

    /**
     * Method getPlayerSize returns the number of players in the current game.
     *
     * @return {@code byte} - number of players.
     */
    @Override
    public byte getPlayerSize() {
        return super.getPlayerSize();
    }

    /**
     * Method getCurrentPlayer returns the current player.
     *
     * @return {@link Player} - instance of the current player.
     */
    @Override
    public Player getCurrentPlayer() {
        return super.getCurrentPlayer();
    }

}