package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.exceptions.UsedCardException;
import it.polimi.ingsw.exceptions.EndGameException;

import java.net.Socket;
import java.util.Comparator;
import java.util.Objects;

public class Player implements Comparator<Player> {
    private final Socket socket;
    private final Team team;
    private final Wizard wizard;
    private final String nickName;
    private byte playedCard;
    private final boolean[] cardsAvailable;
    private byte cardsLeft;
    private final EntranceHall entranceHall;
    private final LunchHall lunchHall;


    public Player(Socket socket, Team team, Wizard wizard, String nickName) {
        this.socket = socket;
        this.team = team;
        this.wizard = wizard;
        this.nickName = nickName;
        this.cardsAvailable = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        this.cardsLeft = 10;
        this.playedCard = 0; // 0 = no card, else 1 to 10
        this.entranceHall = new EntranceHall(this);
        this.lunchHall = new LunchHall(this);
    }

    public void useCard(byte card) throws UsedCardException, UnexpectedValueException, NotAllowedException, EndGameException {
        if (card < 1 || card > 10) throw new UnexpectedValueException();
        if (!cardsAvailable[card]) throw new UsedCardException();
        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        playedCard = card;
        cardsLeft--;
        cardsAvailable[card] = false;
        if (cardsLeft == 0) throw new EndGameException();
    }

    public byte getPlayedCardMoves() {
        // card max movement is 1 for card 1 and 2, 2 for card 3 and 4, ..., 5 for card 9 and 10
        return (byte) ((playedCard + 1) / 2);
    }

    public byte getPlayedCard() {
        return playedCard;
    }

    public LunchHall getLunchHall() {
        return lunchHall;
    }

    public EntranceHall getEntranceHall() {
        return entranceHall;
    }

    public Wizard getWizard(){
        return wizard;
    }

    public String getNickName() {
        return nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return wizard == player.wizard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wizard);
    }

    @Override
    public int compare(Player p1, Player p2) throws ClassCastException {
        return Byte.compare(p1.getPlayedCard(), p2.getPlayedCard());

    }
}
