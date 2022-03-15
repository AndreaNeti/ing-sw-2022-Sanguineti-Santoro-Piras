package it.polimi.ingsw;

public class DashBoard {
    private final Player player;
    private final Hall[] halls;

    public DashBoard(Player p){
        this.player = p;
        halls = new Hall[4];
    }

    public Hall getHall(int hall){
        return halls[hall];
    }
}
