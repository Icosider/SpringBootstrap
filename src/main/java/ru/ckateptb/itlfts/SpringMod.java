package ru.ckateptb.itlfts;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
        classLoaders.add(this.getClass().getClassLoader());
    }

    void refresh() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            classLoaders.add(Thread.currentThread().getContextClassLoader());
            ClassLoader classLoader = new CompoundClassLoader(classLoaders.toArray(new ClassLoader[0]));
            CompletableFuture.supplyAsync(() -> {
                Thread.currentThread().setContextClassLoader(classLoader);
                DefaultResourceLoader loader = new DefaultResourceLoader(classLoader);
                SpringApplication springApplication = new ForgeSpringApplication(loader, mods);
                return springApplication.run();
            }, executorService).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }
}
