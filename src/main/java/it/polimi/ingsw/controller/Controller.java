package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.character.CharacterCard;

public class Controller {
    private boolean isExpertGame;
    private CharacterCard playedChar;
    private Game game;
    private ExpertGame expertGame;
    private final int matchId;

    public Controller(int matchId) {
        this.matchId = matchId;
    }

    public void move(Color color, int idGameComponent) {
        if(playedChar==null){

        }
    }

    public void playCard(int idPlayer, int valueCard) {
        if(playedChar == null){

        }
    }

    public void moveMotherNature(int i) {
        if(playedChar==null){

        }
    }

    public boolean isExpertGame() {
        return isExpertGame;
    }

    public void setCharInput(int i) {
        if (isExpertGame) {
            if (playedChar != null) playedChar.setInput(i);
        }
    }

    public void chooseChar(int i) {
        if (isExpertGame)
            playedChar = expertGame.getCharacter(i);
    }

    public void endPlayChar() {
        if (playedChar != null) {
            if (playedChar.canPlay()) {
                playedChar.play();
                playedChar = null;
            }
        }
    }

    public int getMatchId() {
        return matchId;
    }
}
