package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.CoinListener;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughCoinsException;

import java.util.ArrayList;
import java.util.List;

//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent {
    private List<CoinListener> coinListeners;

    public LunchHall(int lunchHallSize, byte idGameComponent) {
        super(lunchHallSize, idGameComponent);
    }

    @Override
    protected boolean canAddStudents(Color c, byte number) {
        if (c == null) throw new IllegalArgumentException("Null color");
        return howManyStudents(c) + number <= 10 && super.canAddStudents(c, number);
    }

    @Override
    public void moveAll(GameComponent destination) {
        throw new IllegalArgumentException("You can't do moveAll from the lunchHall");
    }

    @Override
    protected void addStudents(Color color, byte number) throws NotEnoughCoinsException {
        if (coinListeners != null) {
            byte coinsToAdd = (byte) ((number + howManyStudents(color) % 3) / 3);
            notifyCoins(coinsToAdd);
        }
        super.addStudents(color, number);
    }

    private void notifyCoins(byte coins) throws NotEnoughCoinsException {
        if (coins < 1) return;
        for (CoinListener l : coinListeners)
            l.notifyCoins(coins);
    }

    public void addCoinListener(CoinListener l) {
        if (coinListeners == null) coinListeners = new ArrayList<>();
        coinListeners.add(l);
    }
}
