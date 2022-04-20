
package com.one.hotspot.vpn.free.master.util;

import java.util.Timer;
import java.util.TimerTask;

public class StopWatch {
    private long time_counter = 45;
    private Timer timer;

    public void startCountDown(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println((--time_counter));
                if(time_counter == 0){
                    timer.cancel();
                }
            }
        },0,1000);
         }


    public static void main(String[] args){
        StopWatch s = new StopWatch();
        s.startCountDown();
    }
}