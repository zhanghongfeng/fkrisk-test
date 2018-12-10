package com.dashu.fk.test.tools;

/**
 * Created by zhf2015 on 17/4/19.
 */
public class Sleep {

    /**
     * Sleep how much millis
     *
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleep how much seconds
     *
     * @param seconds
     */
    public static void sleepseconds(int seconds) {
        sleep(seconds * 1000);
    }

}
