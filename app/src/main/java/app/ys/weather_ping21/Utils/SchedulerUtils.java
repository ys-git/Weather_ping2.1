package app.ys.weather_ping21.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SchedulerUtils {

    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SchedulerUtils() {
    }

    public static ScheduledExecutorService getInstance()
    {
        return scheduler;
    }
}
