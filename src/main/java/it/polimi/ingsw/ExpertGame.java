package it.polimi.ingsw;

import java.util.Map;

public class ExpertGame extends Game{
    private byte coinsLeft;
    private Map<Character,Boolean> characters;
    private boolean extraInfluence; //di default falso
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private Color ignoredColorInfluence;
}
