package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.character.*;

import java.util.ArrayList;
import java.util.Random;

public class ExpertGame implements Game {
    private final byte[] coinsPlayer;
    private final ArrayList<CharacterCard> characters;
    private final ArrayList<Integer> inputsCharacter;
    private final NormalGame normalGame;
    private byte coinsLeft;
    private boolean[] playedCharacters;
    private boolean extraInfluence; //default false
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence;
    private byte prohibitionLeft;
    private CharacterCard chosenCharacter;


    public ExpertGame(NormalGame game) {
        if (game != null) {
            this.normalGame = game;
        } else {
            System.err.println("Game cannot be null");
            throw new NullPointerException();
        }

        int numberOfPlayers = normalGame.getPlayers().size();
        this.coinsLeft = (byte) (20 - numberOfPlayers);
        this.coinsPlayer = new byte[numberOfPlayers];
        for (byte i = 0; i < numberOfPlayers; i++)
            coinsPlayer[i] = 1;

        characters = new ArrayList<>(3);
        Random rand = new Random(System.currentTimeMillis());
        playedCharacters = new boolean[]{false, false, false};
        int characterIndex;
        byte i = 0;
        //mette tre carte diverse in characters
        boolean alreadyPresent;
        while (i < 3) {
            alreadyPresent = false;
            characterIndex = rand.nextInt(12);
            for (CharacterCard c : characters) {
                if (c.getId() == characterIndex) {
                    alreadyPresent = true;
                    break; // :D
                }
            }
            if (!alreadyPresent) {
                CharacterCard c = factoryMethod(characterIndex);
                characters.add(c);
                i++;

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
        this.inputsCharacter = null;
    }

    private CharacterCard factoryMethod(int i) {

        switch (i) {
            case 0:
                Char0 c0 = new Char0();
                try {
                    normalGame.drawStudents(c0, (byte) 4);
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
                    normalGame.drawStudents(c6, (byte) 6);
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
        return null;
    }

    protected void moveById(Color color, int idSource, int idDestination) throws GameException {
        //TODO use this functin
        normalGame.moveById(color, idSource, idDestination);
        if (idDestination == 1 && getCurrentPlayer().getLunchHall().howManyStudents(color) % 3 == 0) {
            addCoinsToPlayer(getCurrentPlayer(), (byte) 1);
        }
    }

    @Override
    public void move(Color color, int idGameComponent, byte actionPhase) throws GameException {
        normalGame.move(color, idGameComponent, actionPhase);
        if (actionPhase >= 1 && actionPhase <= 3 && idGameComponent == 1) {
            if (getCurrentPlayer().getLunchHall().howManyStudents(color) % 3 == 0)
                addCoinsToPlayer(getCurrentPlayer(), (byte) 1);
        }

    }

    @Override
    public void drawStudents(GameComponent gameComponent, byte number) throws EndGameException {
        normalGame.drawStudents(gameComponent, number);
    }

    @Override
    public Player getCurrentPlayer() {
        return normalGame.getCurrentPlayer();
    }

    @Override
    public void setCurrentPlayer(Player p) {
        normalGame.setCurrentPlayer(p);
    }

    @Override
    public void playCard(byte card) throws GameException, EndGameException {
        normalGame.playCard(card);
    }

    @Override
    public Team calculateWinner() {
        return normalGame.calculateWinner();
    }


    public ArrayList<Player> getPlayers() {
        return normalGame.getPlayers();
    }

    public Player[] getProfessor() {
        return normalGame.getProfessor();
    }

    @Override
    public void refillClouds() throws EndGameException {
        normalGame.refillClouds();
    }


    //calculate expertInfluence(it checks all the boolean) and then calls checkMerge

    public void calculateInfluence(Island island) throws EndGameException {
        //prohibition is handled by prohibitionsLeft
        if (island.getProhibitions() > 0) {
            island.removeProhibition();
            restoreProhibition();
        } else {

            int maxInfluence = 0;
            Team winner = null;
            for (Team t : normalGame.getTeams()) {
                int influence = 0;
                for (Color c : Color.values()) {
                    if (c != ignoredColorInfluence) {
                        for (Player p : t.getPlayers()) {
                            if (p.equals(normalGame.getProfessor()[c.ordinal()]))
                                influence += island.howManyStudents(c);
                        }
                    }
                }
                if (island.getTeam() != null && towerInfluence && t.equals(island.getTeam()))
                    influence += island.getNumber();

                if (extraInfluence && normalGame.getCurrentPlayer().getTeam().equals(t)) {
                    influence += 2;
                }

                if (influence > maxInfluence) {
                    winner = t;
                    maxInfluence = influence;
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
                normalGame.checkMerge(island);
            }
        }
    }


    @Override
    public void moveMotherNature(int moves) throws NotAllowedException, EndGameException {
        if (extraSteps)
            if (moves + 2 > normalGame.getCurrentPlayer().getPlayedCardMoves())
                throw new NotAllowedException("Moves can't be higher than the value of the card");
        normalGame.moveMotherNature(moves);
    }

    //TODO da agiungere nella moveExpert
    public void calculateProfessor() {
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

    private void addCoinsToPlayer(Player player, byte coins) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (coinsLeft < coins) {
            coinsPlayer[getPlayers().indexOf(player)] += coinsLeft;
            coinsLeft = 0;
        } else {
            coinsPlayer[getPlayers().indexOf(player)] += coins;
            coinsLeft -= coins;
        }

    }

    public GameComponent getBag() {
        return normalGame.getBag();
    }

    private void addCoins(byte coins) {
        this.coinsLeft += coins;
    }

    @Override
    public void playCharacter() throws GameException, EndGameException {
        if (chosenCharacter == null) throw new NotAllowedException("Cannot play character card");
        if (chosenCharacter.canPlay(inputsCharacter.size())) {
            chosenCharacter.play(this);
            byte charCost = chosenCharacter.getCost();
            // this character card has already been used, increase its cost
            if (playedCharacters[characters.indexOf(chosenCharacter)]) charCost++;
            // remove coins to player
            coinsPlayer[getCurrentPlayer().getWizard().ordinal()] -= charCost;
            // add coins to game
            addCoins(charCost);
            chosenCharacter = null;
            inputsCharacter.clear();
        } else throw new UnexpectedValueException(); // canPlay returned false, needs a different amount of inputs
    }

    @Override
    public void chooseCharacter(int indexCharacter) throws UnexpectedValueException, NotAllowedException {
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
    public void setCharacterInput(int input) throws NotAllowedException {
        if (chosenCharacter != null)
            inputsCharacter.add(input);
        else throw new NotAllowedException("There is no played character card");
    }

    public CharacterCard getCharacter(int index) {
        return characters.get(index);
    }

    public void setExtraInfluence(boolean extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    public void setTowerInfluence(boolean towerInfluence) {
        this.towerInfluence = towerInfluence;
    }

    public void setExtraSteps(boolean extraSteps) {
        this.extraSteps = extraSteps;
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

    public void setEqualProfessorCalculation(boolean equalProfessorCalculation) {
        this.equalProfessorCalculation = equalProfessorCalculation;
    }

    public ArrayList<Integer> getCharacterInputs() {
        return inputsCharacter;
    }

    public ArrayList<Island> getIslands() {
        return normalGame.getIslands();
    }

    public void setProhibition(int idIsland) throws NotAllowedException {
        if (this.prohibitionLeft > 0)
            this.prohibitionLeft--;
        else throw new NotAllowedException("No more prohibitions");
        getIslands().get(idIsland).addProhibitions((byte) 1);
    }

    private void restoreProhibition() {
        if (this.prohibitionLeft < 4) {
            this.prohibitionLeft++;
        }
    }

}