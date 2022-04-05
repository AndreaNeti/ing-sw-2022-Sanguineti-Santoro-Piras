package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalGameTest {
    NormalGame game;
    ArrayList<Team> teamList = new ArrayList<>(2);
    Team t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
    Team t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);
    ArrayList<Player> players = new ArrayList<>(2);
    Socket socket1 = new Socket(), socket2 = new Socket();
    Player p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
    Player p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);


    @Test
    void constructorAndGetTest() {
        players.add(p1);
        players.add(p2);
        try {
            t1.addPlayer(p1);
            t2.addPlayer(p2);
        } catch (NotAllowedException ex) {
            ex.printStackTrace();
        }
        teamList.add(t1);
        teamList.add(t2);
        game = new NormalGame((byte) 2, teamList, players);
        assertEquals(game.getTeams(), teamList);
        assertEquals(game.getPlayers(), players);
        assertEquals(game.getIslands().size(), 12);
        assertEquals(game.getBag().getClass(), Bag.class);
    }


}
