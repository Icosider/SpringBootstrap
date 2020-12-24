package ru.ckateptb.itlfts.spring;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ru.ckateptb.itlfts.listeter.Listener;
import ru.ckateptb.itlfts.schedule.ScheduleService;

import java.util.Collection;

@Configuration
public class MinecraftForgeSpringConfiguration implements SchedulingConfigurer {
    private final AnnotationConfigApplicationContext context;

    public MinecraftForgeSpringConfiguration(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    @EventListener
    public void onStartup(ContextRefreshedEvent event) {
        Collection<Listener> beans = context.getBeansOfType(Listener.class).values();
        IEventBus eventBus = context.getBean(IEventBus.class);
        beans.forEach(eventBus::register);
    }

    @Bean
    public IEventBus getModEventBus() {
        return MinecraftForge.EVENT_BUS;
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(context.getBean(ScheduleService.class));
    }
}
