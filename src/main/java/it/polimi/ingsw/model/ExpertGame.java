package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.ExpertGameDelta;
import it.polimi.ingsw.controller.GameDelta;
import it.polimi.ingsw.controller.MatchConstants;
import it.polimi.ingsw.exceptions.*;

import java.util.*;

public class ExpertGame extends NormalGame {
    private final byte[] coinsPlayer;
    private transient final ArrayList<CharacterCard> characters;
    private transient final ArrayList<Integer> inputsCharacter;
    private final boolean[] playedCharacters;
    private byte coinsLeft;
    private transient boolean extraInfluence; //default false
    private transient boolean towerInfluence;// default true
    private transient boolean extraSteps; //default false
    private transient boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence;
    private byte prohibitionLeft;
    private transient byte chosenCharacter;


    public ExpertGame(ArrayList<Team> teamList, MatchConstants matchConstants) {
        super(teamList, matchConstants);
        byte numberOfPlayers = super.getPlayerSize();
        this.coinsLeft = (byte) (matchConstants.totalCoins() - numberOfPlayers * matchConstants.initialPlayerCoins());
        this.coinsPlayer = new byte[numberOfPlayers];
        Arrays.fill(coinsPlayer, (byte) matchConstants.initialPlayerCoins());

        characters = new ArrayList<>(matchConstants.numOfCharacterCards());
        Random rand = new Random(System.currentTimeMillis());
        byte characterIndex = (byte) rand.nextInt(12);
        byte i = 0;
        ArrayList<Byte> selectedCharacters = new ArrayList<>(matchConstants.numOfCharacterCards());
        CharacterCard c;
        while (i < matchConstants.numOfCharacterCards()) {
            while (selectedCharacters.contains(characterIndex)) {
                characterIndex = (byte) rand.nextInt(12);
            }
            try {
                c = factoryCharacter(characterIndex);
                getGameDelta().addCharacter(i, characterIndex);
                characters.add(c);
                selectedCharacters.add(c.getCharId());
                i++;
            } catch (UnexpectedValueException e) {
                e.printStackTrace();
            }
        }

        playedCharacters = new boolean[matchConstants.numOfCharacterCards()];
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.prohibitionLeft = 4;
        this.chosenCharacter = -1;
        this.inputsCharacter = new ArrayList<>();
    }

    @Override
    protected GameDelta getNewGameDelta() {
        return new ExpertGameDelta();
    }


    private CharacterCard factoryCharacter(byte i) throws UnexpectedValueException {
        switch (i) {
            case 0:
                Char0 c0 = new Char0((byte) -10);
                try {
                    drawStudents(c0, (byte) c0.getMaxStudents());
                } catch (EndGameException e) {
                    e.printStackTrace();
                }
                return c0;
            case 1:
                return new Char1();
            case 2:
                return new Char2();
            case 3:
                return new Char3();
            case 4:
                return new Char4();
            case 5:
                return new Char5();
            case 6:
                Char6 c6 = new Char6((byte) -11);
                try {
                    drawStudents(c6, (byte) c6.getMaxStudents());
                } catch (EndGameException e) {
                    e.printStackTrace();
                }
                return c6;
            case 7:
                return new Char7();
            case 8:
                return new Char8();
            case 9:
                return new Char9();
            case 10:
                Char10 c10 = new Char10((byte) -12);
                try {
                    drawStudents(c10, (byte) c10.getMaxStudents());
                } catch (EndGameException e) {
                    e.printStackTrace();
                }
                return c10;
            case 11:
                return new Char11();
        }
        throw new UnexpectedValueException();
    }

    @Override
    public void move(Color color, int gameComponentSource, int gameComponentDestination) throws GameException {
        super.move(color, gameComponentSource, gameComponentDestination);
        if (gameComponentDestination == 1 && getCurrentPlayer().getLunchHall().howManyStudents(color) % 3 == 0) {
            addCoinToCurrentPlayer();
        }
    }

    //calculate expertInfluence(it checks all the boolean) and then calls checkMerge

