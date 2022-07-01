package it.polimi.ingsw.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LimitedChat<String> class represent the game's chat. <br>
 * It is a linked queue with a size limit.
 *
 */
public class LimitedChat<String> extends ConcurrentLinkedQueue<String> {
    private final int size;

    /**
     * Constructor LimitedChat creates a new instance of LimitedChat.
     *
     * @param size of type {@code int} - size of the chat.
     */
    public LimitedChat(int size) {
        this.size = size;
    }

    /**
     * Method add adds a text message to the chat and removes the oldest text if the chat is full.
     *
     * @param o of type {@code String} - text message to add to the chat.
     */
    @Override
    public boolean add(String o) {
        super.offer(o);
        if (size() > size) {
            super.poll();
        }
        return true;
    }

}
