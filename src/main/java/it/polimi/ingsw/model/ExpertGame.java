package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import it.polimi.ingsw.model.character.*;

import java.util.ArrayList;
import java.util.Random;

public class ExpertGame implements Game {
    private Game normalGame;
    private byte coinsLeft;
    private final CharacterCard[] characters;
    private boolean[] playedCharacters;

    private boolean extraInfluence; //default false
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private boolean equalProfessorCalculation; //default false
    private Color ignoredColorInfluence;
    private byte prohibitionLeft;
    private CharacterCard playedCharacter;
    private ArrayList<Integer> inputsCharacter;

    public ExpertGame(Game game) {
        if (game != null) {
            this.normalGame = game;
        } else
            System.err.println("Game cannot be null");
        this.coinsLeft = 20;


        characters = new CharacterCard[3];
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
                }
            }
            if (!alreadyPresent) {
                CharacterCard c = factoryMethod(characterIndex);
                characters[i] = c;
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
        this.playedCharacter = null;
        this.inputsCharacter = null;
    }

    private CharacterCard factoryMethod(int i) {

        switch (i) {
            case 0:
                Char0 c0 = new Char0();
                normalGame.drawStudents(c0, (byte) 4);
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
                normalGame.drawStudents(c6, (byte) 6);
                return c6;
            case 7:
                return new Char7();
            case 8:
                return new Char8();
            case 9:
                return new Char9();
            case 10:
                Char10 c10 = new Char10();
                normalGame.drawStudents(c10, (byte) 6);
                return c10;
            case 11:
                return new Char11();
        }
        return null;
    }

    @Override
    public void move(Color color, int idGameComponent) {
        normalGame.move(color, idGameComponent);
    }

    @Override
    public void drawStudents(GameComponent bag, byte number) {
        normalGame.drawStudents(bag, number);
    }

    @Override
    public Player getCurrentPlayer() {
        return normalGame.getCurrentPlayer();
    }

    @Override
    public void playCard(byte card) {
        normalGame.playCard(card);
    }

    @Override
    public void endGame() {
        normalGame.endGame();
    }

    @Override
    public void endGame(Team team) {
        normalGame.endGame(team);
    }

    @Override
    public void nextPlayer() {
        normalGame.nextPlayer();
    }

    @Override
    public void nextPhase() {
        normalGame.nextPhase();
    }

    @Override
    public boolean getPhase() {
        return normalGame.getPhase();
    }

    @Override
    public void nextActionPhase() {
        normalGame.nextActionPhase();
    }

    @Override
    public void calculateInfluence() {
        normalGame.calculateInfluence();
    }

    @Override
    public void calculateInfluence(Island island) {
        normalGame.calculateInfluence(island);
    }

    @Override
    public void moveMotherNature(int moves) {
        //TODO controllare il boolean di extra steps
        normalGame.moveMotherNature(moves);
    }

    @Override
    public void calculateProfessor() {
        //TODO vedere come gestire il boolean
        normalGame.calculateProfessor();
    }

    @Override
    public void refillClouds() {
        normalGame.refillClouds();
    }

    @Override
    public void setLastRound() {
        normalGame.setLastRound();
    }

    @Override
    public void setCharacterInput(int input) {
        //TODO implementare setCharacterInput
    }

    @Override
    public void chooseCharacter(int indexCharacter) throws NotExpertGameException {
        //TODO implementare chooseCharacter
    }

    @Override
    public void playCharacter() throws NotExpertGameException {
        //TODO implementare playCharacter
    }

    private void addCoinsToPlayer(ExpertPlayer player, byte coins) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (coinsLeft < coins) {
            player.addCoins(coinsLeft);
            coinsLeft = 0;
        } else {
            player.addCoins(coins);
            coinsLeft -= coins;
        }

    }

    public CharacterCard getCharacter(int index) {
        return characters[index];
    }

    private void addCoins(byte coins) {
        this.coinsLeft += coins;
    }

    public boolean isExtraInfluence() {
        return extraInfluence;
    }

    public void setExtraInfluence(boolean extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    public boolean isTowerInfluence() {
        return towerInfluence;
    }

    public void setTowerInfluence(boolean towerInfluence) {
        this.towerInfluence = towerInfluence;
    }

    public boolean isExtraSteps() {
        return extraSteps;
    }

    public void setExtraSteps(boolean extraSteps) {
        this.extraSteps = extraSteps;
    }

    public Color getIgnoredColorInfluence() {
        return ignoredColorInfluence;
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

}
