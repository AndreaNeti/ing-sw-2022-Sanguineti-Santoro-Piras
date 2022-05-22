package it.polimi.ingsw.Client;

import java.util.LinkedList;

public class LimitedChat<String> extends LinkedList<String> {
    private final int size;

    public LimitedChat(int size) {
        this.size = size;
    }

    @Override
    public boolean add(String o) {
        super.add(o);
        if (size() > size) {
            super.remove();
        }
        return true;
    }

}
