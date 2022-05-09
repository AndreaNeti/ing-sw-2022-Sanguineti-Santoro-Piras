package it.polimi.ingsw.Server.model;


import it.polimi.ingsw.exceptions.NotAllowedException;

public class EntranceHall extends GameComponent {
    public EntranceHall(int entranceHallSize, byte idGameComponent) {
        super(entranceHallSize,idGameComponent);
    }

    @Override
    public void moveAll(GameComponent destination) throws NotAllowedException {
        throw new NotAllowedException("You can't moveAll from the entranceHall");
    }
}
