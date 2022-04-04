package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotAllowedException;

public class EntranceHall extends GameComponent{
    private final Player player;

    public EntranceHall(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void addStudents(Color color, byte number) throws NotAllowedException {
        if(howManyStudents() + number > 9)
            throw new NotAllowedException("Cannot add any more students to entrance hall");
        super.addStudents(color, number);
    }

    public Player getPlayer() {
        return player;
    }
}
