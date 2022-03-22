package it.polimi.ingsw;

public class LunchHall extends GameComponent{
    private final Player player;

    public LunchHall(Player player) {
        super(0);
        this.player = player;
    }

    @Override
    public void moveStudent(Color color, byte number, GameComponent component) {
        super.moveStudent(color, number, component);
        //if(expertGame) {
        // byte coins = (byte) ((number + getStudents()[0]%3) / 3);
        // player.addCoins(coins);
        //}
    }

    public byte getStudentSize(Color color) {
        return getStudents()[0];
    }

    public Player getPlayer() {
        return player;
    }
}
