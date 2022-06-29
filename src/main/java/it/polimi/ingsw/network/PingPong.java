package it.polimi.ingsw.network;

import java.util.Timer;
import java.util.TimerTask;


public class PingPong {
    private long time;
    long maxTime = 4000; //time that need to occurs before closing the connection
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
        time = System.currentTimeMillis();
    }
}
