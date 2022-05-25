package it.polimi.ingsw.Client;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LimitedChat<String> extends ConcurrentLinkedQueue<String> {
    private final int size;

    public LimitedChat(int size) {
        this.size = size;
    }

    @Override
    public boolean add(String o) {
        super.offer(o);
        if (size() > size) {
            super.poll();
        }
        return true;
    }

}
