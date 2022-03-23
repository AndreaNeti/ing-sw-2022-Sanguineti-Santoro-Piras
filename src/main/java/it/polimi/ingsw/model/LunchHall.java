package it.polimi.ingsw.model;

public class LunchHall extends GameComponent{
    private final Player player;

    public LunchHall(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void addStudents(byte color, byte number) {
        super.addStudents(color, number);
//        byte coins = (byte) ((number + getStudents()[color]%3) / 3
    }

    public byte getStudentSize(Color color) {
        return getStudents()[color.ordinal()];
    }

    public Player getPlayer() {
        return player;
    }
}
