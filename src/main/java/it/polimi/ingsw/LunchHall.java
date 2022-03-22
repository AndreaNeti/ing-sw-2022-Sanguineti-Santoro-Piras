package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public class LunchHall extends GameComponent{
    private final Player player;

    public LunchHall(Player player) {
        super(0);
        this.player = player;
    }

    @Override
    public void addStudents(byte color, byte number) {
        super.addStudents(color, number);
        //if(Controller.getController().isExpert()) {
        byte coins = (byte) ((number + getStudents()[color]%3) / 3);
        ((ExpertPlayer)player).addCoins(coins);
        //}
    }

    public byte getStudentSize(Color color) {
        return getStudents()[color.ordinal()];
    }

    public Player getPlayer() {
        return player;
    }
}
