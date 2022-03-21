package it.polimi.ingsw;

import javax.smartcardio.Card;

public class Player {
    private Wizard wizard;
    private byte towerLeft;
    private String nickName;
    private Card playedCard;
    private Card[] cards;
    private EntranceHall entranceHall;
    private LunchHall lunchHall;


    public void useCard(Card card) {

    }
    public void addTowers(byte towers){

    }

    public void removeTowers(byte towers){
    //deve chiamare endgame

    }

    public Wizard getWizard() {
        return wizard;
    }

    public LunchHall getLunchHall() {
        return lunchHall;
    }
    public EntranceHall getEntranceHall() {
        return entranceHall;
    }

    public byte getTowerLeft() {
        return towerLeft;
    }

    public Card getPlayedCard() {
        return playedCard;
    }
}
