package it.polimi.ingsw;

import java.util.Random;

public class Bag extends GameComponent{
    private static Bag bag;

    public Bag() {
        super(0);
    }

    public void drawStudent(GameComponent component, int number) {
        Random rand = new Random();
        for(int i = 0; i < number; i++) {
            int color = rand.nextInt(5);
            moveStudent(Color.values()[color], (byte) 1, component);
        }
        if(getStudentSize() == 0) {
            Game.getGame().endGame();
        }
    }

    public int getStudentSize() {
        int sum = 0;
        for (byte color : getStudents()){
            sum += color;
        }
        return sum;
    }

    public static Bag getBag() {
        if(bag == null){
            bag = new Bag();
        }
        return bag;
    }
}
