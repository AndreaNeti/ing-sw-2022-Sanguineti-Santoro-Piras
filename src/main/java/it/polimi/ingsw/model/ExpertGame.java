package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.character.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpertGame implements Game {
    private Game normalGame;

    private byte coinsLeft;
    private byte[] coinsPlayer;

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


        int numberOfPlayers=normalGame.getPlayers().size();
        this.coinsLeft = (byte) (20-numberOfPlayers);
        this.coinsPlayer=new byte[numberOfPlayers];
        for(byte i=0; i<numberOfPlayers;i++)
            coinsPlayer[i]=1;

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


    }

    @Override
    public void calculateInfluence(Island island) {
        //prohibition is handled by prohibitionsLeft
        if(island.getProhibition()) {
            island.setProhibition(false);
            restoreProhibition();
        }

        else{

            int maxInfluence  = 0;
            Team winner = null;
            for (Team t : normalGame.getTeams()) {
                int influence = 0;
                for(Color c: Color.values()){
                    if(c != ignoredColorInfluence){
                        for(Player p: t.getPlayers()){
                            if(p.equals(normalGame.getprofessor()[c.ordinal()]))
                                influence += island.getStudentSize(c);
                        }
                    }
                }
                if(island.getTeam()!=null && towerInfluence && t.equals(island.getTeam()))
                        influence += island.getNumber();
                if(extraInfluence && normalGame.getCurrentPlayer().getTeam().equals(t))
                influence += 2;
                if(influence > maxInfluence){
                    winner = t;
                    maxInfluence = influence;
                }
            }
            Team oldTeam=island.getTeam();
            if(!oldTeam.equals(winner))
                island.setTeam(winner);


            //TODO aggiungere le torri al team perdente e toglierle da quello vincente
            /*oldTeam.addTowers(island);
            try{
                winner.removeTower(island.getNumber())
            }catch(NoMoreTowers ex) {
                endGame(Team);
            }*/

        }
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

    @Override
    public void checkMerge(Island island) {
        normalGame.checkMerge(island);
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return normalGame.getPlayers();
    }

    @Override
    public ArrayList<Team> getTeams() {
        return normalGame.getTeams();
    }

    @Override
    public Player[] getprofessor() {
        return normalGame.getprofessor();
    }


    private void addCoinsToPlayer(Player player, byte coins) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (coinsLeft < coins) {
            coinsPlayer[getPlayers().indexOf(player)] +=coinsLeft;
            coinsLeft = 0;
        } else {
            coinsPlayer[getPlayers().indexOf(player)] +=coins;
            coinsLeft -= coins;
        }

    }

    public CharacterCard getCharacter(int index) {
        return characters[index];
    }

    private void addCoins(byte coins)  {
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



    public void restoreProhibition(){
        this.prohibitionLeft++;
    }
}