package it.polimi.ingsw.network;

import java.util.Timer;
import java.util.TimerTask;


public class PingPong {
    private long time;
    long maxTime = 3000; //time that need to occurs before closing the connection
    private final static long period = 2000;//frequency at which ping are send

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

    public void resetTime() {
        long delta = System.currentTimeMillis() - time;
        // updates the max time dynamically in function of the response time and caps it to 15 seconds
        maxTime = Math.max(3 * Math.round((double) (maxTime / 3 + delta) / 2), 15000);
        time = System.currentTimeMillis();
    }
}
