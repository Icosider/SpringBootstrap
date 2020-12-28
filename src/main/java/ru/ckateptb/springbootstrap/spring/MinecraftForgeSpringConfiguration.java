package ru.ckateptb.springbootstrap.spring;

import lombok.extern.log4j.Log4j2;
import net.minecraftforge.common.MinecraftForge;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ru.ckateptb.springbootstrap.listeter.Listener;
import ru.ckateptb.springbootstrap.schedule.ScheduleService;

import java.util.Collection;
import java.util.stream.Collectors;

@Log4j2
@Configuration
public class MinecraftForgeSpringConfiguration implements SchedulingConfigurer {
    private final AnnotationConfigApplicationContext context;

    public MinecraftForgeSpringConfiguration(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    @EventListener
    public void onStartup(ContextRefreshedEvent event) {
        Collection<Listener> listeners = context.getBeansOfType(Listener.class).values();
        listeners.forEach(MinecraftForge.EVENT_BUS::register);
        log.info("SpringBootstrap finished its initialization, you can see the bean list in debug mode");
        log.debug("Beans: \n\t{}", context.getBeansOfType(Object.class).values()
                .stream().map(Object::getClass).map(Class::toString).collect(Collectors.joining("\n\t")));
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(context.getBean(ScheduleService.class));
    }
}
