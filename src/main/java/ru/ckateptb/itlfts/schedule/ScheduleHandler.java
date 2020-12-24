package ru.ckateptb.itlfts.schedule;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.springframework.stereotype.Component;
import ru.ckateptb.itlfts.listeter.Listener;

@Component
public class ScheduleHandler implements Listener {
    private final ScheduleService scheduleService;

    public ScheduleHandler(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.scheduleService.tick();
        }
    }
}
