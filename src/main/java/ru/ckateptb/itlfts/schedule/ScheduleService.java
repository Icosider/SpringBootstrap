package ru.ckateptb.itlfts.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.ckateptb.itlfts.schedule.api.AbstractScheduledExecutorService;
import ru.ckateptb.itlfts.utils.FutureUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@NoArgsConstructor
public class ScheduleService extends AbstractScheduledExecutorService {
    private final List<ScheduleTask> schedulers = new ArrayList<>();

    public ScheduleTask schedule(Runnable task, long delay, long rate) {
        return FutureUtils.consume(new ScheduleTask(task, delay, rate), schedulers::add);
    }

    public boolean remove(ScheduleTask task) {
        return schedulers.remove(task);
    }

    public void tick() {
        new CopyOnWriteArrayList<>(schedulers).forEach(ScheduleTask::tick);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        initialDelay = unit.toSeconds(initialDelay) / 50;
        period = unit.toSeconds(period) / 50;
        schedule(command, initialDelay, period);
        return null;
    }

    @Getter
    private static class ScheduleTask {
        private final Runnable runnable;
        private final long delay;
        private final long rate;

        private long ticksLeft;

        private ScheduleTask(Runnable runnable, long delay, long rate) {
            this.runnable = runnable;
            this.ticksLeft = delay;
            this.delay = delay;
            this.rate = rate;
        }


        private void tick() {
            if (ticksLeft-- == 0) {
                runnable.run();
                ticksLeft = delay + rate;
            }
        }
    }
}