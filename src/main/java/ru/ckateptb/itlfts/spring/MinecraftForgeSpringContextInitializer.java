package ru.ckateptb.itlfts.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.fml.common.Mod;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ckateptb.itlfts.utils.FutureUtils;

import java.util.concurrent.CompletableFuture;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MinecraftForgeSpringContextInitializer {
    /**
     * Создает ApplicationContext для указанного мода, применяя к нему bean того самого мода, а так же сканирует
     * и регистрирует бины находящиеся в дочерных пакетах.
     *
     * @param mod - Класс аннотированный как Mod, который расположен в корневом пакете проэкта
     * @return AnnotationConfigApplicationContext для передаваемого мода
     */
    @SneakyThrows
    public static AnnotationConfigApplicationContext initializeContext(Object mod) {
        Class<?> clazz = mod.getClass();
        Asserts.check(mod.getClass().isAnnotationPresent(Mod.class), "{} is not mod main class", clazz);
        return FutureUtils.consume(CompletableFuture.supplyAsync(() -> {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            // TODO Каждый мод работает отдельно в своем modloading-worker, их бы объединить все.
            log.info("Found a mod that requires Spring: {}", clazz);
            beanFactory.registerResolvableDependency(clazz, mod);
            String packageName = clazz.getPackage().getName();
            log.info("Spring scans all components located in {} and beyond", packageName);
            context.scan(packageName);
            return context;
        }, ModWorkManager.syncExecutor()).get(), AnnotationConfigApplicationContext::refresh);
    }
}
