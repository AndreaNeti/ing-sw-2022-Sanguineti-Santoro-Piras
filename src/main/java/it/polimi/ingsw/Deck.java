package it.polimi.ingsw;
import java.util.*;

public class Deck {
    private Mage mage;
    private List<AssistantCard> cards;

    public Mage getMage() {
        return mage;
    }
    public void removeCard(AssistantCard card){
        cards.remove(card);
    }
}
