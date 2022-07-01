package it.polimi.ingsw.server.model.GameComponents;

import it.polimi.ingsw.server.model.CoinListener;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughCoinsException;

import java.util.ArrayList;
import java.util.List;

/**
 * LunchHall class represents the game's Dining Room. Every player has only one LunchHall, and it has a reference to it.
 */
public class LunchHall extends GameComponent {
    private transient List<CoinListener> coinListeners;

    /**
     * Constructor LunchHall creates a new instance of LunchHall.
     *
     * @param lunchHallSize of type {@code int} - maximum amount of students per color allowed (usually 10).
     * @param idGameComponent of type {@code byte} - unique ID to assign to the lunch hall.
     */
    public LunchHall(int lunchHallSize, byte idGameComponent) {
        super(lunchHallSize, idGameComponent);
    }

    /**
     * Method canAddStudents checks if the lunch hall can receive enough students of a selected color.
     *
     * @param color of type {@link Color} - color of the students.
     * @param number of type {@code byte} - number of students.
     * @return {@code boolean} - true if the lunch hall can receive the specified number of students for the selected color, boolean false else.
     */
    @Override
    protected boolean canAddStudents(Color color, byte number) {
        if (color == null) throw new IllegalArgumentException("Null color");
        return howManyStudents(color) + number <= 10 && super.canAddStudents(color, number);
    }

    /**
     * Method moveAll not available for LunchHall.
     *
     * @param destination of type {@link GameComponent} - the instance of the target component.
     * @throws NotAllowedException when this method is called on a LunchHall.
     */
    @Override
    public void moveAll(GameComponent destination) throws NotAllowedException {
        throw new NotAllowedException("You can't moveAll from the entranceHall");
    }

    /**
     * Method addStudents adds students of a selected color to the lunch hall.
     *
     * @param color of type {@link Color} - color of the students to add.
     * @param number of type {@code byte} - number of students to add.
     * @throws NotEnoughCoinsException if there are no more coins in the game
     * to give to the player after placing 3, 6 or 9 students of the same color on the lunch hall.
     */
    @Override
    protected void addStudents(Color color, byte number) throws NotEnoughCoinsException {
        if (coinListeners != null) {
            byte coinsToAdd = (byte) ((number + howManyStudents(color) % 3) / 3);
            notifyCoins(coinsToAdd);
        }
        super.addStudents(color, number);
    }

    /**
     * Method notifyCoins is used to notify the listeners when a player receives a coin
     * after placing 3, 6 or 9 students of the same color on the lunch hall
     *
     * @param coins of type {@code byte} - number of coins received.
     * @throws NotEnoughCoinsException if there are no more coins in the game to give to the player.
     */
    private void notifyCoins(byte coins) throws NotEnoughCoinsException {
        if (coins < 1) return;
        for (CoinListener l : coinListeners)
            l.notifyCoins(coins);
    }

    /**
     * Method addCoinListener adds a listener (should be the instance of Expert Game)
     * to the lunch hall that will be notified when a player receives one or more coins.
     *
     * @param listener of type {@link CoinListener} - instance of the listener to add.
     */
    public void addCoinListener(CoinListener listener) {
        if (coinListeners == null) coinListeners = new ArrayList<>();
        coinListeners.add(listener);
    }
}
