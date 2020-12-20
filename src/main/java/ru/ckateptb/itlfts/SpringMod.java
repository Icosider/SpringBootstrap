package ru.ckateptb.itlfts;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public abstract class SpringMod {
    private final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    private final static ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();


    public SpringMod() {
        Class<? extends SpringMod> clazz = this.getClass();
        log.info("Found a mod that requires Spring: {}", clazz);
        beanFactory.registerResolvableDependency(clazz, this);
        String packageName = clazz.getPackage().getName();
        log.info("Spring scans all components located in {} and beyond", packageName);
        context.scan(packageName);
    }

    void refresh() {
        context.refresh();
    }
}