    protected void calculateInfluence(Island island) throws EndGameException {
        //prohibition is handled by prohibitionsLeft
        if (island.getProhibitions() > 0) {
            island.removeProhibition();
            restoreProhibition();
        } else {
            HouseColor oldController = island.getTeamColor();
            int maxInfluence = 0;
            HouseColor winnerColor = null;
            for (Team t : getTeams()) {
                int influence = 0;
                for (Color c : Color.values()) {
                    if (c != ignoredColorInfluence) {
                        for (Player p : t.getPlayers()) {
                            if (p.getWizard() == getProfessor()[c.ordinal()])
                                influence += island.howManyStudents(c);
                        }
                    }
                }
                if (island.getTeamColor() != null && towerInfluence && t.getHouseColor() == island.getTeamColor())
                    influence += island.getNumber();

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
            HouseColor oldTeamColor = island.getTeamColor();
            if (winnerColor != null && !winnerColor.equals(oldTeamColor)) {
                island.setTeamColor(winnerColor);
                if (oldTeamColor != null) {
                    try {
                        getTeams().get(oldTeamColor.ordinal()).addTowers(island.getNumber());
                    } catch (NotAllowedException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
                getTeams().get(winnerColor.ordinal()).removeTowers(island.getNumber());
                checkMerge(island);
            }
        }
    }

    @Override
    public void setCurrentPlayer(Player p) {
        setCurrentPlayer((byte) p.getWizard().ordinal());
    }

    @Override
    public void setCurrentPlayer(byte currentPlayerIndex) {
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.chosenCharacter = -1;
        this.inputsCharacter.clear();
        super.setCurrentPlayer(currentPlayerIndex);
    }

    @Override
    public GameDelta transformAllGameInDelta() {
        ExpertGameDelta g = (ExpertGameDelta) super.transformAllGameInDelta();
        g.setAutomaticSending(false);
        g.setNewCoinsLeft(coinsLeft);
        for (byte i = 0; i < characters.size(); i++) {
            g.addCharacter(i, characters.get(i).getCharId());
        }
        return g;

    }

    @Override
    protected boolean checkMoveMotherNature(int moves) {
        if (!extraSteps)
            return super.checkMoveMotherNature(moves);
        return moves <= getCurrentPlayer().getPlayedCardMoves() + 2;
    }

    protected void calculateProfessor() {
        byte max;
        Player currentOwner;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            currentOwner = null;
            // player actually controlling that professor
            if (getProfessor()[c.ordinal()] != null)
                currentOwner = getPlayer((byte) getProfessor()[c.ordinal()].ordinal());

            if (currentOwner != null)
                max = currentOwner.getLunchHall().howManyStudents(c);
            else max = 0;
            newOwner = currentOwner;

            for (Player p : getPlayers()) {
                if (!p.equals(currentOwner)) {
                    if (p.getLunchHall().howManyStudents(c) > max) {
                        max = p.getLunchHall().howManyStudents(c);
                        newOwner = p;
                    } else if (equalProfessorCalculation && p.equals(getCurrentPlayer()) && p.getLunchHall().howManyStudents(c) == max && currentOwner != null)
                        newOwner = p;
                }
            }
            if (newOwner != null)
                getProfessor()[c.ordinal()] = newOwner.getWizard();
        }
    }

    private void addCoinToCurrentPlayer() throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        byte playerIndex = getCurrentPlayerIndex();
        coinsPlayer[playerIndex]++;
        coinsLeft--;

        // add to game delta
        getGameDelta().setUpdatedCoinPlayer(playerIndex, getCoinsPlayer(playerIndex));
        getGameDelta().setNewCoinsLeft(coinsLeft);

    }

    private void removeCoinsToCurrentPlayer(byte coins) throws GameException {
        if (coins <= 0) throw new UnexpectedValueException();
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

    @Override
    public void playCharacter() throws GameException, EndGameException {
        if (chosenCharacter == -1) throw new NotAllowedException("No character card selected");
        if (getChosenCharacter().canPlay(inputsCharacter.size())) {
            try {
                getChosenCharacter().play(this);
            } catch (GameException e) {
                // something gone wrong while playing the card, reset inputs
                inputsCharacter.clear();
                throw e;
            }
            byte charCost = getChosenCharacter().getCost();
            // this character card has already been used, increase its cost
            if (playedCharacters[characters.indexOf(getChosenCharacter())]) charCost++;
            else {
                playedCharacters[characters.indexOf(getChosenCharacter())] = true;
                // a coin is left on the character card to remember it has been used
                coinsLeft--;
            }
            // remove coins to player
            removeCoinsToCurrentPlayer(charCost);

            chosenCharacter = -1;
            inputsCharacter.clear();
        } else {
            inputsCharacter.clear();
            throw new UnexpectedValueException(); // canPlay returned false, needs a different amount of inputs
        }
    }

    @Override
    public void chooseCharacter(byte indexCharacter) throws GameException {
        if (indexCharacter < 0 || indexCharacter >= characters.size()) throw new UnexpectedValueException();
        CharacterCard characterToPlay = characters.get(indexCharacter);
        byte charCost = characterToPlay.getCost();
        // this character card has already been used, increase its cost
        if (playedCharacters[indexCharacter]) charCost++;
        if (charCost > coinsPlayer[getCurrentPlayer().getWizard().ordinal()])
            throw new NotAllowedException("You have not enough coins to play this card");

        chosenCharacter = indexCharacter;
    }

    @Override
    public void setCharacterInput(int input) throws GameException {
        if (chosenCharacter != -1)
            inputsCharacter.add(input);
        else throw new NotAllowedException("There is no played character card");
    }

    private CharacterCard getChosenCharacter() {
        return characters.get(chosenCharacter);
    }

    protected void setExtraInfluence() {
        this.extraInfluence = true;
    }

    protected void removeTowerInfluence() {
        this.towerInfluence = false;
    }

    protected void setExtraSteps() {
        this.extraSteps = true;
    }

    protected void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        if (this.ignoredColorInfluence != ignoredColorInfluence) {
            this.ignoredColorInfluence = ignoredColorInfluence;

            // add to game delta
            getGameDelta().setIgnoredColorInfluence(ignoredColorInfluence);
        }
    }

    protected void setEqualProfessorCalculation() {
        this.equalProfessorCalculation = true;
    }

    protected ArrayList<Integer> getCharacterInputs() {
        return new ArrayList<>(inputsCharacter);
    }

    protected void setProhibition(int idIsland) throws NotAllowedException {
        if (this.prohibitionLeft > 0) {
            this.prohibitionLeft--;

            // add to game delta
            getGameDelta().setNewProhibitionsLeft(prohibitionLeft);
        } else throw new NotAllowedException("No more prohibitions");
        try {
            getIslands().get(idIsland).addProhibitions((byte) 1);
        } catch (UnexpectedValueException ignored) {
        }
    }

    private void restoreProhibition() {
        if (this.prohibitionLeft < 4) {
            this.prohibitionLeft++;

            // add to game delta
            getGameDelta().setNewProhibitionsLeft(prohibitionLeft);
        }
    }

    protected byte getCoinsPlayer(Player p) {
        return coinsPlayer[p.getWizard().ordinal()];
    }

    protected byte getCoinsPlayer(byte playerIndex) {
        return coinsPlayer[playerIndex];
    }
}