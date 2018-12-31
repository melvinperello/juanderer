package melvinperello.juanderer.util;

import android.os.SystemClock;

public class TimeTool {
    /**
     * Get boot time in milliseconds.
     */
    private final static long bootTimeInMills = System.currentTimeMillis() - SystemClock.elapsedRealtime();

    public static long getBootTimeInMills() {
        return TimeTool.bootTimeInMills;
    }

    public static long getCurrentTimeInMillsFromERN(long locationElapsedRealtimeNanos) {
        long timeInMills = locationElapsedRealtimeNanos / 1000000; // convert to mills
        return TimeTool.bootTimeInMills + timeInMills;
    }

    private TimeTool() {
        // utility class
    }
}
