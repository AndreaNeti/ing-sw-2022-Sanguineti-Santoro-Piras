package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.character.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpertGame implements Game {
    private Game normalGame;
    private byte coinsLeft;
    private CharacterCard[] characters;
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
                return new Char0(this);
            case 1:
                return new Char1(this);
            case 2:
                return new Char2(this);
            case 3:
                return new Char3(this);
            case 4:
                return new Char4(this);
            case 5:
                return new Char5(this);
            case 6:
                return new Char6(this);
            case 7:
                return new Char7(this);
            case 8:
                return new Char8(this);
            case 9:
                return new Char9(this);
            case 10:
                return new Char10(this);
            case 11:
                return new Char11(this);
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
    public boolean playCard(byte card) {
        return normalGame.playCard(card);
    }

    @Override
    public void moveNotherNatur(int moves) {
        normalGame.moveNotherNatur(moves);
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
