package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.Random;

public class ExpertGame extends NormalGame {
    private final byte[] coinsPlayer;
    private final ArrayList<CharacterCard> characters;
    private final ArrayList<Integer> inputsCharacter;
    private final boolean[] playedCharacters;
    private byte coinsLeft;
    private boolean extraInfluence; //default false
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence;
    private byte prohibitionLeft;
    private CharacterCard chosenCharacter;


    public ExpertGame(byte numberOfPlayers, ArrayList<Team> teamList, ArrayList<Player> playerList) {
        super(numberOfPlayers, teamList, playerList);
        this.coinsLeft = (byte) (20 - numberOfPlayers);
        this.coinsPlayer = new byte[numberOfPlayers];
        for (byte i = 0; i < numberOfPlayers; i++)
            coinsPlayer[i] = 1;

        characters = new ArrayList<>(3);
        Random rand = new Random(System.currentTimeMillis());
        int characterIndex = rand.nextInt(12);
        byte i = 0;
        ArrayList<Integer> selectedCharacters = new ArrayList<>(3);
        CharacterCard c;
        while (i < 3) {
            while (selectedCharacters.contains(characterIndex)) {
                characterIndex = rand.nextInt(12);
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
        this.chosenCharacter = null;
        this.inputsCharacter = new ArrayList<>();
    }

    private CharacterCard factoryCharacter(int i) throws UnexpectedValueException {
        switch (i) {
            case 0:
                Char0 c0 = new Char0();
                try {
                    drawStudents(c0, (byte) 4);
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
                    drawStudents(c6, (byte) 6);
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
                    drawStudents(c10, (byte) 6);
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
            Team oldController = island.getTeam();
            int maxInfluence = 0;
            Team winner = null;
            for (Team t : getTeams()) {
                int influence = 0;
                for (Color c : Color.values()) {
                    if (c != ignoredColorInfluence) {
                        for (Player p : t.getPlayers()) {
                            if (p.equals(getProfessor()[c.ordinal()]))
                                influence += island.howManyStudents(c);
                        }
                    }
                }
                if (island.getTeam() != null && towerInfluence && t.equals(island.getTeam()))
                    influence += island.getNumber();

                if (extraInfluence && getCurrentPlayer().getTeam().equals(t)) {
                    influence += 2;
                }

                if (influence > maxInfluence) {
                    winner = t;
                    maxInfluence = influence;
                } else if (influence == maxInfluence) {
                    winner = oldController;
                }
            }
            Team oldTeam = island.getTeam();
            if (oldTeam != null && winner != null && !oldTeam.equals(winner)) {
                island.setTeam(winner);
                try {
                    oldTeam.addTowers(island.getNumber());
                } catch (NotAllowedException ex) {
                    System.err.println(ex.getErrorMessage());
                }
                winner.removeTowers(island.getNumber());
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
        this.chosenCharacter = null;
        this.inputsCharacter.clear();
        super.setCurrentPlayer(p);
    }

    @Override
    public void moveMotherNature(int moves) throws NotAllowedException, EndGameException {
        if (moves+2 > super.getCurrentPlayer().getPlayedCardMoves())
            throw new NotAllowedException("Moves can't be higher than the value of the card");
        try{
            super.moveMotherNature(moves);
        }catch (NotAllowedException ignored){}
    }

    protected void calculateProfessor() {
        byte max;
        Player currentOwner;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            // player actually controlling that professor
            currentOwner = getProfessor()[c.ordinal()];

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
            getProfessor()[c.ordinal()] = newOwner;
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
        if (chosenCharacter == null) throw new NotAllowedException("Cannot play character card");
        if (chosenCharacter.canPlay(inputsCharacter.size())) {
            chosenCharacter.play(this);
            byte charCost = chosenCharacter.getCost();
            // this character card has already been used, increase its cost
            if (playedCharacters[characters.indexOf(chosenCharacter)]) charCost++;
            else {
                playedCharacters[characters.indexOf(chosenCharacter)] = true;
                // a coin is left on the character card to remember it has been used
                coinsLeft--;
            }
            // remove coins to player
            removeCoinsToPlayer(getCurrentPlayer(), charCost);

            chosenCharacter = null;
            inputsCharacter.clear();
        } else throw new UnexpectedValueException(); // canPlay returned false, needs a different amount of inputs
    }

    @Override
    public void chooseCharacter(int indexCharacter) throws GameException {
        if (indexCharacter < 0 || indexCharacter >= characters.size()) throw new UnexpectedValueException();
        CharacterCard characterToPlay = characters.get(indexCharacter);
        byte charCost = characterToPlay.getCost();
        // this character card has already been used, increase its cost
        if (playedCharacters[indexCharacter]) charCost++;
        if (charCost > coinsPlayer[getCurrentPlayer().getWizard().ordinal()])
            throw new NotAllowedException("You have not enough coins to play this card");

        chosenCharacter = characterToPlay;
    }

    @Override
    public void setCharacterInput(int input) throws GameException {
        if (chosenCharacter != null)
            inputsCharacter.add(input);
        else throw new NotAllowedException("There is no played character card");
    }

    public CharacterCard getCharacter(int index) {
        return characters.get(index);
    }

    protected void setExtraInfluence(boolean extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    protected void setTowerInfluence(boolean towerInfluence) {
        this.towerInfluence = towerInfluence;
    }

    protected void setExtraSteps(boolean extraSteps) {
        this.extraSteps = extraSteps;
    }

    protected void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

    protected void setEqualProfessorCalculation(boolean equalProfessorCalculation) {
        this.equalProfessorCalculation = equalProfessorCalculation;
    }
    // TODO by copy?
    protected ArrayList<Integer> getCharacterInputs() {
        return inputsCharacter;
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

}