package it.polimi.ingsw.network;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Class PingPong is used to create a timeout timer in order to check if the connection between the client and the server is still working. <br>
 * This class will make the client and the server send a {@link PingMessage} at a fixed rate and if the amount of time since the last ping received is
 * too big the connection will be closed. <br>
 * Both the client and the server contain a PingPong instance and therefore have their own timeout timer.
 */
public class PingPong {
    private long time;
    long maxTime = 3000; //time that need to occur before closing the connection
    private final static long period = 2000;//frequency at which ping are send

    /**
     * Constructor PingPong creates a new instance of PingPong.
     *
     * @param pingPongController of type {@link PingPongInterface} - instance of the ControllerClient (Client side) / ClientHandler (Server side).
     */
    public PingPong(PingPongInterface pingPongController) {
        this.time = System.currentTimeMillis() + period;
        Timer pingPong = new Timer();
        pingPong.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - time > maxTime) {
                    pingPongController.quit();
                    pingPong.cancel();
                } else pingPongController.sendPingPong();
            }
        }, period, period);
    }

    /**
     * Method resetTime updates the time of the last ping received to the current one and updates the maximum time allowed based on the response time (capped at 15 seconds).
     */
    public void resetTime() {
        long delta = System.currentTimeMillis() - time;
        // updates the max time dynamically in function of the response time and caps it to 15 seconds
        maxTime = Math.max(3 * Math.round((double) (maxTime / 3 + delta) / 2), 15000);
        time = System.currentTimeMillis();
    }
}
