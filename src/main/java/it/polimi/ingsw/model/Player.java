package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

public class Player implements Comparator<Player> {
    private final Socket socket;
    private final Team team;
    private final Wizard wizard;
    private final String nickName;
    private final boolean[] cardsAvailable;
    private final EntranceHall entranceHall;
    private final LunchHall lunchHall;
    private byte playedCard;
    private byte cardsLeft;

    public Player(Socket socket, Team team, Wizard wizard, String nickName, int entranceHallSize) throws GameException {
        if (socket == null || team == null || nickName == null || entranceHallSize < 1)
            throw new UnexpectedValueException();
        this.socket = socket;
        this.team = team;
        team.addPlayer(this);
        this.wizard = wizard;
        this.nickName = nickName;
        this.cardsAvailable = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        this.cardsLeft = 10;
        this.playedCard = 0; // 0 = no card, else 1 to 10
        this.entranceHall = new EntranceHall(entranceHallSize);
        this.lunchHall = new LunchHall(Color.values().length * 10);
    }

    public void useCard(byte card) throws UsedCardException, UnexpectedValueException, NotAllowedException, EndGameException {
        if (card < 1 || card > 10) throw new UnexpectedValueException();
        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        if (!cardsAvailable[card - 1]) throw new UsedCardException();
        playedCard = card;
        cardsLeft--;
        cardsAvailable[card - 1] = false;
        if (cardsLeft == 0) throw new EndGameException(false);
    }

    public byte getPlayedCardMoves() {
        // card max movement is 1 for card 1 and 2, 2 for card 3 and 4, ..., 5 for card 9 and 10
        return (byte) ((playedCard + 1) / 2);
    }

    public byte getPlayedCard() {
        return playedCard;
    }

    public boolean canPlayCard(ArrayList<Byte> playedCards,byte value){
        //the value goes from 1 to 10
        if(playedCards.size()==0)
            return true;
        boolean oneDifferentCard=false;
        if(playedCards.contains(value)) {
            for (int i = 0; i < cardsAvailable.length && !oneDifferentCard; i++) {
                if (cardsAvailable[i] && !playedCards.contains((byte) (i+1))) {
                    oneDifferentCard = true;
                }
            }
            return !oneDifferentCard;
        }
        else {
            return true;
        }
    }
    public LunchHall getLunchHall() {
        return lunchHall;
    }

    public EntranceHall getEntranceHall() {
        return entranceHall;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public Socket getSocket() {
        return socket;
    }

    // TODO by value?
    public Team getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return wizard == player.wizard;
    }



    @Override
    public int compare(Player p1, Player p2) throws ClassCastException {
        return Byte.compare(p1.getPlayedCard(), p2.getPlayedCard());

    }

    @Override
    public String toString() {
        return nickName;
    }
}
