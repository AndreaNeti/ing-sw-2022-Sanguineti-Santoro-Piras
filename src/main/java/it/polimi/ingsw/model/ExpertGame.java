package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ExpertGame extends NormalGame {
    private final byte[] coinsPlayer;
    private transient final ArrayList<CharacterCard> characters;
    private final Set<Byte> charactersId;
    private transient final ArrayList<Integer> inputsCharacter;
    private final boolean[] playedCharacters;
    private byte coinsLeft;
    private transient boolean extraInfluence; //default false
    private transient boolean towerInfluence;// default true
    private transient boolean extraSteps; //default false
    private transient boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence;
    private byte prohibitionLeft;
    private byte chosenCharacter;


    public ExpertGame(byte numberOfPlayers, ArrayList<Team> teamList, ArrayList<Player> playerList) {
        super(numberOfPlayers, teamList, playerList);
        this.coinsLeft = (byte) (20 - numberOfPlayers);
        this.coinsPlayer = new byte[numberOfPlayers];
        for (byte i = 0; i < numberOfPlayers; i++)
            coinsPlayer[i] = 1;

        characters = new ArrayList<>(3);
        charactersId = new HashSet<>(3);
        Random rand = new Random(System.currentTimeMillis());
        byte characterIndex = (byte) rand.nextInt(12);
        byte i = 0;
        ArrayList<Byte> selectedCharacters = new ArrayList<>(3);
        CharacterCard c;
        while (i < 3) {
            while (selectedCharacters.contains(characterIndex)) {
                characterIndex = (byte) rand.nextInt(12);
            }
            try {
                c = factoryCharacter(characterIndex);
                characters.add(c);
                selectedCharacters.add(c.getId());
                i++;
            } catch (UnexpectedValueException e) {
                e.printStackTrace();
            }
        }

        playedCharacters = new boolean[]{false, false, false};
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.prohibitionLeft = 4;
        this.chosenCharacter = -1;
        this.inputsCharacter = new ArrayList<>();
    }

    private CharacterCard factoryCharacter(byte i) throws UnexpectedValueException {
        charactersId.add(i);
        switch (i) {
            case 0:
                Char0 c0 = new Char0();
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
                Char6 c6 = new Char6();
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
                Char10 c10 = new Char10();
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
            addCoinToPlayer(getCurrentPlayer());
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

                if (extraInfluence && getCurrentPlayer().getTeam().equals(t)) {
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
        this.extraInfluence = false;
        this.towerInfluence = true;
        this.extraSteps = false;
        this.equalProfessorCalculation = false;
        this.ignoredColorInfluence = null;
        this.chosenCharacter = -1;
        this.inputsCharacter.clear();
        super.setCurrentPlayer(p);
    }

    @Override
    protected boolean checkMoveMotherNature(int moves) {
        if (!extraSteps)
            return super.checkMoveMotherNature(moves);
        return moves <= getCurrentPlayer().getPlayedCardMoves() + 2;
    }

    protected void calculateProfessor() {
        byte max;
        Player currentOwner = null;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            // player actually controlling that professor
            if (getProfessor()[c.ordinal()] != null)
                currentOwner = getPlayers().get(getProfessor()[c.ordinal()].ordinal());

            if (currentOwner != null)
                max = currentOwner.getLunchHall().howManyStudents(c);
            else max = 0;
            newOwner = currentOwner;

            for (Player p : getPlayers()) {
                if (!p.equals(currentOwner)) {
                    if (p.getLunchHall().howManyStudents(c) > max) {
                        max = p.getLunchHall().howManyStudents(c);
                        newOwner = p;
                    } else if (equalProfessorCalculation && p.equals(getCurrentPlayer()) && p.getLunchHall().howManyStudents(c) == max)
                        newOwner = p;
                }
            }
            if (newOwner != null)
                getProfessor()[c.ordinal()] = newOwner.getWizard();
        }
    }

    private void addCoinToPlayer(Player player) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (player != null) {
            coinsPlayer[getPlayers().indexOf(player)]++;
            coinsLeft--;
        } else System.err.println("Can't add coin to null player");

    }

    private void removeCoinsToPlayer(Player player, byte coins) throws GameException {
        if (coins <= 0) throw new UnexpectedValueException();
        if (player != null) {
            // player's wizard is its index inside the list
            int playerIndex = getCurrentPlayer().getWizard().ordinal();
            if (coinsPlayer[playerIndex] >= coins) {
                coinsPlayer[playerIndex] -= coins;
                // add coins to player
                coinsLeft += coins;
            } else throw new NotEnoughCoinsException();
        } else System.err.println("Can't remove coins to null player");
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
            removeCoinsToPlayer(getCurrentPlayer(), charCost);

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
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

    protected void setEqualProfessorCalculation() {
        this.equalProfessorCalculation = true;
    }

    protected ArrayList<Integer> getCharacterInputs() {
        return new ArrayList<>(inputsCharacter);
    }

    protected void setProhibition(int idIsland) throws NotAllowedException {
        if (this.prohibitionLeft > 0)
            this.prohibitionLeft--;
        else throw new NotAllowedException("No more prohibitions");
        try {
            getIslands().get(idIsland).addProhibitions((byte) 1);
        } catch (UnexpectedValueException e) {
            // Passing 1 as argument, 1 > 0 -> no exceptions
            e.printStackTrace();
        }
    }

    private void restoreProhibition() {
        if (this.prohibitionLeft < 4) {
            this.prohibitionLeft++;
        }
    }

    public byte getCoinsPlayer(Player p) {
        return coinsPlayer[getPlayers().indexOf(p)];
        // return coinsPlayer[p.getWizard().ordinal()]
    }
}