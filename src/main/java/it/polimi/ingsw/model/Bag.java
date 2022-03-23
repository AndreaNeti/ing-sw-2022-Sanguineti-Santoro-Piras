package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.Random;

public class Bag extends GameComponent{

    private final Random rand;

    public Bag() {
        super();
        rand = new Random(System.currentTimeMillis());
    }

    public void drawStudent(GameComponent component, int number) {
        for(int i = 0; i < number; i++) {
            int color = rand.nextInt(5);
            try {
                moveStudents(Color.values()[color], (byte) 1, component);
            } catch (NotEnoughStudentsException ex) {
                i--;
            }
        }
        /*if(getStudentSize() == 0) {
            Game.getGame().endGame();
        }*/
    }

    public int getStudentSize() {
        int sum = 0;
        for (byte color : getStudents()){
            sum += color;
        }
        return sum;
    }
}
