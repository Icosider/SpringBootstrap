package ru.ckateptb.itlfts;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.switchyard.common.type.CompoundClassLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public abstract class SpringMod {
    private final static Set<SpringMod> mods = new HashSet<>();
    private final static Set<ClassLoader> classLoaders = new HashSet<>();

    public SpringMod() {
        mods.add(this);
        Class<? extends SpringMod> clazz = this.getClass();
        classLoaders.add(clazz.getClassLoader());
        log.info("Found a mod that requires Spring: {}", clazz);
    }

    void refresh() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        log.info("Spring starts to initialize all found class loaders ({})", classLoaders.size());
        try {
            classLoaders.add(Thread.currentThread().getContextClassLoader());
            ClassLoader classLoader = new CompoundClassLoader(classLoaders.toArray(new ClassLoader[0]));
            CompletableFuture.supplyAsync(() -> {
                Thread.currentThread().setContextClassLoader(classLoader);
                DefaultResourceLoader loader = new DefaultResourceLoader(classLoader);
                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
                ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
                context.setResourceLoader(loader);
                context.scan(mods.stream().map(mod -> {
                    beanFactory.registerResolvableDependency(mod.getClass(), mod);
                    String componentPackage = mod.getClass().getPackage().getName();
                    log.info("Spring scans all components located in {} and beyond", componentPackage);
                    return componentPackage;
                }).toArray(String[]::new));
                return context;
            }, executorService).get().refresh();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }
}
