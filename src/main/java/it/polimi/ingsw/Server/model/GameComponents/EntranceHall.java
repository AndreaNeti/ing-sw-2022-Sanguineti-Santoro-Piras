package it.polimi.ingsw.Server.model.GameComponents;


import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * EntranceHall class represents the player's entrance hall, used to add students to the player's LunchHalls and to the Islands.
 */
public class EntranceHall extends GameComponent {
    /**
     * Constructor EntranceHall creates a new instance of EntranceHall.
     *
     * @param entranceHallSize of type {@code int} - maximum number of students placeable on the entrance hall.
     * @param idGameComponent of type {@code byte} - unique ID to assign to the entrance hall.
     */
    public EntranceHall(int entranceHallSize, byte idGameComponent) {
        super(entranceHallSize,idGameComponent);
    }

    /**
     * Method moveAll not available for EntranceHall.
     *
     * @param destination of type {@link GameComponent} - the instance of the target component.
     * @throws NotAllowedException when this method is called on an EntranceHall.
     */
    @Override
    public void moveAll(GameComponent destination) throws NotAllowedException {
        throw new NotAllowedException("You can't moveAll from the entranceHall");
    }
}
