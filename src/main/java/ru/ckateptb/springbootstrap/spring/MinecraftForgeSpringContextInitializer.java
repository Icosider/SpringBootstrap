package ru.ckateptb.springbootstrap.spring;

import com.google.common.collect.ArrayListMultimap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.http.util.Asserts;
import org.objectweb.asm.Type;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.ckateptb.springbootstrap.spring.mod.SpringContextHolder;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MinecraftForgeSpringContextInitializer {
    private static boolean initialized;

    private static final ArrayListMultimap<SpringContextHolder, Class<?>> classesMultimap = ArrayListMultimap.create();

    public static void register(SpringContextHolder holder) {
        register(holder, null);
    }

    @SafeVarargs
    public static void register(SpringContextHolder holder, String basePackage, Predicate<String>... filters) {
        Class<?> clazz = holder.getClass();
        Asserts.check(clazz.isAnnotationPresent(Mod.class), "{} is not mod main class", clazz);
        ClassLoader classLoader = clazz.getClassLoader();
        String modId = clazz.getAnnotation(Mod.class).value();
        log.info("Found a mod that requires Spring: {} ({})", modId, clazz);
        String finalBasePackage = basePackage == null ? clazz.getPackage().getName() : basePackage;
        log.info("Spring scans all components located in {} and beyond", finalBasePackage);
        Set<Class<?>> classes = ModList.get().getModFileById(modId).getFile().getScanResult().getClasses().stream()
                .map(classData -> {
                    try {
                        Field field = ModFileScanData.ClassData.class.getDeclaredField("clazz");
                        field.setAccessible(true);
                        Type type = (Type) field.get(classData);
                        return type.getClassName();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(s -> {
                    for (Predicate<String> filter : filters) {
                        if (!filter.test(s)) return false;
                    }
                    return s.startsWith(finalBasePackage);
                })
                .map(s -> {
                    try {
                        return classLoader.loadClass(s);
                    } catch (Exception ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                //START - TODO I don't like this piece of code, it could be made better!
                .filter(cl -> cl.isAnnotationPresent(Component.class)
                        || cl.isAnnotationPresent(Service.class) || cl.isAnnotationPresent(Repository.class)
                        || cl.isAnnotationPresent(Controller.class) || cl.isAnnotationPresent(Configuration.class)
                )
                //END
                .collect(Collectors.toSet());
        log.info("Spring Scan completed successfully ({} beans)!", classes.size());
        classesMultimap.putAll(holder, classes);
    }

    public static void initialize() {
        if(initialized) return;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        context.register(classesMultimap.values().toArray(new Class<?>[0]));
        classesMultimap.keySet().forEach(key -> {
            key.setContext(context);
            beanFactory.registerResolvableDependency(key.getClass(), key);
        });
        context.refresh();
        initialized = true;
    }
}
