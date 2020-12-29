package ru.ckateptb.springbootstrap.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.springframework.stereotype.Service;
import ru.ckateptb.springbootstrap.listeter.Listener;
import ru.ckateptb.springbootstrap.schedule.api.AbstractScheduledExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@NoArgsConstructor
public class ScheduleService extends AbstractScheduledExecutorService implements Listener {
    private final List<ScheduleTask> schedulers = new ArrayList<>();

    public ScheduleTask schedule(Runnable task, long delay, long rate) {
        ScheduleTask scheduleTask = new ScheduleTask(this, task, delay, rate);
        schedulers.add(scheduleTask);
        return scheduleTask;
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tick();
        }
    }

    @Getter
    private static class ScheduleTask {
        private final ScheduleService service;
        private final Runnable runnable;
        private final long rate;

        private long ticksLeft;

        private ScheduleTask(ScheduleService service, Runnable runnable, long delay, long rate) {
            this.service = service;
            this.runnable = runnable;
            this.ticksLeft = Math.max(delay, 0);
            this.rate = rate;
        }


        private void tick() {
            if (ticksLeft-- == 0) {
                runnable.run();
                if (rate <= 0) service.remove(this);
                else ticksLeft = rate;
            }
        }
    }
}
