package ru.ckateptb.itlfts;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Set;

public class ForgeSpringApplication extends SpringApplication {
    private final Set<SpringMod> mods;

    public ForgeSpringApplication(DefaultResourceLoader loader, Set<SpringMod> mods) {
        super(loader, SpringSource.class);
        this.mods = mods;
    }

    @SneakyThrows
    @Override
    protected ConfigurableApplicationContext createApplicationContext() {
        ConfigurableApplicationContext context = super.createApplicationContext();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) context);
        scanner.addExcludeFilter(((metadataReader, metadataReaderFactory) -> false));
        // todo НЕ ОБРАБАТЫВАЕТ ПАКЕТЫ СТОРОННИХ МОДОВ
        scanner.scan(mods.stream().map(mod -> {
            beanFactory.registerResolvableDependency(mod.getClass(), mod);
            return mod.getClass().getPackage().getName();
        }).toArray(String[]::new));
//        BeanDefinitionRegistry registry = scanner.getRegistry();
//        if (registry != null) {
//            for (String name : registry.getBeanDefinitionNames()) {
//                beanFactory.createBean(registry.getBeanDefinition(name).getClass());
//            }
//        }
        return context;
    }
}
