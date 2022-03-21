package it.polimi.ingsw;


import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private final Wizard wizard;
    private byte towerLeft;
    private final String nickName;
    private Card playedCard;
    private ArrayList<Card> cards;
    private final EntranceHall entranceHall;
    private final LunchHall lunchHall;



    public Player(Wizard wizard, byte tower,String nickName, EntranceHall entranceHall, LunchHall lunchHall){
        this.wizard=wizard;
        this.towerLeft=tower;
        this.nickName=nickName;
        this.playedCard=null;

        this.cards=new ArrayList<>(10);
        for(byte i=0; i<10; i++){
            byte j= (byte) ( i/2 +1 ) ;
            Card prova=new Card(i,j, this);
            cards.add(prova);

        }

        this.entranceHall=entranceHall;
        this.lunchHall=lunchHall;
    }

    public void useCard(Card card) throws  CardNotFoundException{
        if(cards.contains(card)) {
            this.playedCard = card;
            cards.remove(card);
        }
        else {
            throw new CardNotFoundException();
        }

    }
    public void addTowers(byte towers){

        towerLeft+=towers;
    }

    public void removeTowers(byte towers){
    //deve chiamare endgame
        towerLeft-=towers;
        if(towerLeft<=0){
            towerLeft=0;
            Game.getGame().endGame(this);
        }
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
